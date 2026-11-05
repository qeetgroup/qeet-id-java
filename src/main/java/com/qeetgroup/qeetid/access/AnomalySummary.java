package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * How many sign-in anomalies are open and were resolved, and whom they affect.
 *
 * @param open the open
 * @param resolved24h the resolved 24h
 * @param affectedAccounts the affected accounts
 * @param highSeverity24h the high severity 24h
 */
public record AnomalySummary(
    int open,
    @JsonProperty("resolved_24h") int resolved24h,
    @JsonProperty("affected_accounts") int affectedAccounts,
    @JsonProperty("high_severity_24h") int highSeverity24h) {}
