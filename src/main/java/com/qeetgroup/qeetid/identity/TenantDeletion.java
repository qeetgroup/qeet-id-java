package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * A scheduled deletion.
 *
 * @param tenantId the organisation's id
 * @param deletionDueAt the deletion due time
 */
public record TenantDeletion(
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("deletion_due_at") Instant deletionDueAt) {}
