package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;

/** How many soft-deleted users to list. */
public final class DeletedUserListParams extends Params {
  private DeletedUserListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link DeletedUserListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link DeletedUserListParams}. */
  public static final class Builder extends Params.Builder<DeletedUserListParams, Builder> {
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

    @Override
    public DeletedUserListParams build() {
      return new DeletedUserListParams(this);
    }
  }
}
