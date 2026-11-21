package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * An allow or deny rule for an address range. Deny wins over allow.
 *
 * @param id its id
 * @param organizationId the organization's id
 * @param action One of {@code "allow"}, {@code "deny"}; others may be added
 * @param cidr the cidr
 * @param label the label
 * @param createdAt when it was created
 */
public record IpRule(
    String id,
    @JsonProperty("organization_id") String organizationId,
    String action,
    String cidr,
    String label,
    @JsonProperty("created_at") Instant createdAt) {}
