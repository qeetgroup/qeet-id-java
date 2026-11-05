package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qeetgroup.qeetid.access.Role;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A group of users, optionally nested under a parent. Members inherit the group's roles.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param parentId the parent's id
 * @param name its name
 * @param description its description
 * @param memberCount the member count
 * @param createdAt when it was created
 * @param roles The roles granted to the group; listed only for a caller holding {@code role.read}.
 */
public record Group(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("parent_id") @Nullable String parentId,
    String name,
    String description,
    @JsonProperty("member_count") int memberCount,
    @JsonProperty("created_at") Instant createdAt,
    List<Role> roles) {

  /**
   * The roles granted to the group; listed only for a caller holding {@code role.read}.
   *
   * @param roleId the role's id
   * @param name its name
   */
  public record Role(@JsonProperty("role_id") String roleId, String name) {}
}
