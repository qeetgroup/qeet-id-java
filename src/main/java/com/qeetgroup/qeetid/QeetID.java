package com.qeetgroup.qeetid;

import com.qeetgroup.qeetid.internal.Headers;
import com.qeetgroup.qeetid.internal.Transport;
import com.qeetgroup.qeetid.internal.UserAgent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * The Qeet ID API client. Build one per process and share it: it is immutable, safe for concurrent
 * use, and reuses connections and cached tokens.
 *
 * <pre>{@code
 * QeetID qeetId = QeetID.builder().apiKey(System.getenv("QEET_ID_API_KEY")).build();
 * User user = qeetId.users().get(userId);
 * }</pre>
 *
 * <p>Each accessor groups the calls on one kind of resource. Every call is synchronous, throws a
 * {@link com.qeetgroup.qeetid.exception.QeetIDException} on failure, and has an overload taking
 * {@link RequestOptions} last.
 */
public final class QeetID {
  /** The production API. */
  public static final String DEFAULT_BASE_URL = "https://api.id.qeet.in";

  /** How long each attempt may take by default, response body included. */
  public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

  /** How many times a retry-safe request is retried by default. */
  public static final int DEFAULT_MAX_RETRIES = 2;

  private final Transport transport;
  private final String description;

  // RESOURCE FIELDS

  private QeetID(Builder builder, String baseUrl) {
    Map<String, String> headers = Map.copyOf(builder.headers);
    HttpClient httpClient = builder.httpClient;
    if (httpClient == null) {
      httpClient =
          HttpClient.newBuilder()
              .connectTimeout(Duration.ofSeconds(10))
              .followRedirects(HttpClient.Redirect.NEVER)
              .build();
    }
    this.transport =
        new Transport(
            new Transport.Config(
                baseUrl,
                builder.apiKey,
                builder.clientId,
                builder.clientSecret,
                builder.timeout,
                builder.maxRetries,
                builder.initialRetryDelay,
                builder.maxRetryDelay,
                headers,
                UserAgent.build(builder.appName, builder.appVersion),
                builder.logger,
                httpClient));
    String credential =
        builder.apiKey != null
            ? "apiKey=[redacted]"
            : builder.clientId != null
                ? "clientId=" + builder.clientId + ", clientSecret=[redacted]"
                : "no credentials";
    this.description = "QeetID[baseUrl=" + baseUrl + ", " + credential + "]";
    // RESOURCE INIT
  }

  /**
   * Starts building a client.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * The base URL requests go to.
   *
   * @return the base URL, without a trailing slash
   */
  public String baseUrl() {
    return transport.baseUrl();
  }

  // RESOURCE ACCESSORS

  /** The base URL and which credential the client uses; never the credential itself. */
  @Override
  public String toString() {
    return description;
  }

  /**
   * Builds a {@link QeetID} client. Give it one credential: an {@link #apiKey(String) API key}, or
   * a service principal's {@link #clientCredentials(String, String) client credentials}. A client
   * without one can still call the endpoints that authenticate otherwise: the OAuth endpoints,
   * directory sign-in, agent tokens and credential verification.
   */
  public static final class Builder {
    private String baseUrl = DEFAULT_BASE_URL;
    private @Nullable String apiKey;
    private @Nullable String clientId;
    private @Nullable String clientSecret;
    private Duration timeout = DEFAULT_TIMEOUT;
    private int maxRetries = DEFAULT_MAX_RETRIES;
    private Duration initialRetryDelay = Duration.ofMillis(500);
    private Duration maxRetryDelay = Duration.ofSeconds(8);
    private final Map<String, String> headers = new LinkedHashMap<>();
    private @Nullable String appName;
    private @Nullable String appVersion;
    private System.Logger logger = System.getLogger("com.qeetgroup.qeetid");
    private @Nullable HttpClient httpClient;

    private Builder() {}

    /**
     * Authenticates every call with a server-side API key ({@code qk_…}), sent as {@code
     * Authorization: ApiKey <key>}. Keep it out of browsers, source control and logs.
     *
     * @param apiKey the key
     * @return this builder
     */
    public Builder apiKey(String apiKey) {
      Objects.requireNonNull(apiKey, "apiKey");
      if (apiKey.isBlank()) {
        throw new IllegalArgumentException("apiKey must not be blank.");
      }
      this.apiKey = apiKey.trim();
      return this;
    }

    /**
     * Authenticates every call with an access token the client gets for a service principal with
     * the OAuth {@code client_credentials} grant ({@code POST /v1/oauth/token}). The token is
     * cached and replaced shortly before it expires. It carries every scope the principal holds.
     *
     * @param clientId the principal's client ID
     * @param clientSecret its secret
     * @return this builder
     */
    public Builder clientCredentials(String clientId, String clientSecret) {
      Objects.requireNonNull(clientId, "clientId");
      Objects.requireNonNull(clientSecret, "clientSecret");
      if (clientId.isBlank() || clientSecret.isBlank()) {
        throw new IllegalArgumentException("clientId and clientSecret must not be blank.");
      }
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      return this;
    }

    /**
     * The API's base URL. Default {@value QeetID#DEFAULT_BASE_URL}. It must use https, except for a
     * loopback host ({@code localhost}, {@code *.localhost}, {@code 127.0.0.1}, {@code [::1]}): a
     * credential must never cross a network in the clear.
     *
     * @param baseUrl an absolute URL without a query or fragment
     * @return this builder
     */
    public Builder baseUrl(String baseUrl) {
      this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
      return this;
    }

    /**
     * How long each attempt may take, response body included. Default 30 seconds.
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
     * How many times a retry-safe request is retried after a transient failure. Default 2; 0 turns
     * retries off.
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
     * The bounds of the exponential backoff between retries, when the server sends no {@code
     * Retry-After}. Default 500 ms doubling to 8 s; each wait is between half and all of it.
     *
     * @param initial the first wait, more than zero
     * @param max the longest wait, at least {@code initial}
     * @return this builder
     */
    public Builder retryBackoff(Duration initial, Duration max) {
      Objects.requireNonNull(initial, "initial");
      Objects.requireNonNull(max, "max");
      if (initial.isZero() || initial.isNegative() || max.compareTo(initial) < 0) {
        throw new IllegalArgumentException("retryBackoff needs 0 < initial <= max.");
      }
      this.initialRetryDelay = initial;
      this.maxRetryDelay = max;
      return this;
    }

    /**
     * Adds a header to every request, such as a tracing header. It can't replace {@code
     * Authorization}, {@code Accept}, {@code Content-Type}, {@code User-Agent} or {@code
     * Idempotency-Key}.
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
     * Names your application at the front of the {@code User-Agent}, such as {@code
     * brightleaf-onboarding/1.4.0}.
     *
     * @param name a short token: letters, digits and {@code . _ - @ / +}
     * @param version its version, or null
     * @return this builder
     */
    public Builder appInfo(String name, @Nullable String version) {
      this.appName = UserAgent.checkToken(name, "The application name");
      this.appVersion = version == null ? null : UserAgent.checkToken(version, "The version");
      return this;
    }

    /**
     * Receives the SDK's diagnostics, at {@code DEBUG}: each attempt's method, path, status,
     * duration and request id, and each retry. Never credentials, query strings, headers or bodies.
     * Default {@code System.getLogger("com.qeetgroup.qeetid")}, which SLF4J and Log4j can take over
     * through their {@code System.Logger} bridges.
     *
     * @param logger the logger
     * @return this builder
     */
    public Builder logger(System.Logger logger) {
      this.logger = Objects.requireNonNull(logger, "logger");
      return this;
    }

    /**
     * The HTTP client that sends the requests: for a proxy, custom TLS or an executor of your own.
     * By default one with a 10-second connect timeout that doesn't follow redirects.
     *
     * @param httpClient the client
     * @return this builder
     */
    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
      return this;
    }

    /**
     * Builds the client. Makes no network call.
     *
     * @return the client
     * @throws IllegalStateException when both an API key and client credentials were given
     * @throws IllegalArgumentException when the base URL is not allowed
     */
    public QeetID build() {
      if (apiKey != null && clientId != null) {
        throw new IllegalStateException("Give one credential: an API key or client credentials.");
      }
      return new QeetID(this, normalizeBaseUrl(baseUrl));
    }
  }

  /**
   * Checks and normalises a base URL: absolute, https unless the host is this machine, without user
   * info, query or fragment, and without a trailing slash.
   *
   * @param baseUrl the URL
   * @return the normalised URL
   * @throws IllegalArgumentException when it is not allowed
   */
  static String normalizeBaseUrl(String baseUrl) {
    URI uri;
    try {
      uri = new URI(baseUrl.trim());
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(
          "baseUrl must be an absolute URL, such as " + DEFAULT_BASE_URL + ".", e);
    }
    String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
    String host = uri.getHost();
    if (host == null || !(scheme.equals("https") || scheme.equals("http"))) {
      throw new IllegalArgumentException(
          "baseUrl must be an absolute http(s) URL, such as " + DEFAULT_BASE_URL + ".");
    }
    if (scheme.equals("http") && !isLoopback(host)) {
      throw new IllegalArgumentException(
          "baseUrl must use https (plain http is allowed only for localhost).");
    }
    if (uri.getRawUserInfo() != null || uri.getRawQuery() != null || uri.getRawFragment() != null) {
      throw new IllegalArgumentException(
          "baseUrl must not carry credentials, a query or a fragment.");
    }
    String text = uri.toString();
    while (text.endsWith("/")) {
      text = text.substring(0, text.length() - 1);
    }
    return text;
  }

  private static boolean isLoopback(String host) {
    String h = host.toLowerCase(Locale.ROOT);
    return h.equals("localhost")
        || h.endsWith(".localhost")
        || h.equals("[::1]")
        || h.equals("::1")
        || h.startsWith("127.");
  }
}
