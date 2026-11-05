package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** Paging for the people holding a role. */
public final class RoleHoldersParams extends Params {
  private RoleHoldersParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RoleHoldersParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RoleHoldersParams}. */
  public static final class Builder extends Params.Builder<RoleHoldersParams, Builder> {
    private Builder() {}

    /**
     * The limit.
     *
     * @param limit the value
     * @return this builder
     */
    public Builder limit(int limit) {
      return set("limit", limit);
    }

    /**
     * The cursor.
     *
     * @param cursor the value
     * @return this builder
     */
    public Builder cursor(String cursor) {
      return set("cursor", nonNull(cursor, "cursor"));
    }

    @Override
    public RoleHoldersParams build() {
      return new RoleHoldersParams(this);
    }
  }
}
