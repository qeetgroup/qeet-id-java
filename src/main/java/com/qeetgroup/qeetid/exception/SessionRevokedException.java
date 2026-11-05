package com.qeetgroup.qeetid.exception;

/**
 * 401 {@code auth.session_revoked}: the session behind the token has ended (signed out, revoked by
 * an administrator, or timed out). Never retried: sign in again instead.
 */
public class SessionRevokedException extends AuthenticationException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public SessionRevokedException(ApiError error) {
    super(error);
  }
}
