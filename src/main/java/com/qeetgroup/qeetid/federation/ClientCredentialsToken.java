package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * The client-credentials token endpoint's answer: a token with its lifetime and expiry.
 *
 * @param accessToken the access token
 * @param tokenType the token type
 * @param expiresIn The access token's lifetime in seconds.
 * @param expiresAt When the access token expires.
 * @param scope The service principal's scopes, space-separated.
 */
public record ClientCredentialsToken(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_in") int expiresIn,
    @JsonProperty("expires_at") Instant expiresAt,
    String scope) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "ClientCredentialsToken[accessToken=[redacted], tokenType="
        + tokenType
        + ", expiresIn="
        + expiresIn
        + ", expiresAt="
        + expiresAt
        + ", scope="
        + scope
        + "]";
  }
}
