package com.qeetgroup.qeetid.exception;

/** 401: the credential is missing, wrong, expired or revoked. */
public class AuthenticationException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public AuthenticationException(ApiError error) {
    super(error);
  }
}
