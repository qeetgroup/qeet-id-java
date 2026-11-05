package com.qeetgroup.qeetid.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.qeetgroup.qeetid.exception.WebhookVerificationException;
import com.qeetgroup.qeetid.exception.WebhookVerificationException.Reason;
import com.qeetgroup.qeetid.internal.Json;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.jspecify.annotations.Nullable;

/**
 * Verifies webhook deliveries and returns their events. Build one per subscription secret (or pair,
 * during a rotation) and share it: it is immutable and thread-safe.
 *
 * <pre>{@code
 * WebhookVerifier verifier = WebhookVerifier.builder().secret(System.getenv("QEET_ID_WEBHOOK_SECRET")).build();
 * WebhookEvent event = verifier.verify(rawBody, request::getHeader); // throws WebhookVerificationException
 * }</pre>
 *
 * <p>Each delivery's {@code X-Qeet-Signature} is {@code sha256=} and the hex HMAC-SHA256, keyed by
 * the subscription's secret, of {@code X-Qeet-Timestamp}, {@code "."} and the raw body. Every value
 * in the header is compared with every secret, in constant time, and a delivery signed more than
 * the tolerance (five minutes) from now is refused as a possible replay. Always pass the raw body,
 * exactly as received: a parsed and re-serialised one won't match.
 */
public final class WebhookVerifier {
  /** By default a delivery signed more than five minutes from now is refused. */
  public static final Duration DEFAULT_TOLERANCE = Duration.ofMinutes(5);

  private static final String SCHEME = "sha256=";
  private static final String HMAC = "HmacSHA256";

  private final List<SecretKeySpec> keys;
  private final Duration tolerance;
  private final Clock clock;

  private WebhookVerifier(Builder builder) {
    this.keys =
        builder.secrets.stream()
            .map(secret -> new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC))
            .toList();
    this.tolerance = builder.tolerance;
    this.clock = builder.clock;
  }

  /**
   * Starts building a verifier.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Verifies a delivery and returns its event.
   *
   * @param payload the raw request body, exactly as received
   * @param headers looks a request header up by name, case-insensitively, such as {@code
   *     request::getHeader} (Servlet) or {@code headers::getFirst} (Spring's {@code HttpHeaders})
   * @return the event
   * @throws WebhookVerificationException when the delivery is not authentic, too old, or not an
   *     event
   */
  public WebhookEvent verify(byte[] payload, Function<String, @Nullable String> headers) {
    verifySignature(
        payload, headers.apply(WebhookHeaders.SIGNATURE), headers.apply(WebhookHeaders.TIMESTAMP));
    return parse(payload);
  }

  /**
   * Verifies a delivery whose headers are a map, such as the JDK HTTP server's.
   *
   * @param payload the raw request body, exactly as received
   * @param headers the request headers; names are matched case-insensitively
   * @return the event
   * @throws WebhookVerificationException when the delivery is not authentic, too old, or not an
   *     event
   */
  public WebhookEvent verify(byte[] payload, Map<String, ? extends Collection<String>> headers) {
    return verify(payload, name -> header(headers, name));
  }

  /**
   * Verifies a delivery whose body is already text. It must be the exact text received.
   *
   * @param payload the raw request body, exactly as received
   * @param headers looks a request header up by name, case-insensitively
   * @return the event
   * @throws WebhookVerificationException when the delivery is not authentic, too old, or not an
   *     event
   */
  public WebhookEvent verify(String payload, Function<String, @Nullable String> headers) {
    return verify(payload.getBytes(StandardCharsets.UTF_8), headers);
  }

  /**
   * Checks a delivery's signature and timestamp, without parsing its body.
   *
   * @param payload the raw request body, exactly as received
   * @param signature the {@code X-Qeet-Signature} header, or null when absent
   * @param timestamp the {@code X-Qeet-Timestamp} header, or null when absent
   * @throws WebhookVerificationException unless both are good
   */
  public void verifySignature(
      byte[] payload, @Nullable String signature, @Nullable String timestamp) {
    String sentAt = timestamp == null ? "" : timestamp.trim();
    if (sentAt.isEmpty()) {
      throw new WebhookVerificationException(
          Reason.MISSING_TIMESTAMP, "The delivery has no X-Qeet-Timestamp header.");
    }
    if (!sentAt.matches("\\d{1,15}")) {
      throw new WebhookVerificationException(
          Reason.INVALID_TIMESTAMP, "X-Qeet-Timestamp is not Unix seconds.");
    }
    long skew = Math.abs(clock.instant().getEpochSecond() - Long.parseLong(sentAt));
    if (Duration.ofSeconds(skew).compareTo(tolerance) > 0) {
      throw new WebhookVerificationException(
          Reason.TIMESTAMP_OUTSIDE_TOLERANCE,
          "The delivery was signed more than "
              + tolerance.toSeconds()
              + " seconds from now; refusing it as a possible replay.");
    }
    String header = signature == null ? "" : signature.trim();
    if (header.isEmpty()) {
      throw new WebhookVerificationException(
          Reason.MISSING_SIGNATURE, "The delivery has no X-Qeet-Signature header.");
    }
    List<byte[]> signatures = parseSignatures(header);
    if (signatures.isEmpty()) {
      throw new WebhookVerificationException(
          Reason.INVALID_SIGNATURE_HEADER, "X-Qeet-Signature holds no sha256=<hex> value.");
    }
    boolean matched = false;
    for (SecretKeySpec key : keys) {
      byte[] expected = hmac(key, sentAt, payload);
      for (byte[] candidate : signatures) {
        // No early exit: the work done doesn't depend on which value matched.
        matched |= MessageDigest.isEqual(expected, candidate);
      }
    }
    if (!matched) {
      throw new WebhookVerificationException(
          Reason.SIGNATURE_MISMATCH,
          "The delivery's signature does not match: it was not signed with this secret.");
    }
  }

  /**
   * Signs a body the way Qeet ID does, for testing a webhook handler.
   *
   * @param payload the body
   * @param secret the subscription's secret
   * @param timestamp when it is signed
   * @return the {@code X-Qeet-Timestamp} and {@code X-Qeet-Signature} values
   */
  public static Signature sign(byte[] payload, String secret, Instant timestamp) {
    String sentAt = Long.toString(timestamp.getEpochSecond());
    byte[] mac =
        hmac(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC), sentAt, payload);
    return new Signature(sentAt, SCHEME + HexFormat.of().formatHex(mac));
  }

  /**
   * The headers {@link #sign} makes.
   *
   * @param timestamp the {@code X-Qeet-Timestamp} value
   * @param signature the {@code X-Qeet-Signature} value
   */
  public record Signature(String timestamp, String signature) {}

  @Override
  public String toString() {
    return "WebhookVerifier[secrets=" + keys.size() + ", tolerance=" + tolerance + "]";
  }

  private static byte[] hmac(SecretKeySpec key, String timestamp, byte[] payload) {
    try {
      Mac mac = Mac.getInstance(HMAC);
      mac.init(key);
      mac.update(timestamp.getBytes(StandardCharsets.US_ASCII));
      mac.update((byte) '.');
      return mac.doFinal(payload);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new IllegalStateException("HMAC-SHA256 is unavailable in this JVM.", e);
    }
  }

  private static List<byte[]> parseSignatures(String header) {
    List<byte[]> out = new ArrayList<>();
    for (String part : header.split(",", -1)) {
      String entry = part.trim();
      if (!entry.startsWith(SCHEME)) {
        continue; // a scheme this SDK doesn't know: ignored, never trusted
      }
      String hex = entry.substring(SCHEME.length());
      if (hex.matches("[0-9a-fA-F]{64}")) {
        out.add(HexFormat.of().parseHex(hex));
      }
    }
    return out;
  }

  private static @Nullable String header(
      Map<String, ? extends Collection<String>> headers, String name) {
    for (Map.Entry<String, ? extends Collection<String>> entry : headers.entrySet()) {
      if (entry.getKey() != null
          && entry.getKey().equalsIgnoreCase(name)
          && entry.getValue() != null) {
        return String.join(",", entry.getValue());
      }
    }
    return null;
  }

  private static WebhookEvent parse(byte[] payload) {
    JsonNode root;
    try {
      root = Json.mapper().readTree(payload);
    } catch (IOException e) {
      throw new WebhookVerificationException(
          Reason.INVALID_PAYLOAD, "The delivery's body is not JSON.");
    }
    if (root == null
        || !root.isObject()
        || !root.path("id").isTextual()
        || !root.path("type").isTextual()
        || !root.path("tenant_id").isTextual()
        || !root.path("occurred_at").isTextual()
        || !root.path("data").isObject()) {
      throw new WebhookVerificationException(
          Reason.INVALID_PAYLOAD,
          "The delivery's body is not a Qeet ID event (id, type, tenant_id, occurred_at, data).");
    }
    Instant occurredAt;
    try {
      occurredAt = Instant.parse(root.get("occurred_at").asText());
    } catch (DateTimeException e) {
      throw new WebhookVerificationException(
          Reason.INVALID_PAYLOAD, "The event's occurred_at is not an RFC 3339 time.");
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> data = Json.mapper().convertValue(root.get("data"), LinkedHashMap.class);
    return new WebhookEvent(
        root.get("id").asText(),
        root.get("type").asText(),
        root.get("tenant_id").asText(),
        occurredAt,
        data);
  }

  /** Builds a {@link WebhookVerifier}. */
  public static final class Builder {
    private final List<String> secrets = new ArrayList<>();
    private Duration tolerance = DEFAULT_TOLERANCE;
    private Clock clock = Clock.systemUTC();

    private Builder() {}

    /**
     * Adds a subscription's signing secret. While you rotate it, add both the new and the old one:
     * a delivery signed with either is accepted.
     *
     * @param secret the secret
     * @return this builder
     */
    public Builder secret(String secret) {
      Objects.requireNonNull(secret, "secret");
      if (secret.isEmpty()) {
        throw new IllegalArgumentException("A webhook secret must not be empty.");
      }
      secrets.add(secret);
      return this;
    }

    /**
     * The largest gap between a delivery's timestamp and now, either way. Default five minutes.
     * Keep it small: it bounds how long a captured delivery can be replayed.
     *
     * @param tolerance more than zero
     * @return this builder
     */
    public Builder tolerance(Duration tolerance) {
      Objects.requireNonNull(tolerance, "tolerance");
      if (tolerance.isZero() || tolerance.isNegative()) {
        throw new IllegalArgumentException("tolerance must be greater than zero.");
      }
      this.tolerance = tolerance;
      return this;
    }

    /**
     * The clock "now" is read from. For tests.
     *
     * @param clock the clock
     * @return this builder
     */
    public Builder clock(Clock clock) {
      this.clock = Objects.requireNonNull(clock, "clock");
      return this;
    }

    /**
     * Builds the verifier.
     *
     * @return the verifier
     * @throws IllegalStateException when no secret was given
     */
    public WebhookVerifier build() {
      if (secrets.isEmpty()) {
        throw new IllegalStateException("A webhook verifier needs at least one secret.");
      }
      return new WebhookVerifier(this);
    }
  }
}
