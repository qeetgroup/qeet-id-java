package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A token endpoint's answer.
 *
 * @param accessToken the access token
 * @param tokenType the token type
 * @param expiresIn The access token's lifetime in seconds.
 * @param scope the scope
 * @param idToken Present when the grant issues one: {@code "openid"} was granted.
 * @param refreshToken Present when the application is registered for {@code refresh_token}.
 * @param issuedTokenType RFC 8693: the type of the token a token exchange issued.
 */
public record TokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_in") int expiresIn,
    @Nullable String scope,
    @JsonProperty("id_token") @Nullable String idToken,
    @JsonProperty("refresh_token") @Nullable String refreshToken,
    @JsonProperty("issued_token_type") @Nullable String issuedTokenType) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "TokenResponse[accessToken=[redacted], tokenType="
        + tokenType
        + ", expiresIn="
        + expiresIn
        + ", scope="
        + scope
        + ", idToken=[redacted], refreshToken=[redacted], issuedTokenType="
        + issuedTokenType
        + "]";
  }
}
