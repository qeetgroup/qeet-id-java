package com.qeetgroup.qeetid.exception;

/** 403: the credential is valid but may not do this. */
public class PermissionDeniedException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public PermissionDeniedException(ApiError error) {
    super(error);
  }
}
