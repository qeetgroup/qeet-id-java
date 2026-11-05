package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * A user, as the API returns one.
 *
 * @param id its id
 * @param tenantId The user's home organisation.
 * @param email the email address
 * @param emailVerifiedAt the email verified time
 * @param phone the phone
 * @param phoneVerifiedAt the phone verified time
 * @param displayName the display name, or null
 * @param avatarUrl the avatar URL
 * @param status One of {@code "active"}, {@code "invited"}, {@code "suspended"}; others may be
 *     added
 * @param metadata Free-form data. Empty when the user's home is another organisation, which keeps
 *     it.
 * @param createdAt when it was created
 * @param updatedAt when it last changed
 * @param roles Role names in the organisation reading the user; on lists and a member's own record.
 * @param assignedRoles The same roles with their ids.
 * @param mfaEnabled whether MFA enabled
 * @param groupsCount the groups count
 * @param lastSeenAt the last seen time
 * @param lastSeenIp the last seen IP
 * @param homeTenantName The name of the organisation that manages the account.
 * @param suspension the suspension
 */
public record User(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String email,
    @JsonProperty("email_verified_at") @Nullable Instant emailVerifiedAt,
    @Nullable String phone,
    @JsonProperty("phone_verified_at") @Nullable Instant phoneVerifiedAt,
    @JsonProperty("display_name") @Nullable String displayName,
    @JsonProperty("avatar_url") @Nullable String avatarUrl,
    String status,
    Map<String, Object> metadata,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    List<String> roles,
    @JsonProperty("assigned_roles") List<AssignedRole> assignedRoles,
    @JsonProperty("mfa_enabled") @Nullable Boolean mfaEnabled,
    @JsonProperty("groups_count") @Nullable Integer groupsCount,
    @JsonProperty("last_seen_at") @Nullable Instant lastSeenAt,
    @JsonProperty("last_seen_ip") @Nullable String lastSeenIp,
    @JsonProperty("home_tenant_name") @Nullable String homeTenantName,
    @Nullable UserSuspension suspension) {

  /**
   * The same roles with their ids.
   *
   * @param id its id
   * @param name its name
   */
  public record AssignedRole(String id, String name) {}
}
