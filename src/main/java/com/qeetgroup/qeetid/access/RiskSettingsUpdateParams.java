package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** Adaptive-MFA risk settings, replaced whole. */
public final class RiskSettingsUpdateParams extends Params {
  private RiskSettingsUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RiskSettingsUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RiskSettingsUpdateParams}. */
  public static final class Builder extends Params.Builder<RiskSettingsUpdateParams, Builder> {
    private Builder() {}

    /**
     * The medium threshold. Required.
     *
     * @param mediumThreshold the value
     * @return this builder
     */
    public Builder mediumThreshold(double mediumThreshold) {
      return set("medium_threshold", mediumThreshold);
    }

    /**
     * The high threshold. Required.
     *
     * @param highThreshold the value
     * @return this builder
     */
    public Builder highThreshold(double highThreshold) {
      return set("high_threshold", highThreshold);
    }

    /**
     * The risk level at which a second factor is required even on a trusted device. One of {@code
     * "medium"}, {@code "high"}. Required.
     *
     * @param forceMfaAtLevel the value
     * @return this builder
     */
    public Builder forceMfaAtLevel(String forceMfaAtLevel) {
      return set("force_mfa_at_level", nonNull(forceMfaAtLevel, "forceMfaAtLevel"));
    }

    /**
     * Flags a sign-in from a new country sooner than {@code min_travel_hours} after the last one.
     * Required.
     *
     * @param impossibleTravelEnabled the value
     * @return this builder
     */
    public Builder impossibleTravelEnabled(boolean impossibleTravelEnabled) {
      return set("impossible_travel_enabled", impossibleTravelEnabled);
    }

    /**
     * The min travel hours. Required.
     *
     * @param minTravelHours the value
     * @return this builder
     */
    public Builder minTravelHours(double minTravelHours) {
      return set("min_travel_hours", minTravelHours);
    }

    /**
     * Flags a sign-in from a browser and OS never seen for the user. Required.
     *
     * @param deviceReputationEnabled the value
     * @return this builder
     */
    public Builder deviceReputationEnabled(boolean deviceReputationEnabled) {
      return set("device_reputation_enabled", deviceReputationEnabled);
    }

    @Override
    public RiskSettingsUpdateParams build() {
      require("medium_threshold", "mediumThreshold");
      require("high_threshold", "highThreshold");
      require("force_mfa_at_level", "forceMfaAtLevel");
      require("impossible_travel_enabled", "impossibleTravelEnabled");
      require("min_travel_hours", "minTravelHours");
      require("device_reputation_enabled", "deviceReputationEnabled");
      return new RiskSettingsUpdateParams(this);
    }
  }
}
