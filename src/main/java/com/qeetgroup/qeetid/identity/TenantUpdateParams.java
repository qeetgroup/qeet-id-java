package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import java.util.Map;

/**
 * The fields an update may change. Changing {@code status} is an owner's; the region can't change.
 */
public final class TenantUpdateParams extends Params {
  private TenantUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link TenantUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link TenantUpdateParams}. */
  public static final class Builder extends Params.Builder<TenantUpdateParams, Builder> {
    private Builder() {}

    /**
     * Its name.
     *
     * @param name the value
     * @return this builder
     */
    public Builder name(String name) {
      return set("name", nonNull(name, "name"));
    }

    /**
     * One of {@code "active"}, {@code "suspended"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    /**
     * One of {@code "free"}, {@code "pro"}, {@code "enterprise"}.
     *
     * @param plan the value
     * @return this builder
     */
    public Builder plan(String plan) {
      return set("plan", nonNull(plan, "plan"));
    }

    /**
     * Free-form data.
     *
     * @param metadata the value
     * @return this builder
     */
    public Builder metadata(Map<String, ?> metadata) {
      return set("metadata", nonNull(metadata, "metadata"));
    }

    @Override
    public TenantUpdateParams build() {
      return new TenantUpdateParams(this);
    }
  }
}
