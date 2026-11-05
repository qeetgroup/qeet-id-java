package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * Sign-in lockout settings.
 *
 * @param threshold the threshold
 * @param windowMinutes the window minutes
 * @param durationMinutes the duration minutes
 */
public record LockoutSettings(
    @Nullable Double threshold,
    @JsonProperty("window_minutes") @Nullable Integer windowMinutes,
    @JsonProperty("duration_minutes") @Nullable Integer durationMinutes) {}
