package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A group the identity provider provisioned over SCIM.
 *
 * @param id its id
 * @param name its name
 * @param externalId the external's id
 * @param members the members
 * @param createdAt when it was created
 * @param updatedAt when it last changed
 */
public record ScimGroup(
    String id,
    String name,
    @JsonProperty("external_id") @Nullable String externalId,
    int members,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt) {}
