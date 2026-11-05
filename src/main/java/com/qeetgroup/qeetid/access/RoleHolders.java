package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qeetgroup.qeetid.identity.Group;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Who holds a role: people (directly or through groups) and the groups granted it.
 *
 * @param people the people
 * @param groups the groups
 * @param nextCursor Pass as {@code "cursor"} for the next page of people; absent on the last.
 */
public record RoleHolders(
    List<People> people,
    List<Group> groups,
    @JsonProperty("next_cursor") @Nullable String nextCursor) {

  /**
   * People.
   *
   * @param userId the user's id
   * @param email the email address
   * @param displayName the display name, or null
   * @param direct the direct
   * @param viaGroups the via groups
   */
  public record People(
      @JsonProperty("user_id") String userId,
      String email,
      @JsonProperty("display_name") String displayName,
      boolean direct,
      @JsonProperty("via_groups") List<ViaGroup> viaGroups) {

    /**
     * Via group.
     *
     * @param id its id
     * @param name its name
     */
    public record ViaGroup(String id, String name) {}
  }

  /**
   * Group.
   *
   * @param id its id
   * @param name its name
   */
  public record Group(String id, String name) {}
}
