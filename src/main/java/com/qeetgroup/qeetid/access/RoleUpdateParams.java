package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** The changes to a custom role: only the fields set are sent. */
public final class RoleUpdateParams extends Params {
  private RoleUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RoleUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RoleUpdateParams}. */
  public static final class Builder extends Params.Builder<RoleUpdateParams, Builder> {
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
     * Its description.
     *
     * @param description the value
     * @return this builder
     */
    public Builder description(String description) {
      return set("description", nonNull(description, "description"));
    }

    @Override
    public RoleUpdateParams build() {
      return new RoleUpdateParams(this);
    }
  }
}
