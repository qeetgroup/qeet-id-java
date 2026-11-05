package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qeetgroup.qeetid.identity.Group;
import com.qeetgroup.qeetid.identity.User;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Who may sign in to a service provider that requires assignment.
 *
 * @param users the users
 * @param groups the groups
 */
public record SamlServiceProviderAssignments(List<User> users, List<Group> groups) {

  /**
   * User.
   *
   * @param userId the user's id
   * @param email the email address
   * @param displayName the display name, or null
   * @param assignedAt the assigned time
   * @param assignedBy the assigned by
   */
  public record User(
      @JsonProperty("user_id") String userId,
      String email,
      @JsonProperty("display_name") @Nullable String displayName,
      @JsonProperty("assigned_at") Instant assignedAt,
      @JsonProperty("assigned_by") @Nullable String assignedBy) {}

  /**
   * Group.
   *
   * @param groupId the group's id
   * @param name its name
   * @param assignedAt the assigned time
   * @param assignedBy the assigned by
   */
  public record Group(
      @JsonProperty("group_id") String groupId,
      String name,
      @JsonProperty("assigned_at") Instant assignedAt,
      @JsonProperty("assigned_by") @Nullable String assignedBy) {}
}
