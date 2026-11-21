package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** A permission check: may {@code user_id} use {@code permission} in {@code organization_id}? */
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
     * The organization's id. Required.
     *
     * @param organizationId the value
     * @return this builder
     */
    public Builder organizationId(String organizationId) {
      return set("organization_id", nonNull(organizationId, "organizationId"));
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
      require("organization_id", "organizationId");
      require("permission", "permission");
      return new PermissionCheckParams(this);
    }
  }
}
