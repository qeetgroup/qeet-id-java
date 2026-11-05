package com.qeetgroup.qeetid.exception;

/**
 * 503: the server is overloaded ({@code server.overloaded}). Wait {@link #retryAfter()} before
 * trying again.
 */
public class ServiceUnavailableException extends InternalServerException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates the exception for an error answer.
   *
   * @param error the answer
   */
  public ServiceUnavailableException(ApiError error) {
    super(error);
  }
}
