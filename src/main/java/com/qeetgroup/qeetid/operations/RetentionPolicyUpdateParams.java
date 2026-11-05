package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** When soft-deleted users are purged for good. */
public final class RetentionPolicyUpdateParams extends Params {
  private RetentionPolicyUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RetentionPolicyUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RetentionPolicyUpdateParams}. */
  public static final class Builder extends Params.Builder<RetentionPolicyUpdateParams, Builder> {
    private Builder() {}

    /**
     * Whether deleted users enabled. Required.
     *
     * @param deletedUsersEnabled the value
     * @return this builder
     */
    public Builder deletedUsersEnabled(boolean deletedUsersEnabled) {
      return set("deleted_users_enabled", deletedUsersEnabled);
    }

    /**
     * 1 to 3650; clamped. Required.
     *
     * @param deletedUsersDays the value
     * @return this builder
     */
    public Builder deletedUsersDays(int deletedUsersDays) {
      return set("deleted_users_days", deletedUsersDays);
    }

    @Override
    public RetentionPolicyUpdateParams build() {
      require("deleted_users_enabled", "deletedUsersEnabled");
      require("deleted_users_days", "deletedUsersDays");
      return new RetentionPolicyUpdateParams(this);
    }
  }
}
