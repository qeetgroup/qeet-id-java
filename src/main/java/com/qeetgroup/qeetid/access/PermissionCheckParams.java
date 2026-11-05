package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** A permission check: may {@code user_id} use {@code permission} in {@code tenant_id}? */
public final class PermissionCheckParams extends Params {
  private PermissionCheckParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link PermissionCheckParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link PermissionCheckParams}. */
  public static final class Builder extends Params.Builder<PermissionCheckParams, Builder> {
    private Builder() {}

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
     * The organisation's id. Required.
     *
     * @param tenantId the value
     * @return this builder
     */
    public Builder tenantId(String tenantId) {
      return set("tenant_id", nonNull(tenantId, "tenantId"));
    }

    /**
     * A permission key, such as {@code invoice.write}. Required.
     *
     * @param permission the value
     * @return this builder
     */
    public Builder permission(String permission) {
      return set("permission", nonNull(permission, "permission"));
    }

    @Override
    public PermissionCheckParams build() {
      require("user_id", "userId");
      require("tenant_id", "tenantId");
      require("permission", "permission");
      return new PermissionCheckParams(this);
    }
  }
}
