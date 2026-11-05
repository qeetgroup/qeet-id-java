package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * A detected sign-in anomaly.
 *
 * @param id its id
 * @param type its type
 * @param severity the severity
 * @param detail the detail
 * @param status One of {@code "open"}, {@code "resolved"}; others may be added
 * @param userId the user's id
 * @param userEmail the user email
 * @param ip the IP
 * @param metadata free-form data
 * @param createdAt when it was created
 * @param resolvedAt the resolved time
 * @param resolvedBy the resolved by
 * @param resolvedByEmail the resolved by email
 */
public record Anomaly(
    String id,
    String type,
    String severity,
    String detail,
    String status,
    @JsonProperty("user_id") @Nullable String userId,
    @JsonProperty("user_email") @Nullable String userEmail,
    @Nullable String ip,
    Map<String, Object> metadata,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("resolved_at") @Nullable Instant resolvedAt,
    @JsonProperty("resolved_by") @Nullable String resolvedBy,
    @JsonProperty("resolved_by_email") @Nullable String resolvedByEmail) {}
