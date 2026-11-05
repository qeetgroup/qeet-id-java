package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * One attempt-tracked delivery of an event to a subscription.
 *
 * @param id its id
 * @param eventType the event type
 * @param attempt the attempt
 * @param statusCode the status code
 * @param error the error
 * @param payload The delivered body, as JSON text.
 * @param responseBody the response body
 * @param deliveredAt the delivered time
 * @param nextAttemptAt the next attempt time
 * @param deadAt the dead time
 * @param createdAt when it was created
 */
public record WebhookDelivery(
    String id,
    @JsonProperty("event_type") String eventType,
    int attempt,
    @JsonProperty("status_code") @Nullable Integer statusCode,
    @Nullable String error,
    String payload,
    @JsonProperty("response_body") @Nullable String responseBody,
    @JsonProperty("delivered_at") @Nullable Instant deliveredAt,
    @JsonProperty("next_attempt_at") @Nullable Instant nextAttemptAt,
    @JsonProperty("dead_at") @Nullable Instant deadAt,
    @JsonProperty("created_at") Instant createdAt) {}
