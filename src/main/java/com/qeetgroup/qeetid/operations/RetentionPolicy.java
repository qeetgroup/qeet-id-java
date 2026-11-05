package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * When soft-deleted users are purged for good.
 *
 * @param deletedUsersEnabled whether deleted users enabled
 * @param deletedUsersDays 1 to 3650; clamped.
 * @param updatedAt when it last changed
 * @param updatedBy the updated by
 * @param updatedByEmail the updated by email
 */
public record RetentionPolicy(
    @JsonProperty("deleted_users_enabled") boolean deletedUsersEnabled,
    @JsonProperty("deleted_users_days") int deletedUsersDays,
    @JsonProperty("updated_at") @Nullable Instant updatedAt,
    @JsonProperty("updated_by") @Nullable String updatedBy,
    @JsonProperty("updated_by_email") @Nullable String updatedByEmail) {}
