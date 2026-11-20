package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * A scheduled deletion.
 *
 * @param organizationId the organization's id
 * @param deletionDueAt the deletion due time
 */
public record OrganizationDeletion(
    @JsonProperty("organization_id") String organizationId,
    @JsonProperty("deletion_due_at") Instant deletionDueAt) {}
