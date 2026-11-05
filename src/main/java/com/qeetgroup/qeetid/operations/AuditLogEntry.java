package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * One entry of the hash-chained audit log.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param action the action
 * @param actorType the actor type
 * @param actorUserId the actor user's id
 * @param resourceType the resource type
 * @param resourceId the resource's id
 * @param ip the IP
 * @param userAgent the user agent
 * @param requestId the request's id
 * @param metadata free-form data
 * @param createdAt when it was created
 */
public record AuditLogEntry(
    String id,
    @JsonProperty("tenant_id") @Nullable String tenantId,
    String action,
    @JsonProperty("actor_type") String actorType,
    @JsonProperty("actor_user_id") @Nullable String actorUserId,
    @JsonProperty("resource_type") String resourceType,
    @JsonProperty("resource_id") @Nullable String resourceId,
    @Nullable String ip,
    @JsonProperty("user_agent") @Nullable String userAgent,
    @JsonProperty("request_id") @Nullable String requestId,
    Map<String, Object> metadata,
    @JsonProperty("created_at") Instant createdAt) {}
