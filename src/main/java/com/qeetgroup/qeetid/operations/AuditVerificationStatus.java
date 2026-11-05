package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * The latest scheduled verification of the chain.
 *
 * @param verified the verified
 * @param intact the intact
 * @param lastVerifiedAt the last verified time
 * @param brokenAt the broken time
 * @param lastVerifiedEventId the last verified event's id
 * @param lastRunAt the last run time
 * @param lastRunScope the last run scope
 */
public record AuditVerificationStatus(
    boolean verified,
    boolean intact,
    @JsonProperty("last_verified_at") @Nullable Instant lastVerifiedAt,
    @JsonProperty("broken_at") @Nullable Instant brokenAt,
    @JsonProperty("last_verified_event_id") @Nullable String lastVerifiedEventId,
    @JsonProperty("last_run_at") @Nullable Instant lastRunAt,
    @JsonProperty("last_run_scope") String lastRunScope) {}
