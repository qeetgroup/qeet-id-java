package com.qeetgroup.qeetid.internal;

import com.fasterxml.jackson.databind.JavaType;
import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.Params;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.exception.ApiError;
import com.qeetgroup.qeetid.exception.ApiException;
import com.qeetgroup.qeetid.exception.NetworkException;
import com.qeetgroup.qeetid.exception.QeetIDException;
import com.qeetgroup.qeetid.exception.TimeoutException;
import java.io.UncheckedIOException;
import java.lang.System.Logger.Level;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.jspecify.annotations.Nullable;

/**
 * Sends requests to the API: credentials, serialisation, typed errors, timeouts, and retries with
 * backoff for the requests that are safe to repeat. One per client; safe for concurrent use.
 */
public final class Transport {
  /** Methods safe to repeat. PUT and PATCH are not: replaying a write is the caller's decision. */
  private static final Set<String> SAFE_METHODS = Set.of("GET", "DELETE");

  private final Config config;
  private final @Nullable Credential credential;

  /** Creates the transport. Makes no network call. */
  public Transport(Config config) {
    this.config = config;
    String apiKey = config.apiKey();
    String clientId = config.clientId();
    String clientSecret = config.clientSecret();
    if (apiKey != null) {
      this.credential = new Credential.ApiKey(apiKey);
    } else if (clientId != null && clientSecret != null) {
      this.credential =
          new Credential.ClientCredentials(clientId, clientSecret, this, Clock.systemUTC());
    } else {
      this.credential = null;
    }
  }

  /** The base URL requests go to, without a trailing slash. */
  public String baseUrl() {
    return config.baseUrl();
  }

  /**
   * How a client is configured. Its {@code toString} never shows a credential.
   *
   * @param baseUrl the API's base URL, without a trailing slash
   * @param apiKey the API key, or null
   * @param clientId a service principal's client ID, or null
   * @param clientSecret its secret, or null
   * @param timeout how long each attempt may take
   * @param maxRetries how many times a retry-safe request is retried
   * @param initialRetryDelay the first backoff delay
   * @param maxRetryDelay the longest backoff delay
   * @param headers headers sent with every request
   * @param userAgent the User-Agent
   * @param logger receives diagnostics; never credentials, query strings or bodies
   * @param httpClient sends the requests
   */
  public record Config(
      String baseUrl,
      @Nullable String apiKey,
      @Nullable String clientId,
      @Nullable String clientSecret,
      Duration timeout,
      int maxRetries,
      Duration initialRetryDelay,
      Duration maxRetryDelay,
      Map<String, String> headers,
      String userAgent,
      System.Logger logger,
      HttpClient httpClient) {

    @Override
    public String toString() {
      return "Config[baseUrl="
          + baseUrl
          + ", timeout="
          + timeout
          + ", maxRetries="
          + maxRetries
          + "]";
    }
  }

  // Resource helpers. Each takes the already encoded path (see Paths).

  /** A GET answered with a {@code type}. */
  public <T> T get(String path, @Nullable Params query, Class<T> type, RequestOptions options) {
    return execute(ApiRequest.get(path).query(query), options, type);
  }

  /** A request with a JSON body (or none), answered with a {@code type}. */
  public <T> T send(
      String method, String path, @Nullable Object body, Class<T> type, RequestOptions options) {
    return execute(ApiRequest.of(method, path).json(body), options, type);
  }

  /** A request with a JSON body (or none) whose answer has nothing to read. */
  public void sendVoid(String method, String path, @Nullable Object body, RequestOptions options) {
    executeVoid(ApiRequest.of(method, path).json(body), options);
  }

  /** A DELETE whose answer has nothing to read. */
  public void delete(String path, RequestOptions options) {
    executeVoid(ApiRequest.of("DELETE", path), options);
  }

  /** A GET of a whole list, {@code {"items": [...], "truncated": ...}}. */
  public <T> ListResult<T> list(
      String path, @Nullable Params query, Class<T> item, RequestOptions options) {
    return list(ApiRequest.get(path).query(query), item, options);
  }

  /** A whole list, {@code {"items": [...], "truncated": ...}}, from any request. */
  public <T> ListResult<T> list(ApiRequest request, Class<T> item, RequestOptions options) {
    Envelopes.ListEnvelope<T> envelope =
        read(request, options, Json.type(Envelopes.ListEnvelope.class, item));
    return new ListResult<>(envelope.items(), envelope.truncated());
  }

  /** The first page of a cursor-paginated GET; later pages are fetched on demand. */
  public <T> Page<T> page(
      String path, @Nullable Params query, Class<T> item, RequestOptions options) {
    return page(ApiRequest.get(path).query(query), item, options);
  }

  private <T> Page<T> page(ApiRequest request, Class<T> item, RequestOptions options) {
    Envelopes.PageEnvelope<T> envelope =
        read(request, options, Json.type(Envelopes.PageEnvelope.class, item));
    String next = envelope.nextCursor();
    return Page.of(
        envelope.items(),
        next == null || next.isEmpty() ? null : next,
        cursor -> page(request.copyWithQuery("cursor", cursor), item, options));
  }

  /** Sends {@code request} and reads the answer as {@code type}. */
  public <T> T execute(ApiRequest request, RequestOptions options, Class<T> type) {
    return read(request, options, Json.type(type));
  }

  /**
   * Sends {@code request} and reads the answer as {@code type}, a generic type built from the
   * caller's class (see {@link Json#type(Class, Class[])}).
   */
  @SuppressWarnings("TypeParameterUnusedInFormals") // like Jackson's readValue(JavaType)
  public <T> T read(ApiRequest request, RequestOptions options, JavaType type) {
    Response response = send(request, options);
    if (response.body().length == 0) {
      throw invalidResponse(response, "The API answered with an empty body.");
    }
    try {
      return Json.read(response.body(), type);
    } catch (UncheckedIOException e) {
      throw invalidResponse(response, "The API's answer could not be read.");
    }
  }

  /** Sends {@code request} and ignores the answer's body. */
  public void executeVoid(ApiRequest request, RequestOptions options) {
    send(request, options);
  }

  private Response send(ApiRequest request, RequestOptions options) {
    Map<String, String> headers = headers(request, options);
    boolean retrySafe =
        !request.neverRetry
            && (request.retrySafe
                || SAFE_METHODS.contains(request.method)
                || headers.containsKey("Idempotency-Key"));
    Integer callRetries = options.maxRetries();
    int maxRetries =
        request.neverRetry ? 0 : callRetries != null ? callRetries : config.maxRetries();
    Duration callTimeout = options.timeout();
    Duration timeout = callTimeout != null ? callTimeout : config.timeout();
    URI uri = uri(request);
    Payload payload = payload(request);
    String authorization = request.authenticate ? authorization() : null;

    for (int attempt = 0; ; attempt++) {
      long started = System.nanoTime();
      try {
        Response response = attempt(request, uri, headers, authorization, payload, timeout);
        log(request, attempt, response.status(), started, response.requestId());
        if (response.status() >= 200 && response.status() < 300) {
          return response;
        }
        ApiException failure =
            ErrorMapper.fromResponse(response.status(), response.headers(), response.body());
        if (failure.status() == 401 && authorization != null && credential != null) {
          credential.invalidate(authorization);
        }
        throw failure;
      } catch (QeetIDException failure) {
        Duration delay =
            attempt < maxRetries && RetryPolicy.isRetryable(failure, retrySafe)
                ? RetryPolicy.delay(
                    attempt,
                    failure,
                    config.initialRetryDelay(),
                    config.maxRetryDelay(),
                    () -> ThreadLocalRandom.current().nextDouble())
                : null;
        if (delay == null) {
          throw failure;
        }
        logRetry(request, attempt, delay, failure);
        sleep(delay);
      }
    }
  }

  private Response attempt(
      ApiRequest request,
      URI uri,
      Map<String, String> headers,
      @Nullable String authorization,
      @Nullable Payload payload,
      Duration timeout) {
    HttpRequest.Builder builder =
        HttpRequest.newBuilder(uri)
            .timeout(timeout)
            .method(
                request.method,
                payload == null
                    ? HttpRequest.BodyPublishers.noBody()
                    : HttpRequest.BodyPublishers.ofByteArray(payload.bytes()));
    headers.forEach(builder::header);
    if (payload != null) {
      builder.header("Content-Type", payload.contentType());
    }
    if (authorization != null) {
      builder.header("Authorization", authorization);
    }
    CompletableFuture<HttpResponse<byte[]>> future =
        config.httpClient().sendAsync(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
    try {
      // The whole attempt, body included, within the timeout.
      HttpResponse<byte[]> response = future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
      return new Response(response.statusCode(), response.headers().map(), response.body());
    } catch (java.util.concurrent.TimeoutException e) {
      future.cancel(true);
      throw new TimeoutException(
          "The request got no complete response within " + timeout.toMillis() + " ms.", e);
    } catch (InterruptedException e) {
      future.cancel(true);
      throw NetworkException.interrupted(e);
    } catch (ExecutionException e) {
      Throwable cause = e.getCause() == null ? e : e.getCause();
      if (cause instanceof HttpTimeoutException) {
        throw new TimeoutException(
            "The request got no response within " + timeout.toMillis() + " ms.", cause);
      }
      throw new NetworkException(
          "The request could not reach the Qeet ID API: " + describe(cause), cause);
    }
  }

  private String authorization() {
    if (credential == null) {
      throw new QeetIDException(
          "This call needs credentials: give the client an API key or client credentials.",
          "missing_credentials");
    }
    return credential.authorization();
  }

  private Map<String, String> headers(ApiRequest request, RequestOptions options) {
    Map<String, String> headers = new LinkedHashMap<>(config.headers());
    headers.putAll(options.headers());
    headers.put("Accept", "application/json");
    headers.put("User-Agent", config.userAgent());
    String key = options.idempotencyKey();
    if (request.idempotent) {
      headers.put("Idempotency-Key", key != null ? key : UUID.randomUUID().toString());
    } else if (key != null) {
      throw new IllegalArgumentException(
          "This call does not take an idempotency key: the API does not make it idempotent.");
    }
    return headers;
  }

  private URI uri(ApiRequest request) {
    StringBuilder url = new StringBuilder(config.baseUrl()).append(request.path);
    if (!request.query.isEmpty()) {
      StringJoiner query = new StringJoiner("&", "?", "");
      request.query.forEach(
          entry -> query.add(encode(entry.getKey()) + "=" + encode(entry.getValue())));
      url.append(query);
    }
    return URI.create(url.toString());
  }

  private static @Nullable Payload payload(ApiRequest request) {
    byte[] raw = request.raw;
    String contentType = request.contentType;
    if (raw != null && contentType != null) {
      return new Payload(raw, contentType);
    }
    Map<String, String> form = request.form;
    if (form != null) {
      StringJoiner body = new StringJoiner("&");
      form.forEach((name, value) -> body.add(encode(name) + "=" + encode(value)));
      return new Payload(
          body.toString().getBytes(StandardCharsets.UTF_8), "application/x-www-form-urlencoded");
    }
    Object json = request.json;
    return json == null ? null : new Payload(Json.write(json), "application/json");
  }

  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  private static ApiException invalidResponse(Response response, String message) {
    ApiError error =
        new ApiError(
            response.status(),
            "invalid_response",
            message,
            response.requestId(),
            null,
            List.of(),
            Map.of(),
            false,
            null,
            response.headers());
    return new ApiException(error);
  }

  private static void sleep(Duration delay) {
    try {
      Thread.sleep(delay.toMillis());
    } catch (InterruptedException e) {
      throw NetworkException.interrupted(e);
    }
  }

  private static String describe(Throwable cause) {
    String message = cause.getMessage();
    return message == null || message.isBlank()
        ? cause.getClass().getSimpleName()
        : cause.getClass().getSimpleName() + ": " + message;
  }

  // What the logger may see: the method, the path without its query, the attempt, the status, the
  // time taken and the request id. Never credentials, query strings, headers or bodies.

  private void log(
      ApiRequest request, int attempt, int status, long startedNanos, @Nullable String requestId) {
    System.Logger logger = config.logger();
    if (logger.isLoggable(Level.DEBUG)) {
      long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedNanos);
      logger.log(
          Level.DEBUG,
          "qeet-id: {0} {1} attempt {2} answered {3} in {4} ms (request {5})",
          request.method,
          request.path,
          attempt + 1,
          status,
          millis,
          requestId == null ? "-" : requestId);
    }
  }

  private void logRetry(ApiRequest request, int attempt, Duration delay, QeetIDException failure) {
    System.Logger logger = config.logger();
    if (logger.isLoggable(Level.DEBUG)) {
      logger.log(
          Level.DEBUG,
          "qeet-id: retrying {0} {1} after attempt {2} failed ({3}); waiting {4} ms",
          request.method,
          request.path,
          attempt + 1,
          failure.code(),
          delay.toMillis());
    }
  }

  @SuppressWarnings("ArrayRecordComponent") // private, never compared or hashed
  private record Payload(byte[] bytes, String contentType) {}

  @SuppressWarnings("ArrayRecordComponent") // private, never compared or hashed
  private record Response(int status, Map<String, List<String>> headers, byte[] body) {
    @Nullable String requestId() {
      for (Map.Entry<String, List<String>> header : headers.entrySet()) {
        if (header.getKey().equalsIgnoreCase("x-request-id") && !header.getValue().isEmpty()) {
          return header.getValue().get(0);
        }
      }
      return null;
    }
  }
}
