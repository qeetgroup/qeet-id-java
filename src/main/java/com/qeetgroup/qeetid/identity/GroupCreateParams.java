package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import org.jspecify.annotations.Nullable;

/** A new group. */
public final class GroupCreateParams extends Params {
  private GroupCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link GroupCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link GroupCreateParams}. */
  public static final class Builder extends Params.Builder<GroupCreateParams, Builder> {
    private Builder() {}

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

    /**
     * One of the organisation's groups; omit or null for a top-level group. Null sends an explicit
     * null, which clears it.
     *
     * @param parentId the value
     * @return this builder
     */
    public Builder parentId(@Nullable String parentId) {
      return set("parent_id", parentId);
    }

    @Override
    public GroupCreateParams build() {
      require("tenant_id", "tenantId");
      require("name", "name");
      return new GroupCreateParams(this);
    }
  }
}
