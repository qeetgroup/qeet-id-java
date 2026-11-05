package com.qeetgroup.qeetid.exception;

/** 5xx: the server failed. Quote {@link #requestId()} to support. */
public class InternalServerException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public InternalServerException(ApiError error) {
    super(error);
  }
}
