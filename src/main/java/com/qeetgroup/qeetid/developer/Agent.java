package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * An AI agent identity, accountable to a human sponsor.
 *
 * @param id its id
 * @param name its name
 * @param scopes the scopes
 * @param tokenTtlSeconds The lifetime of the tokens it gets.
 * @param status One of {@code "active"}, {@code "suspended"}, {@code "decommissioned"}; others may
 *     be added
 * @param disabled True whenever {@code status} isn't {@code "active"}.
 * @param sponsorUserId The person accountable for the agent.
 * @param lastUsedAt when it was last used
 * @param previousSecretExpiresAt While a rotated-out secret still gets tokens.
 * @param createdAt when it was created
 * @param secret The agent's secret: only in {@code "create"}'s answer, once.
 */
public record Agent(
    String id,
    String name,
    List<String> scopes,
    @JsonProperty("token_ttl_seconds") int tokenTtlSeconds,
    String status,
    boolean disabled,
    @JsonProperty("sponsor_user_id") @Nullable String sponsorUserId,
    @JsonProperty("last_used_at") @Nullable Instant lastUsedAt,
    @JsonProperty("previous_secret_expires_at") @Nullable Instant previousSecretExpiresAt,
    @JsonProperty("created_at") Instant createdAt,
    @Nullable String secret) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "Agent[id="
        + id
        + ", name="
        + name
        + ", scopes="
        + scopes
        + ", tokenTtlSeconds="
        + tokenTtlSeconds
        + ", status="
        + status
        + ", disabled="
        + disabled
        + ", sponsorUserId="
        + sponsorUserId
        + ", lastUsedAt="
        + lastUsedAt
        + ", previousSecretExpiresAt="
        + previousSecretExpiresAt
        + ", createdAt="
        + createdAt
        + ", secret=[redacted]]";
  }
}
