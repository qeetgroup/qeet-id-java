package com.qeetgroup.qeetid;

import com.qeetgroup.qeetid.internal.Headers;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Settings for one call, overriding the client's. Every method has an overload that takes them
 * last.
 *
 * <pre>{@code
 * qeetId.users().get(userId, RequestOptions.builder().timeout(Duration.ofSeconds(5)).maxRetries(0).build());
 * }</pre>
 */
public final class RequestOptions {
  private static final RequestOptions NONE = new RequestOptions(new Builder());

  private final @Nullable Duration timeout;
  private final @Nullable Integer maxRetries;
  private final Map<String, String> headers;
  private final @Nullable String idempotencyKey;

  private RequestOptions(Builder builder) {
    this.timeout = builder.timeout;
    this.maxRetries = builder.maxRetries;
    this.headers = Map.copyOf(builder.headers);
    this.idempotencyKey = builder.idempotencyKey;
  }

  /**
   * No overrides: the client's settings.
   *
   * @return the empty options
   */
  public static RequestOptions none() {
    return NONE;
  }

  /**
   * Starts building options.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * How long each attempt may take, response body included; null for the client's.
   *
   * @return the timeout, or null
   */
  public @Nullable Duration timeout() {
    return timeout;
  }

  /**
   * How many times a retry-safe call is retried; null for the client's.
   *
   * @return the retries, or null
   */
  public @Nullable Integer maxRetries() {
    return maxRetries;
  }

  /**
   * Extra headers for this call.
   *
   * @return the headers
   */
  public Map<String, String> headers() {
    return headers;
  }

  /**
   * The idempotency key, for the create calls the API makes idempotent; null to have one generated.
   *
   * @return the key, or null
   */
  public @Nullable String idempotencyKey() {
    return idempotencyKey;
  }

  @Override
  public String toString() {
    return "RequestOptions[timeout="
        + timeout
        + ", maxRetries="
        + maxRetries
        + ", headers="
        + headers.keySet()
        + ", idempotencyKey="
        + idempotencyKey
        + "]";
  }

  /** Builds {@link RequestOptions}. */
  public static final class Builder {
    private @Nullable Duration timeout;
    private @Nullable Integer maxRetries;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private @Nullable String idempotencyKey;

    private Builder() {}

    /**
     * How long each attempt may take, response body included.
     *
     * @param timeout more than zero
     * @return this builder
     */
    public Builder timeout(Duration timeout) {
      Objects.requireNonNull(timeout, "timeout");
      if (timeout.isZero() || timeout.isNegative()) {
        throw new IllegalArgumentException("timeout must be greater than zero.");
      }
      this.timeout = timeout;
      return this;
    }

    /**
     * How many times a retry-safe call is retried after a transient failure; 0 turns retries off.
     *
     * @param maxRetries 0 or more
     * @return this builder
     */
    public Builder maxRetries(int maxRetries) {
      if (maxRetries < 0) {
        throw new IllegalArgumentException("maxRetries must be 0 or more.");
      }
      this.maxRetries = maxRetries;
      return this;
    }

    /**
     * Adds a header, such as {@code traceparent}. It can't replace {@code Authorization}, {@code
     * Accept}, {@code Content-Type}, {@code User-Agent} or {@code Idempotency-Key}.
     *
     * @param name the header name
     * @param value its value
     * @return this builder
     */
    public Builder header(String name, String value) {
      Headers.check(name, value);
      headers.put(name.trim(), value);
      return this;
    }

    /**
     * Makes a create call safe to repeat: the API answers a call repeated with the same key and
     * body with the first call's answer, for 24 hours, instead of acting twice. Only the calls the
     * API makes idempotent take one; the SDK generates one for them when you give none. Give your
     * own to make retries across processes safe too.
     *
     * @param idempotencyKey 1 to 255 printable ASCII characters; a UUID is a good choice
     * @return this builder
     */
    public Builder idempotencyKey(String idempotencyKey) {
      this.idempotencyKey = Headers.checkIdempotencyKey(idempotencyKey);
      return this;
    }

    /**
     * Builds the options.
     *
     * @return the options
     */
    public RequestOptions build() {
      return new RequestOptions(this);
    }
  }
}
