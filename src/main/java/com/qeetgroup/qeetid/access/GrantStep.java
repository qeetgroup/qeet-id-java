package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * One grant behind a permission: the role, held directly or through a group.
 *
 * @param permission the permission
 * @param grantedBy the granted by
 * @param via {@code "direct"}, or {@code group:<name>}.
 * @param groupId the group's id
 * @param groupName the group name
 * @param roleId the role's id
 * @param roleName the role name
 */
public record GrantStep(
    String permission,
    @JsonProperty("granted_by") String grantedBy,
    String via,
    @JsonProperty("group_id") @Nullable String groupId,
    @JsonProperty("group_name") @Nullable String groupName,
    @JsonProperty("role_id") String roleId,
    @JsonProperty("role_name") @Nullable String roleName) {}
