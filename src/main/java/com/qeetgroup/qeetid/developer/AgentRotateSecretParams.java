package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;

/** How long an agent's replaced secret keeps getting tokens. */
public final class AgentRotateSecretParams extends Params {
  private AgentRotateSecretParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AgentRotateSecretParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AgentRotateSecretParams}. */
  public static final class Builder extends Params.Builder<AgentRotateSecretParams, Builder> {
    private Builder() {}

    /**
     * How many hours the replaced credential keeps working: 0 stops it at once. The server bounds
     * it.
     *
     * @param overlapHours the value
     * @return this builder
     */
    public Builder overlapHours(int overlapHours) {
      return set("overlap_hours", overlapHours);
    }

    @Override
    public AgentRotateSecretParams build() {
      return new AgentRotateSecretParams(this);
    }
  }
}
