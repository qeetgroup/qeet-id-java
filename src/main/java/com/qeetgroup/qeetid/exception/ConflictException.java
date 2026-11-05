package com.qeetgroup.qeetid.exception;

/**
 * 409: the request conflicts with the resource's current state, or an idempotency key is still in
 * use. {@code idempotency.outcome_unknown} means the first request with the key was interrupted:
 * check whether it took effect before trying again with a new key.
 */
public class ConflictException extends ApiException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public ConflictException(ApiError error) {
    super(error);
  }
}
