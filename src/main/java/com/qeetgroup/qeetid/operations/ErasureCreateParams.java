package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** A request to erase a user's data after a grace period. */
public final class ErasureCreateParams extends Params {
  private ErasureCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ErasureCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ErasureCreateParams}. */
  public static final class Builder extends Params.Builder<ErasureCreateParams, Builder> {
    private Builder() {}

    /**
     * The organisation's id. Required.
     *
     * @param tenantId the value
     * @return this builder
     */
    public Builder tenantId(String tenantId) {
      return set("tenant_id", nonNull(tenantId, "tenantId"));
    }

    /**
     * The user's id. Required.
     *
     * @param userId the value
     * @return this builder
     */
    public Builder userId(String userId) {
      return set("user_id", nonNull(userId, "userId"));
    }

    /**
     * Why. Required.
     *
     * @param reason the value
     * @return this builder
     */
    public Builder reason(String reason) {
      return set("reason", nonNull(reason, "reason"));
    }

    @Override
    public ErasureCreateParams build() {
      require("tenant_id", "tenantId");
      require("user_id", "userId");
      require("reason", "reason");
      return new ErasureCreateParams(this);
    }
  }
}
