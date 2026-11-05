package com.qeetgroup.qeetid.exception;

/** 404: there is no such resource, or the credential may not see it. */
public class NotFoundException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public NotFoundException(ApiError error) {
    super(error);
  }
}
