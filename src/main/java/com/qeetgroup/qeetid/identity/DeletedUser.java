package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A soft-deleted user, as the recycle bin shows it.
 *
 * @param id its id
 * @param email the email address
 * @param displayName the display name, or null
 * @param deletedAt the deleted time
 * @param deletedBy The administrator who deleted the user, when recorded.
 * @param createdAt when it was created
 */
public record DeletedUser(
    String id,
    String email,
    @JsonProperty("display_name") @Nullable String displayName,
    @JsonProperty("deleted_at") Instant deletedAt,
    @JsonProperty("deleted_by") @Nullable String deletedBy,
    @JsonProperty("created_at") Instant createdAt) {}
