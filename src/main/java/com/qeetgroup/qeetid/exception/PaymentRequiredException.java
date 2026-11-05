package com.qeetgroup.qeetid.exception;

/** 402: the organisation's plan doesn't include this, or a plan limit is reached. */
public class PaymentRequiredException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public PaymentRequiredException(ApiError error) {
    super(error);
  }
}
