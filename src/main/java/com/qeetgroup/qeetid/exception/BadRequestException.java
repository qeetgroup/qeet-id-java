package com.qeetgroup.qeetid.exception;

/** 400: the request was malformed. */
public class BadRequestException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public BadRequestException(ApiError error) {
    super(error);
  }
}
