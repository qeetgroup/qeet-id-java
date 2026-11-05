package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A figure and its change, in percent, against the previous period.
 *
 * @param value the value
 * @param deltaPct the delta pct
 */
public record AnalyticsMetric(double value, @JsonProperty("delta_pct") double deltaPct) {}
