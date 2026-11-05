package com.qeetgroup.qeetid.exception;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * An error answer from the API: the HTTP status, the response headers, and the error envelope
 * {@code {"error": {"code", "message", "detail", "fields", "metadata", "retryable",
 * "request_id"}}}. {@link ApiException} and its subclasses carry one.
 *
 * @param status the HTTP status code
 * @param code the stable error code, such as {@code not_found}; {@code http_<status>} when the
 *     answer had no envelope
 * @param message a message safe to show a person
 * @param requestId the request's id in Qeet ID's logs, from {@code X-Request-Id}; quote it to
 *     support
 * @param detail developer-facing context, never localised
 * @param fields the refused fields, on a {@code 422 validation_failed}; empty otherwise
 * @param metadata structured, error-specific context, such as {@code {"remaining_attempts": 2}}
 * @param retryable the server's hint that the action may succeed if tried again, possibly after
 *     signing in again. Advice, not an instruction: the SDK retries only by its own rules
 * @param retryAfter how long the server asked the caller to wait, from {@code Retry-After}
 * @param headers the response headers, names in lower case
 */
public record ApiError(
    int status,
    String code,
    String message,
    @Nullable String requestId,
    @Nullable String detail,
    List<FieldError> fields,
    Map<String, Object> metadata,
    boolean retryable,
    @Nullable Duration retryAfter,
    Map<String, List<String>> headers) {

  /** Copies the collections, so an error can't change after it is made. */
  public ApiError {
    Objects.requireNonNull(code, "code");
    Objects.requireNonNull(message, "message");
    fields = List.copyOf(fields);
    metadata = java.util.Collections.unmodifiableMap(new java.util.LinkedHashMap<>(metadata));
    headers = Map.copyOf(headers);
  }

  /**
   * An error with only a status, a code and a message: for tests, and for errors the SDK makes.
   *
   * @param status the HTTP status code
   * @param code the stable error code
   * @param message the message
   * @return the error
   */
  public static ApiError of(int status, String code, String message) {
    return new ApiError(
        status, code, message, null, null, List.of(), Map.of(), false, null, Map.of());
  }
}
