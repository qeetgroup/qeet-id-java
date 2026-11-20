package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A token for an agent, from its id and secret: the client-credentials grant's answer.
 *
 * @param accessToken the access token
 * @param tokenType the token type
 * @param expiresIn The access token's lifetime in seconds: the agent's own token lifetime.
 * @param expiresAt When the access token expires.
 * @param scope The agent's scopes, space-separated; null when it holds none.
 */
public record AgentToken(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_in") int expiresIn,
    @JsonProperty("expires_at") Instant expiresAt,
    @Nullable String scope) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "AgentToken[accessToken=[redacted], tokenType="
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
