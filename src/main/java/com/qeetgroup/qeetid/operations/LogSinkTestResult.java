package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A test event's delivery.
 *
 * @param delivered the delivered
 * @param status its state
 * @param error the error
 * @param durationMs the duration ms
 */
public record LogSinkTestResult(
    boolean delivered,
    @Nullable Integer status,
    @Nullable String error,
    @JsonProperty("duration_ms") int durationMs) {}
