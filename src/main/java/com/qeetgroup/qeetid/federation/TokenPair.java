package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A session's tokens.
 *
 * @param accessToken the access token
 * @param tokenType the token type
 * @param expiresAt when it expires
 * @param refreshToken the refresh token
 * @param sessionExpiresAt When the session ends however active it is.
 * @param idleExpiresAt When the session ends unless renewed before then.
 * @param sessionId the session's id
 * @param userId the user's id
 * @param tenantId the organisation's id
 */
public record TokenPair(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_at") Instant expiresAt,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("session_expires_at") Instant sessionExpiresAt,
    @JsonProperty("idle_expires_at") Instant idleExpiresAt,
    @JsonProperty("session_id") String sessionId,
    @JsonProperty("user_id") String userId,
    @JsonProperty("tenant_id") @Nullable String tenantId) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "TokenPair[accessToken=[redacted], tokenType="
        + tokenType
        + ", expiresAt="
        + expiresAt
        + ", refreshToken=[redacted], sessionExpiresAt="
        + sessionExpiresAt
        + ", idleExpiresAt="
        + idleExpiresAt
        + ", sessionId="
        + sessionId
        + ", userId="
        + userId
        + ", tenantId="
        + tenantId
        + "]";
  }
}
