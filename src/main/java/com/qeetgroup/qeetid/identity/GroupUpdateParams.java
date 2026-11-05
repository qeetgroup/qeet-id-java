package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import org.jspecify.annotations.Nullable;

/** Changes only the fields given; {@code parent_id: null} makes the group top-level. */
public final class GroupUpdateParams extends Params {
  private GroupUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link GroupUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link GroupUpdateParams}. */
  public static final class Builder extends Params.Builder<GroupUpdateParams, Builder> {
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

    /**
     * The parent's id. Null sends an explicit null, which clears it.
     *
     * @param parentId the value
     * @return this builder
     */
    public Builder parentId(@Nullable String parentId) {
      return set("parent_id", parentId);
    }

    @Override
    public GroupUpdateParams build() {
      return new GroupUpdateParams(this);
    }
  }
}
