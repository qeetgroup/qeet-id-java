package com.qeetgroup.qeetid.exception;

/** 429: too many requests. Wait {@link #retryAfter()} before trying again. */
public class RateLimitException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public RateLimitException(ApiError error) {
    super(error);
  }
}
