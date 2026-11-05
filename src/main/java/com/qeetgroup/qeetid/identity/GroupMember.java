package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A member of a group.
 *
 * @param userId the user's id
 * @param email the email address
 * @param displayName the display name, or null
 */
public record GroupMember(
    @JsonProperty("user_id") String userId,
    String email,
    @JsonProperty("display_name") @Nullable String displayName) {}
