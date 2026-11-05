package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A token for an agent, from its id and secret.
 *
 * @param accessToken the access token
 * @param tokenType the token type
 * @param expiresIn the expires in
 * @param scope the scope
 */
public record AgentToken(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_in") int expiresIn,
    @Nullable String scope) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "AgentToken[accessToken=[redacted], tokenType="
        + tokenType
        + ", expiresIn="
        + expiresIn
        + ", scope="
        + scope
        + "]";
  }
}
