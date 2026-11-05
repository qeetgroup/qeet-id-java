package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * An API key's record. The key itself is shown only when it is created or rotated.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param userId The user a personal key acts for; null for an organisation key.
 * @param name its name
 * @param prefix The key's first characters, to recognise it by.
 * @param scopes the scopes
 * @param expiresAt when it expires
 * @param lastUsedAt when it was last used
 * @param revokedAt the revoked time
 * @param createdAt when it was created
 * @param createdBy the created by
 * @param rotatedFrom The key this one replaced in a rotation.
 */
public record ApiKey(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("user_id") @Nullable String userId,
    String name,
    String prefix,
    List<String> scopes,
    @JsonProperty("expires_at") @Nullable Instant expiresAt,
    @JsonProperty("last_used_at") @Nullable Instant lastUsedAt,
    @JsonProperty("revoked_at") @Nullable Instant revokedAt,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("created_by") @Nullable String createdBy,
    @JsonProperty("rotated_from") @Nullable String rotatedFrom) {}
