package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import java.util.List;

/** Up to 1000 users to create in one call. */
public final class UserBulkCreateParams extends Params {
  private UserBulkCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link UserBulkCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link UserBulkCreateParams}. */
  public static final class Builder extends Params.Builder<UserBulkCreateParams, Builder> {
    private Builder() {}

    /**
     * Optional; when present it must be the API key's own organisation.
     *
     * @param tenantId the value
     * @return this builder
     */
    public Builder tenantId(String tenantId) {
      return set("tenant_id", nonNull(tenantId, "tenantId"));
    }

    /**
     * A role every created user receives.
     *
     * @param roleId the value
     * @return this builder
     */
    public Builder roleId(String roleId) {
      return set("role_id", nonNull(roleId, "roleId"));
    }

    /**
     * 1 to 1000 rows. Required.
     *
     * @param users the value
     * @return this builder
     */
    public Builder users(List<BulkUserRow> users) {
      return set("users", listOf(users));
    }

    @Override
    public UserBulkCreateParams build() {
      require("users", "users");
      return new UserBulkCreateParams(this);
    }
  }
}
