package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * A role granted to a group.
 *
 * @param roleId the role's id
 * @param name its name
 * @param grantedAt the granted time
 */
public record GroupRole(
    @JsonProperty("role_id") String roleId,
    String name,
    @JsonProperty("granted_at") Instant grantedAt) {}
