package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A SCIM bearer token's record.
 *
 * @param id its id
 * @param name its name
 * @param tokenPrefix the token prefix
 * @param status One of {@code "active"}, {@code "rotated"}, {@code "expired"}, {@code "revoked"};
 *     others may be added
 * @param createdAt when it was created
 * @param expiresAt when it expires
 * @param lastUsedAt when it was last used
 * @param revokedAt the revoked time
 * @param replacedBy the replaced by
 */
public record ScimToken(
    String id,
    String name,
    @JsonProperty("token_prefix") String tokenPrefix,
    String status,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("expires_at") @Nullable Instant expiresAt,
    @JsonProperty("last_used_at") @Nullable Instant lastUsedAt,
    @JsonProperty("revoked_at") @Nullable Instant revokedAt,
    @JsonProperty("replaced_by") @Nullable String replacedBy) {}
