package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** A request to export a user's data. */
public final class ExportCreateParams extends Params {
  private ExportCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ExportCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ExportCreateParams}. */
  public static final class Builder extends Params.Builder<ExportCreateParams, Builder> {
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

    @Override
    public ExportCreateParams build() {
      require("tenant_id", "tenantId");
      require("user_id", "userId");
      return new ExportCreateParams(this);
    }
  }
}
