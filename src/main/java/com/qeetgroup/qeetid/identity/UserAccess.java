package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qeetgroup.qeetid.access.Role;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A user's access in the organisation: roles, groups and permission counts.
 *
 * @param organization the organization
 * @param roles the roles
 * @param groups the groups
 * @param applicationsCount the applications count
 * @param policiesCount the policies count
 * @param permissionsDirect the permissions direct
 * @param permissionsInherited the permissions inherited
 * @param permissionsTotal the permissions total
 */
public record UserAccess(
    Organization organization,
    List<Role> roles,
    List<Group> groups,
    @JsonProperty("applications_count") int applicationsCount,
    @JsonProperty("policies_count") int policiesCount,
    @JsonProperty("permissions_direct") int permissionsDirect,
    @JsonProperty("permissions_inherited") int permissionsInherited,
    @JsonProperty("permissions_total") int permissionsTotal) {

  /**
   * Organization.
   *
   * @param id its id
   * @param name its name
   * @param slug the slug
   */
  public record Organization(String id, String name, String slug) {}

  /**
   * Role.
   *
   * @param id its id
   * @param name its name
   * @param description its description
   */
  public record Role(String id, String name, @Nullable String description) {}

  /**
   * Group.
   *
   * @param id its id
   * @param name its name
   * @param description its description
   */
  public record Group(String id, String name, @Nullable String description) {}
}
