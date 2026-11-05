package com.qeetgroup.qeetid.exception;

import org.jspecify.annotations.Nullable;

/**
 * An OAuth endpoint refused the request. These endpoints answer in the RFC 6749 section 5.2 shape,
 * {@code {"error", "error_description"}}, not the API's error envelope: {@link #code()} is the
 * {@code error} value and the message its description. {@link #status()} is 401 for {@code
 * invalid_client} and 400 otherwise.
 *
 * <p>The codes: {@code invalid_request}, {@code invalid_client}, {@code invalid_grant}, {@code
 * unauthorized_client}, {@code unsupported_grant_type}, {@code invalid_scope}, {@code
 * invalid_target}, {@code unsupported_token_type}, and while polling the device and backchannel
 * flows {@code authorization_pending}, {@code slow_down}, {@code access_denied} and {@code
 * expired_token}. The constants of this class name them.
 */
public class OAuthException extends ApiException {
  private static final long serialVersionUID = 1L;

  /** The request is missing a parameter or is otherwise malformed. */
  public static final String INVALID_REQUEST = "invalid_request";

  /** The client is unknown, or its secret is wrong (status 401). */
  public static final String INVALID_CLIENT = "invalid_client";

  /** The grant (code, refresh token, device code) is invalid, expired, used or revoked. */
  public static final String INVALID_GRANT = "invalid_grant";

  /** The client may not use this grant. */
  public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";

  /** The endpoint doesn't serve this grant. */
  public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";

  /** A scope was asked for that the client may not have. */
  public static final String INVALID_SCOPE = "invalid_scope";

  /** RFC 8707: the resource asked for is unknown or not allowed. */
  public static final String INVALID_TARGET = "invalid_target";

  /** RFC 7009: the token can't be revoked, such as an access token. */
  public static final String UNSUPPORTED_TOKEN_TYPE = "unsupported_token_type";

  /** RFC 8628 and CIBA: the person hasn't decided yet; poll again after the interval. */
  public static final String AUTHORIZATION_PENDING = "authorization_pending";

  /** RFC 8628 and CIBA: polling too fast; add five seconds to the interval. */
  public static final String SLOW_DOWN = "slow_down";

  /** RFC 8628 and CIBA: the person refused. Stop polling. */
  public static final String ACCESS_DENIED = "access_denied";

  /** RFC 8628 and CIBA: the request expired. Stop polling. */
  public static final String EXPIRED_TOKEN = "expired_token";

  private final @Nullable String errorDescription;
  private final @Nullable String errorUri;

  /**
   * Creates the exception for an RFC 6749 error answer.
   *
   * @param error the answer; its code is the RFC 6749 {@code error}
   * @param errorDescription the {@code error_description}, or null
   * @param errorUri the {@code error_uri}, or null
   */
  public OAuthException(
      ApiError error, @Nullable String errorDescription, @Nullable String errorUri) {
    super(error);
    this.errorDescription = errorDescription;
    this.errorUri = errorUri;
  }

  /**
   * The RFC 6749 {@code error} value; the same as {@link #code()}.
   *
   * @return the error code
   */
  public String error() {
    return code();
  }

  /**
   * The RFC 6749 {@code error_description}, when the server gave one.
   *
   * @return the description, or null
   */
  public @Nullable String errorDescription() {
    return errorDescription;
  }

  /**
   * The RFC 6749 {@code error_uri}, when the server gave one.
   *
   * @return the URI, or null
   */
  public @Nullable String errorUri() {
    return errorUri;
  }
}
