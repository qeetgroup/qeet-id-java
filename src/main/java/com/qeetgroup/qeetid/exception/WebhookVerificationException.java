package com.qeetgroup.qeetid.exception;

import java.util.Locale;

/**
 * A webhook delivery failed verification. Answer it with a 400 and do not act on it. {@link
 * #reason()} says why; {@link #code()} is the reason in lower case, such as {@code
 * signature_mismatch}.
 */
public class WebhookVerificationException extends QeetIDException {
  private static final long serialVersionUID = 1L;

  /** Why a delivery was refused. */
  public enum Reason {
    /** The delivery has no {@code X-Qeet-Signature} header. */
    MISSING_SIGNATURE,
    /** The delivery has no {@code X-Qeet-Timestamp} header. */
    MISSING_TIMESTAMP,
    /** {@code X-Qeet-Timestamp} isn't Unix seconds. */
    INVALID_TIMESTAMP,
    /** The delivery was signed too long ago (or ahead): a possible replay. */
    TIMESTAMP_OUTSIDE_TOLERANCE,
    /** {@code X-Qeet-Signature} holds no {@code sha256=<hex>} value. */
    INVALID_SIGNATURE_HEADER,
    /** No signature matches any of the secrets. */
    SIGNATURE_MISMATCH,
    /** The body isn't a Qeet ID event. */
    INVALID_PAYLOAD;

    /**
     * The reason as a code, such as {@code signature_mismatch}.
     *
     * @return the code
     */
    public String code() {
      return name().toLowerCase(Locale.ROOT);
    }
  }

  private final Reason reason;

  /**
   * Creates the exception.
   *
   * @param reason why the delivery was refused
   * @param message what was wrong
   */
  public WebhookVerificationException(Reason reason, String message) {
    super(message, reason.code());
    this.reason = reason;
  }

  /**
   * Why the delivery was refused.
   *
   * @return the reason
   */
  public Reason reason() {
    return reason;
  }
}
