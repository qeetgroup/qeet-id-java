package com.qeetgroup.qeetid.exception;

/** 422: the request was well formed but refused; see {@link #fields()}. */
public class ValidationException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public ValidationException(ApiError error) {
    super(error);
  }
}
