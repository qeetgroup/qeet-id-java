package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * A test call's outcome.
 *
 * @param statusCode the status code
 * @param durationMs the duration ms
 * @param decision One of {@code "allow"}, {@code "deny"}, {@code "none"}; others may be added
 * @param message the message
 * @param claims the claims
 * @param error the error
 */
public record AuthHookTestResult(
    @JsonProperty("status_code") @Nullable Integer statusCode,
    @JsonProperty("duration_ms") @Nullable Integer durationMs,
    @Nullable String decision,
    @Nullable String message,
    Map<String, Object> claims,
    @Nullable String error) {}
