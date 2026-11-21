package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/**
 * When audit events are flagged as anomalies. An update replaces all three settings, so each is
 * required: one left out would be stored as off or 0.
 */
public final class AuditAnomalySettingsUpdateParams extends Params {
  private AuditAnomalySettingsUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AuditAnomalySettingsUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AuditAnomalySettingsUpdateParams}. */
  public static final class Builder
      extends Params.Builder<AuditAnomalySettingsUpdateParams, Builder> {
    private Builder() {}

    /**
     * Whether anomaly detection is on. Required.
     *
     * @param enabled the value
     * @return this builder
     */
    public Builder enabled(boolean enabled) {
      return set("enabled", enabled);
    }

    /**
     * Events scoring at least this, from 0 to 1, are flagged. Required.
     *
     * @param scoreThreshold the value
     * @return this builder
     */
    public Builder scoreThreshold(double scoreThreshold) {
      return set("score_threshold", scoreThreshold);
    }

    /**
     * Events an actor needs before their behaviour is judged. Required.
     *
     * @param minBaselineEvents the value
     * @return this builder
     */
    public Builder minBaselineEvents(int minBaselineEvents) {
      return set("min_baseline_events", minBaselineEvents);
    }

    @Override
    public AuditAnomalySettingsUpdateParams build() {
      require("enabled", "enabled");
      require("score_threshold", "scoreThreshold");
      require("min_baseline_events", "minBaselineEvents");
      return new AuditAnomalySettingsUpdateParams(this);
    }
  }
}
