package com.qeetgroup.qeetid.exception;

import org.jspecify.annotations.Nullable;

/**
 * The request got no response: DNS, connection, TLS or a reset. Code {@code network_error}, or
 * {@code interrupted} when the calling thread was interrupted (its interrupt flag is set again).
 */
public class NetworkException extends QeetIDException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception.
   *
   * @param message what failed
   * @param cause the underlying failure, or null
   */
  public NetworkException(String message, @Nullable Throwable cause) {
    this(message, "network_error", cause);
  }

  /**
   * Creates the exception with a code of its own.
   *
   * @param message what failed
   * @param code the stable code
   * @param cause the underlying failure, or null
   */
  protected NetworkException(String message, String code, @Nullable Throwable cause) {
    super(message, code, cause);
  }

  /**
   * The exception for a thread interrupted while it waited for the API. Sets the interrupt flag
   * again.
   *
   * @param cause the {@link InterruptedException}
   * @return the exception, code {@code interrupted}
   */
  public static NetworkException interrupted(InterruptedException cause) {
    Thread.currentThread().interrupt();
    return new NetworkException("The call was interrupted.", "interrupted", cause);
  }
}
