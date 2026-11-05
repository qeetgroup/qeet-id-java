package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;

/** Filters for the groups. They combine. */
public final class GroupListParams extends Params {
  private GroupListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link GroupListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link GroupListParams}. */
  public static final class Builder extends Params.Builder<GroupListParams, Builder> {
    private Builder() {}

    /**
     * Only groups granted this role (needs {@code role.read}).
     *
     * @param role the value
     * @return this builder
     */
    public Builder role(String role) {
      return set("role", nonNull(role, "role"));
    }

    /**
     * {@code "some"}: groups granted at least one role; {@code "none"}: groups granted none. One of
     * {@code "some"}, {@code "none"}.
     *
     * @param grants the value
     * @return this builder
     */
    public Builder grants(String grants) {
      return set("grants", nonNull(grants, "grants"));
    }

    @Override
    public GroupListParams build() {
      return new GroupListParams(this);
    }
  }
}
