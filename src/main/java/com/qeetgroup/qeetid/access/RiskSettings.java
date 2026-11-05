package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * Adaptive-MFA risk settings. Replaced whole on update.
 *
 * @param mediumThreshold the medium threshold
 * @param highThreshold the high threshold
 * @param forceMfaAtLevel The risk level at which a second factor is required even on a trusted
 *     device. One of {@code "medium"}, {@code "high"}; others may be added
 * @param impossibleTravelEnabled Flags a sign-in from a new country sooner than {@code
 *     min_travel_hours} after the last one.
 * @param minTravelHours the min travel hours
 * @param deviceReputationEnabled Flags a sign-in from a browser and OS never seen for the user.
 * @param countryHeaderConfigured Read-only: whether the deployment receives a country header.
 * @param countryLastSeenAt the country last seen time
 */
public record RiskSettings(
    @JsonProperty("medium_threshold") double mediumThreshold,
    @JsonProperty("high_threshold") double highThreshold,
    @JsonProperty("force_mfa_at_level") String forceMfaAtLevel,
    @JsonProperty("impossible_travel_enabled") boolean impossibleTravelEnabled,
    @JsonProperty("min_travel_hours") double minTravelHours,
    @JsonProperty("device_reputation_enabled") boolean deviceReputationEnabled,
    @JsonProperty("country_header_configured") @Nullable Boolean countryHeaderConfigured,
    @JsonProperty("country_last_seen_at") @Nullable Instant countryLastSeenAt) {}
