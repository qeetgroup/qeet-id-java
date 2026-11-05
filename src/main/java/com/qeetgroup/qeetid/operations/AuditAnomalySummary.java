package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * How many audit anomalies are open, high-scoring and recently resolved.
 *
 * @param open the open
 * @param highScoreOpen the high score open
 * @param resolved7d the resolved 7d
 */
public record AuditAnomalySummary(
    int open,
    @JsonProperty("high_score_open") int highScoreOpen,
    @JsonProperty("resolved_7d") int resolved7d) {}
