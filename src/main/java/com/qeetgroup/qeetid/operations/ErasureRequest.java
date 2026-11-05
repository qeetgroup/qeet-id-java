package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * An erasure request. It runs when its grace period ends, unless cancelled first.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param userId the user's id
 * @param requestedBy the requested by
 * @param reason the reason
 * @param status its state
 * @param graceUntil the grace until
 * @param gracePeriodDays the grace period days
 * @param completedAt the completed time
 * @param createdAt when it was created
 * @param subjectEmail the subject email
 * @param subjectName the subject name
 * @param subjectRedacted The subject's details were erased.
 */
public record ErasureRequest(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("user_id") String userId,
    @JsonProperty("requested_by") @Nullable String requestedBy,
    @Nullable String reason,
    String status,
    @JsonProperty("grace_until") Instant graceUntil,
    @JsonProperty("grace_period_days") @Nullable Integer gracePeriodDays,
    @JsonProperty("completed_at") @Nullable Instant completedAt,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("subject_email") @Nullable String subjectEmail,
    @JsonProperty("subject_name") @Nullable String subjectName,
    @JsonProperty("subject_redacted") @Nullable Boolean subjectRedacted) {}
