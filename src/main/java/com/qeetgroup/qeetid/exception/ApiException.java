package com.qeetgroup.qeetid.exception;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * The API answered with an error. A subclass names the status; this class itself is thrown for a
 * status without one (such as 405 or 413) and for a successful answer whose body could not be read
 * ({@code invalid_response}). Quote {@link #requestId()} when you contact support.
 */
public class ApiException extends QeetIDException {
  private static final long serialVersionUID = 1L;

  private final transient ApiError error;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public ApiException(ApiError error) {
    super(error.message(), error.code());
    this.error = error;
  }

  /**
   * The whole error answer.
   *
   * @return the answer
   */
  public ApiError apiError() {
    return error;
  }

  /**
   * The HTTP status code.
   *
   * @return the status
   */
  public int status() {
    return error.status();
  }

  /**
   * The request's id in Qeet ID's logs, when the server sent one. Quote it to support.
   *
   * @return the request id, or null
   */
  public @Nullable String requestId() {
    return error.requestId();
  }

  /**
   * Developer-facing context, when the server gave any. Never localised.
   *
   * @return the detail, or null
   */
  public @Nullable String detail() {
    return error.detail();
  }

  /**
   * The refused fields, on a {@code 422 validation_failed}. Empty otherwise.
   *
   * @return the fields
   */
  public List<FieldError> fields() {
    return error.fields();
  }

  /**
   * Structured, error-specific context, such as {@code {"retry_after": 2}}.
   *
   * @return the metadata, empty when there is none
   */
  public Map<String, Object> metadata() {
    return error.metadata();
  }

  /**
   * The server's hint that the action may succeed if tried again, possibly after the person signs
   * in again. It is advice: the SDK retries only by its own rules.
   *
   * @return whether the server marked the error retryable
   */
  public boolean retryable() {
    return error.retryable();
  }

  /**
   * How long the server asked the caller to wait before trying again, from {@code Retry-After}; on
   * 429 and 503.
   *
   * @return the wait, or null when the server didn't say
   */
  public @Nullable Duration retryAfter() {
    return error.retryAfter();
  }

  /**
   * The response headers, names in lower case.
   *
   * @return the headers
   */
  public Map<String, List<String>> headers() {
    return error.headers();
  }

  @Override
  public String getMessage() {
    StringBuilder text = new StringBuilder().append(status()).append(' ').append(code());
    String message = super.getMessage();
    if (message != null && !message.isEmpty()) {
      text.append(": ").append(message);
    }
    if (requestId() != null) {
      text.append(" (request ").append(requestId()).append(')');
    }
    return text.toString();
  }
}
