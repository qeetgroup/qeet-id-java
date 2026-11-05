package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * One attempt to send a delivery.
 *
 * @param attemptedAt the attempted time
 * @param statusCode the status code
 * @param durationMs the duration ms
 * @param error the error
 */
public record WebhookDeliveryAttempt(
    @JsonProperty("attempted_at") Instant attemptedAt,
    @JsonProperty("status_code") @Nullable Integer statusCode,
    @JsonProperty("duration_ms") int durationMs,
    @Nullable String error) {}
