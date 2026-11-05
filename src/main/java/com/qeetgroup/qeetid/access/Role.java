package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A role: a named set of permissions. Built-in roles ({@code owner}, {@code admin}, {@code member},
 * {@code viewer}) have {@code is_system} set and can't change.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param name its name
 * @param description its description
 * @param isSystem whether is system
 * @param createdAt when it was created
 * @param holders How many hold the role, directly or through groups.
 */
public record Role(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String name,
    @Nullable String description,
    @JsonProperty("is_system") boolean isSystem,
    @JsonProperty("created_at") Instant createdAt,
    @Nullable Holders holders) {

  /**
   * How many hold the role, directly or through groups.
   *
   * @param peopleDirect the people direct
   * @param peopleThroughGroups the people through groups
   * @param groups the groups
   */
  public record Holders(
      @JsonProperty("people_direct") int peopleDirect,
      @JsonProperty("people_through_groups") int peopleThroughGroups,
      int groups) {}
}
