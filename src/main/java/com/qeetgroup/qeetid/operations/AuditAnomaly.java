package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * An audit event that departs from the actor's usual behaviour.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param eventId the event's id
 * @param actorUserId the actor user's id
 * @param actorEmail the actor email
 * @param score the score
 * @param reasons One of {@code "new_action_type"}, {@code "unusual_hour"}, {@code "new_ip"}; others
 *     may be added
 * @param status One of {@code "open"}, {@code "resolved"}; others may be added
 * @param resolvedAt the resolved time
 * @param resolvedBy the resolved by
 * @param action the action
 * @param resourceType the resource type
 * @param ip the IP
 * @param eventAt the event time
 * @param createdAt when it was created
 */
public record AuditAnomaly(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("event_id") String eventId,
    @JsonProperty("actor_user_id") @Nullable String actorUserId,
    @JsonProperty("actor_email") @Nullable String actorEmail,
    double score,
    List<String> reasons,
    String status,
    @JsonProperty("resolved_at") @Nullable Instant resolvedAt,
    @JsonProperty("resolved_by") @Nullable String resolvedBy,
    @Nullable String action,
    @JsonProperty("resource_type") @Nullable String resourceType,
    @Nullable String ip,
    @JsonProperty("event_at") @Nullable Instant eventAt,
    @JsonProperty("created_at") Instant createdAt) {}
