package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A user the identity provider provisioned over SCIM.
 *
 * @param id its id
 * @param email the email address
 * @param displayName the display name, or null
 * @param externalId the external's id
 * @param status its state
 * @param createdAt when it was created
 */
public record ScimUser(
    String id,
    String email,
    @JsonProperty("display_name") @Nullable String displayName,
    @JsonProperty("external_id") @Nullable String externalId,
    String status,
    @JsonProperty("created_at") Instant createdAt) {}
