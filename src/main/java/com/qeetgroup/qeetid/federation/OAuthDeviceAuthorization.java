package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A device-flow authorization, for review. Never includes the device code.
 *
 * @param id its id
 * @param clientId the client's id
 * @param clientName the client name
 * @param userCode the user code
 * @param status One of {@code "pending"}, {@code "authorized"}, {@code "denied"}; others may be
 *     added
 * @param userId the user's id
 * @param userEmail the user email
 * @param scopes the scopes
 * @param createdAt when it was created
 * @param expiresAt when it expires
 * @param lastPolledAt the last polled time
 * @param consumedAt the consumed time
 */
public record OAuthDeviceAuthorization(
    String id,
    @JsonProperty("client_id") String clientId,
    @JsonProperty("client_name") @Nullable String clientName,
    @JsonProperty("user_code") String userCode,
    String status,
    @JsonProperty("user_id") @Nullable String userId,
    @JsonProperty("user_email") @Nullable String userEmail,
    List<String> scopes,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("expires_at") Instant expiresAt,
    @JsonProperty("last_polled_at") @Nullable Instant lastPolledAt,
    @JsonProperty("consumed_at") @Nullable Instant consumedAt) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "OAuthDeviceAuthorization[id="
        + id
        + ", clientId="
        + clientId
        + ", clientName="
        + clientName
        + ", userCode=[redacted], status="
        + status
        + ", userId="
        + userId
        + ", userEmail="
        + userEmail
        + ", scopes="
        + scopes
        + ", createdAt="
        + createdAt
        + ", expiresAt="
        + expiresAt
        + ", lastPolledAt="
        + lastPolledAt
        + ", consumedAt="
        + consumedAt
        + "]";
  }
}
