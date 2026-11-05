package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * What removing a member who belongs to another organisation took away here.
 *
 * @param id its id
 * @param rolesRemoved the roles removed
 * @param sessionsEnded the sessions ended
 */
public record MembershipRemoval(
    String id,
    @JsonProperty("roles_removed") int rolesRemoved,
    @JsonProperty("sessions_ended") int sessionsEnded) {}
