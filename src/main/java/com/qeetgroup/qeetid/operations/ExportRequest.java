package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * A data export. {@code payload} is filled in once {@code status} is {@code ready}.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param userId the user's id
 * @param requestedBy the requested by
 * @param status One of {@code "pending"}, {@code "ready"}, {@code "failed"}; others may be added
 * @param payload the payload
 * @param error the error
 * @param completedAt the completed time
 * @param createdAt when it was created
 */
public record ExportRequest(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("user_id") String userId,
    @JsonProperty("requested_by") @Nullable String requestedBy,
    String status,
    Map<String, Object> payload,
    @Nullable String error,
    @JsonProperty("completed_at") @Nullable Instant completedAt,
    @JsonProperty("created_at") Instant createdAt) {}
