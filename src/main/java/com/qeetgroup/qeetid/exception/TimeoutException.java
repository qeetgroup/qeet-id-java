package com.qeetgroup.qeetid.exception;

import org.jspecify.annotations.Nullable;

/** The request got no complete response within the timeout. Code {@code timeout}. */
public class TimeoutException extends NetworkException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception.
   *
   * @param message what timed out
   * @param cause the underlying failure, or null
   */
  public TimeoutException(String message, @Nullable Throwable cause) {
    super(message, "timeout", cause);
  }
}
