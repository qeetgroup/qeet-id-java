package com.qeetgroup.qeetid.exception;

import java.util.Locale;
import org.jspecify.annotations.Nullable;

/**
 * An access token failed local verification. Treat the caller as unauthenticated. {@link #reason()}
 * says why; {@link #code()} is the reason in lower case, such as {@code token_expired}.
 */
public class TokenVerificationException extends QeetIDException {
  private static final long serialVersionUID = 1L;

  /** Why a token was refused. */
  public enum Reason {
    /** The token isn't a well-formed JWT, or lacks a required claim. */
    MALFORMED_TOKEN,
    /** The token isn't signed with ES256. */
    UNSUPPORTED_ALGORITHM,
    /** The token names no key, or one the issuer doesn't publish. */
    UNKNOWN_KEY,
    /** The signature doesn't verify. */
    INVALID_SIGNATURE,
    /** The token has expired. */
    TOKEN_EXPIRED,
    /** The token isn't valid yet ({@code nbf}), or was issued in the future ({@code iat}). */
    TOKEN_NOT_YET_VALID,
    /** The token was issued by another issuer. */
    INVALID_ISSUER,
    /** The token was issued for another audience. */
    INVALID_AUDIENCE,
    /** The issuer's signing keys could not be fetched. */
    JWKS_UNAVAILABLE;

    /**
     * The reason as a code, such as {@code token_expired}.
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
   * @param reason why the token was refused
   * @param message what was wrong
   */
  public TokenVerificationException(Reason reason, String message) {
    this(reason, message, null);
  }

  /**
   * Creates the exception with a cause.
   *
   * @param reason why the token was refused
   * @param message what was wrong
   * @param cause the underlying failure, or null
   */
  public TokenVerificationException(Reason reason, String message, @Nullable Throwable cause) {
    super(message, reason.code(), cause);
    this.reason = reason;
  }

  /**
   * Why the token was refused.
   *
   * @return the reason
   */
  public Reason reason() {
    return reason;
  }
}
