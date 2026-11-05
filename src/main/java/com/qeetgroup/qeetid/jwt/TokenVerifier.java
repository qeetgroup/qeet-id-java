package com.qeetgroup.qeetid.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.qeetgroup.qeetid.exception.TokenVerificationException;
import com.qeetgroup.qeetid.exception.TokenVerificationException.Reason;
import com.qeetgroup.qeetid.internal.Json;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

/**
 * Verifies Qeet ID access tokens locally, against the issuer's published signing keys (JWKS), with
 * the JDK's own cryptography: no network call per token once the keys are cached. Build one per
 * process and share it: it is thread-safe.
 *
 * <pre>{@code
 * TokenVerifier verifier = TokenVerifier.builder()
 *     .discovery(Discovery.fetch(QeetID.DEFAULT_BASE_URL))   // issuer and JWKS URI
 *     .audience("https://api.brightleaffoods.in")
 *     .build();
 * AccessTokenClaims claims = verifier.verify(bearerToken);    // throws TokenVerificationException
 * }</pre>
 *
 * <p>It accepts ES256 only and needs a known {@code kid}; it checks the signature, {@code iss},
 * {@code aud}, {@code exp}, {@code nbf} and {@code iat}, with a clock leeway (30 seconds by
 * default). Keys are cached for five minutes; a token naming an unknown {@code kid} refetches them
 * at most every 30 seconds, so made-up kids can't flood the issuer. If a refresh fails, the keys
 * already held keep verifying until the issuer answers again.
 *
 * <p>A token verified offline stays valid until it expires (minutes), even if its session was
 * revoked meanwhile. Where revocation must take effect at once, introspect instead ({@code
 * qeetId.oauth().introspect}).
 */
public final class TokenVerifier {
  /** The default leeway for clock differences when checking times. */
  public static final Duration DEFAULT_CLOCK_SKEW = Duration.ofSeconds(30);

  static final Duration CACHE_TTL = Duration.ofMinutes(5);
  static final Duration REFRESH_COOLDOWN = Duration.ofSeconds(30);
  private static final Duration JWKS_TIMEOUT = Duration.ofSeconds(10);
  private static final int MAX_TOKEN_LENGTH = 16 * 1024;
  private static final Pattern BASE64URL = Pattern.compile("[A-Za-z0-9_-]*");

  private final String issuer;
  private final List<String> audiences;
  private final URI jwksUri;
  private final Duration clockSkew;
  private final HttpClient httpClient;
  private final Clock clock;

  private final ReentrantLock lock = new ReentrantLock();
  private volatile Map<String, PublicKey> keys = Map.of();
  private volatile @Nullable Instant fetchedAt;
  private volatile @Nullable Instant lastAttempt;

  private TokenVerifier(Builder builder, String issuer, URI jwksUri) {
    this.issuer = issuer;
    this.audiences = List.copyOf(builder.audiences);
    this.jwksUri = jwksUri;
    this.clockSkew = builder.clockSkew;
    HttpClient client = builder.httpClient;
    this.httpClient =
        client != null
            ? client
            : HttpClient.newBuilder()
                .connectTimeout(JWKS_TIMEOUT)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
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
   * The issuer tokens must carry.
   *
   * @return the issuer
   */
  public String issuer() {
    return issuer;
  }

  /**
   * The audiences a token may be issued for; it must name at least one.
   *
   * @return the audiences
   */
  public List<String> audiences() {
    return audiences;
  }

  /**
   * Where the signing keys are fetched from.
   *
   * @return the JWKS URI
   */
  public URI jwksUri() {
    return jwksUri;
  }

  /**
   * Verifies a token and returns its claims.
   *
   * @param token the access token, without {@code Bearer }
   * @return the claims
   * @throws TokenVerificationException when the token is not one to accept
   */
  public AccessTokenClaims verify(String token) {
    if (token.length() > MAX_TOKEN_LENGTH) {
      throw malformed();
    }
    String[] parts = token.split("\\.", -1);
    if (parts.length != 3) {
      throw malformed();
    }
    JsonNode header = json(parts[0]);
    if (!"ES256".equals(header.path("alg").asText(null))) {
      throw new TokenVerificationException(
          Reason.UNSUPPORTED_ALGORITHM, "Only ES256 tokens are accepted.");
    }
    if (header.has("crit")) {
      throw new TokenVerificationException(
          Reason.MALFORMED_TOKEN,
          "The token has critical header parameters this verifier doesn't understand.");
    }
    JsonNode kid = header.get("kid");
    if (kid == null || !kid.isTextual() || kid.asText().isEmpty()) {
      throw new TokenVerificationException(
          Reason.UNKNOWN_KEY, "The token names no signing key (kid).");
    }
    PublicKey key = key(kid.asText());
    byte[] signature = decode(parts[2]);
    if (signature.length != 64 || !signatureValid(key, parts[0] + "." + parts[1], signature)) {
      throw new TokenVerificationException(
          Reason.INVALID_SIGNATURE, "The token's signature is not valid.");
    }

    JsonNode payload = json(parts[1]);
    JsonNode exp = payload.get("exp");
    if (exp == null || !exp.isNumber()) {
      throw new TokenVerificationException(
          Reason.MALFORMED_TOKEN, "The token has no expiry (exp).");
    }
    Instant now = clock.instant();
    if (!now.minus(clockSkew).isBefore(Instant.ofEpochSecond(exp.asLong()))) {
      throw new TokenVerificationException(Reason.TOKEN_EXPIRED, "The token has expired.");
    }
    JsonNode nbf = payload.get("nbf");
    if (nbf != null
        && nbf.isNumber()
        && now.plus(clockSkew).isBefore(Instant.ofEpochSecond(nbf.asLong()))) {
      throw new TokenVerificationException(
          Reason.TOKEN_NOT_YET_VALID, "The token is not valid yet (nbf).");
    }
    JsonNode iat = payload.get("iat");
    if (iat != null
        && iat.isNumber()
        && now.plus(clockSkew).isBefore(Instant.ofEpochSecond(iat.asLong()))) {
      throw new TokenVerificationException(
          Reason.TOKEN_NOT_YET_VALID, "The token was issued in the future (iat).");
    }
    if (!issuer.equals(payload.path("iss").asText(null))) {
      throw new TokenVerificationException(
          Reason.INVALID_ISSUER, "The token was issued by another issuer.");
    }
    if (!audienceMatches(payload.get("aud"))) {
      throw new TokenVerificationException(
          Reason.INVALID_AUDIENCE, "The token was not issued for this audience.");
    }
    if (!payload.path("sub").isTextual()) {
      throw new TokenVerificationException(
          Reason.MALFORMED_TOKEN, "The token has no subject (sub).");
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> claims = Json.mapper().convertValue(payload, LinkedHashMap.class);
    return new AccessTokenClaims(claims);
  }

  @Override
  public String toString() {
    return "TokenVerifier[issuer="
        + issuer
        + ", audiences="
        + audiences
        + ", jwksUri="
        + jwksUri
        + "]";
  }

  private boolean audienceMatches(@Nullable JsonNode aud) {
    if (aud == null) {
      return false;
    }
    if (aud.isTextual()) {
      return audiences.contains(aud.asText());
    }
    if (aud.isArray()) {
      for (JsonNode one : aud) {
        if (one.isTextual() && audiences.contains(one.asText())) {
          return true;
        }
      }
    }
    return false;
  }

  private PublicKey key(String kid) {
    Instant fetched = fetchedAt;
    if ((fetched == null || clock.instant().isAfter(fetched.plus(CACHE_TTL))) && !coolingDown()) {
      refresh(true);
    }
    if (fetchedAt == null) {
      throw unavailable(null); // never fetched, and the last attempt failed moments ago
    }
    PublicKey key = keys.get(kid);
    if (key == null && !coolingDown()) {
      // A key rotated in since the last fetch: fetch again, at most once per cooldown.
      refresh(false);
      key = keys.get(kid);
    }
    if (key == null) {
      throw new TokenVerificationException(
          Reason.UNKNOWN_KEY, "The token's signing key is not among the issuer's keys.");
    }
    return key;
  }

  /** Whether the keys were last fetched, or a fetch tried, less than the cooldown ago. */
  private boolean coolingDown() {
    Instant attempted = lastAttempt;
    return attempted != null && clock.instant().isBefore(attempted.plus(REFRESH_COOLDOWN));
  }

  /**
   * Fetches the keys; concurrent callers share one attempt. When the keys are merely stale, a
   * failed fetch keeps them.
   */
  private void refresh(boolean expired) {
    Instant attemptedBefore = lastAttempt;
    lock.lock();
    try {
      if (!Objects.equals(lastAttempt, attemptedBefore)) {
        return; // another thread tried while this one waited
      }
      try {
        load();
      } catch (TokenVerificationException e) {
        if (keys.isEmpty() || !expired) {
          throw e;
        }
        // Keep verifying with the keys already held until the issuer answers again.
      }
    } finally {
      lock.unlock();
    }
  }

  private void load() {
    lastAttempt = clock.instant();
    JsonNode document;
    try {
      HttpRequest request =
          HttpRequest.newBuilder(jwksUri)
              .timeout(JWKS_TIMEOUT)
              .header("Accept", "application/json")
              .GET()
              .build();
      HttpResponse<byte[]> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
      if (response.statusCode() != 200) {
        throw new IOException("HTTP " + response.statusCode());
      }
      document = Json.mapper().readTree(response.body());
    } catch (IOException e) {
      throw unavailable(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw unavailable(e);
    }
    Map<String, PublicKey> loaded = new HashMap<>();
    JsonNode list = document == null ? null : document.get("keys");
    if (list != null && list.isArray()) {
      for (JsonNode jwk : list) {
        String kid = jwk.path("kid").asText("");
        if (!"EC".equals(jwk.path("kty").asText())
            || !"P-256".equals(jwk.path("crv").asText())
            || kid.isEmpty()
            || (jwk.has("use") && !"sig".equals(jwk.path("use").asText()))
            || (jwk.has("alg") && !"ES256".equals(jwk.path("alg").asText()))) {
          continue;
        }
        try {
          loaded.put(
              kid,
              ecKey(
                  decodeKeyPart(jwk.path("x").asText("")),
                  decodeKeyPart(jwk.path("y").asText(""))));
        } catch (GeneralSecurityException | IllegalArgumentException e) {
          // A malformed key is skipped; the others still verify.
        }
      }
    }
    keys = Map.copyOf(loaded);
    fetchedAt = clock.instant();
  }

  private static TokenVerificationException unavailable(@Nullable Exception cause) {
    return new TokenVerificationException(
        Reason.JWKS_UNAVAILABLE, "The issuer's signing keys could not be fetched.", cause);
  }

  private static PublicKey ecKey(byte[] x, byte[] y) throws GeneralSecurityException {
    if (x.length != 32 || y.length != 32) {
      throw new GeneralSecurityException("not a P-256 point");
    }
    AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
    parameters.init(new ECGenParameterSpec("secp256r1"));
    ECParameterSpec spec = parameters.getParameterSpec(ECParameterSpec.class);
    ECPoint point = new ECPoint(new BigInteger(1, x), new BigInteger(1, y));
    return KeyFactory.getInstance("EC").generatePublic(new ECPublicKeySpec(point, spec));
  }

  private static boolean signatureValid(PublicKey key, String signingInput, byte[] signature) {
    try {
      // JOSE signatures are R || S, which this JDK algorithm takes as is (RFC 7518 section 3.4).
      Signature verifier = Signature.getInstance("SHA256withECDSAinP1363Format");
      verifier.initVerify(key);
      verifier.update(signingInput.getBytes(StandardCharsets.US_ASCII));
      return verifier.verify(signature);
    } catch (GeneralSecurityException e) {
      return false;
    }
  }

  private static byte[] decodeKeyPart(String part) {
    if (!BASE64URL.matcher(part).matches()) {
      throw new IllegalArgumentException("not base64url");
    }
    return Base64.getUrlDecoder().decode(part);
  }

  private static byte[] decode(String segment) {
    if (!BASE64URL.matcher(segment).matches()) {
      throw malformed();
    }
    try {
      return Base64.getUrlDecoder().decode(segment);
    } catch (IllegalArgumentException e) {
      throw malformed();
    }
  }

  private static JsonNode json(String segment) {
    try {
      JsonNode node = Json.mapper().readTree(decode(segment));
      if (node != null && node.isObject()) {
        return node;
      }
    } catch (IOException e) {
      // reported below
    }
    throw malformed();
  }

  private static TokenVerificationException malformed() {
    return new TokenVerificationException(Reason.MALFORMED_TOKEN, "The token is not a JWT.");
  }

  /** Builds a {@link TokenVerifier}. */
  public static final class Builder {
    private @Nullable String issuer;
    private final List<String> audiences = new ArrayList<>();
    private @Nullable URI jwksUri;
    private Duration clockSkew = DEFAULT_CLOCK_SKEW;
    private @Nullable HttpClient httpClient;
    private Clock clock = Clock.systemUTC();

    private Builder() {}

    /**
     * The issuer tokens must carry in {@code iss}: the discovery document's {@code issuer}.
     * Required.
     *
     * @param issuer the issuer
     * @return this builder
     */
    public Builder issuer(String issuer) {
      Objects.requireNonNull(issuer, "issuer");
      if (issuer.isBlank()) {
        throw new IllegalArgumentException("issuer must not be blank.");
      }
      this.issuer = issuer;
      return this;
    }

    /**
     * An audience a token may be issued for: the platform audience, or your service's RFC 8707
     * resource identifier. At least one is required: without it a token issued for another service
     * would pass. Call it again to accept several.
     *
     * @param audience the audience
     * @return this builder
     */
    public Builder audience(String audience) {
      Objects.requireNonNull(audience, "audience");
      if (audience.isBlank()) {
        throw new IllegalArgumentException("audience must not be blank.");
      }
      audiences.add(audience);
      return this;
    }

    /**
     * Where the signing keys are published. Required unless given by {@link
     * #discovery(DiscoveryDocument)}. Must use https, except for a loopback host.
     *
     * @param jwksUri the JWKS URI
     * @return this builder
     */
    public Builder jwksUri(String jwksUri) {
      this.jwksUri = checkUri(jwksUri);
      return this;
    }

    /**
     * Takes the issuer and the JWKS URI from the discovery document.
     *
     * @param discovery the document, from {@link Discovery#fetch(String)}
     * @return this builder
     */
    public Builder discovery(DiscoveryDocument discovery) {
      issuer(discovery.issuer());
      return jwksUri(discovery.jwksUri());
    }

    /**
     * The leeway for clock differences when checking {@code exp}, {@code nbf} and {@code iat}.
     * Default 30 seconds.
     *
     * @param clockSkew zero or more
     * @return this builder
     */
    public Builder clockSkew(Duration clockSkew) {
      Objects.requireNonNull(clockSkew, "clockSkew");
      if (clockSkew.isNegative()) {
        throw new IllegalArgumentException("clockSkew must not be negative.");
      }
      this.clockSkew = clockSkew;
      return this;
    }

    /**
     * The HTTP client that fetches the keys.
     *
     * @param httpClient the client
     * @return this builder
     */
    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
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
     * Builds the verifier. Makes no network call: the keys are fetched on first use.
     *
     * @return the verifier
     * @throws IllegalStateException when the issuer, an audience or the JWKS URI is missing
     */
    public TokenVerifier build() {
      String iss = issuer;
      URI uri = jwksUri;
      if (iss == null) {
        throw new IllegalStateException("A token verifier needs the issuer.");
      }
      if (audiences.isEmpty()) {
        throw new IllegalStateException(
            "A token verifier needs an audience: without it a token issued for another service would pass.");
      }
      if (uri == null) {
        throw new IllegalStateException(
            "A token verifier needs the JWKS URI, or a discovery document.");
      }
      return new TokenVerifier(this, iss, uri);
    }

    static URI checkUri(String value) {
      URI uri;
      try {
        uri = URI.create(value.trim());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Not a URL: " + value, e);
      }
      String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
      String host = uri.getHost() == null ? "" : uri.getHost().toLowerCase(Locale.ROOT);
      boolean loopback =
          host.equals("localhost")
              || host.endsWith(".localhost")
              || host.startsWith("127.")
              || host.equals("[::1]");
      if (!(scheme.equals("https") || (scheme.equals("http") && loopback)) || host.isEmpty()) {
        throw new IllegalArgumentException(
            "The URL must use https (plain http is allowed only for localhost): " + value);
      }
      return uri;
    }
  }
}
