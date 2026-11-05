package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** A new custom role. */
public final class RoleCreateParams extends Params {
  private RoleCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RoleCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RoleCreateParams}. */
  public static final class Builder extends Params.Builder<RoleCreateParams, Builder> {
    private Builder() {}

    /**
     * Its name. Required.
     *
     * @param name the value
     * @return this builder
     */
    public Builder name(String name) {
      return set("name", nonNull(name, "name"));
    }

    /**
     * Its description.
     *
     * @param description the value
     * @return this builder
     */
    public Builder description(String description) {
      return set("description", nonNull(description, "description"));
    }

    @Override
    public RoleCreateParams build() {
      require("name", "name");
      return new RoleCreateParams(this);
    }
  }
}
