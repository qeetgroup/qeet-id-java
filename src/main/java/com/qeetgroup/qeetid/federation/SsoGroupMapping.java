package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * Maps an identity provider group to one of the organisation's groups.
 *
 * @param id its id
 * @param idpGroup the idp group
 * @param groupId the group's id
 * @param groupName the group name
 * @param createdAt when it was created
 */
public record SsoGroupMapping(
    String id,
    @JsonProperty("idp_group") String idpGroup,
    @JsonProperty("group_id") String groupId,
    @JsonProperty("group_name") String groupName,
    @JsonProperty("created_at") Instant createdAt) {}
