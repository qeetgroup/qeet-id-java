package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * When audit events are flagged as anomalies. Replaced whole on update, with {@link
 * AuditAnomalySettingsUpdateParams}.
 *
 * @param organizationId the organization's id
 * @param enabled whether it is on
 * @param scoreThreshold Events scoring at least this are flagged.
 * @param minBaselineEvents Events an actor needs before their behaviour is judged.
 */
public record AuditAnomalySettings(
    @JsonProperty("organization_id") String organizationId,
    boolean enabled,
    @JsonProperty("score_threshold") double scoreThreshold,
    @JsonProperty("min_baseline_events") int minBaselineEvents) {}
