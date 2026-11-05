package com.qeetgroup.qeetid.exception;

import org.jspecify.annotations.Nullable;

/**
 * The base of every exception this SDK throws on purpose. {@link #code()} is a stable,
 * machine-readable code: branch on it, never on the message.
 */
public class QeetIDException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private final String code;

  /**
   * Creates an exception.
   *
   * @param message what went wrong, for a person
   * @param code the stable code
   */
  public QeetIDException(String message, String code) {
    this(message, code, null);
  }

  /**
   * Creates an exception with a cause.
   *
   * @param message what went wrong, for a person
   * @param code the stable code
   * @param cause the underlying failure, or null
   */
  public QeetIDException(String message, String code, @Nullable Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * The stable, machine-readable code, such as {@code not_found}, {@code auth.session_revoked},
   * {@code invalid_grant} or {@code timeout}.
   *
   * @return the code
   */
  public String code() {
    return code;
  }
}
