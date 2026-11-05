package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** A relationship check: does {@code user_id} hold {@code relation} on {@code object}? */
public final class RelationCheckParams extends Params {
  private RelationCheckParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RelationCheckParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RelationCheckParams}. */
  public static final class Builder extends Params.Builder<RelationCheckParams, Builder> {
    private Builder() {}

    /**
     * The object. Required.
     *
     * @param object the value
     * @return this builder
     */
    public Builder object(String object) {
      return set("object", nonNull(object, "object"));
    }

    /**
     * The relation. Required.
     *
     * @param relation the value
     * @return this builder
     */
    public Builder relation(String relation) {
      return set("relation", nonNull(relation, "relation"));
    }

    /**
     * The user's id. Required.
     *
     * @param userId the value
     * @return this builder
     */
    public Builder userId(String userId) {
      return set("user_id", nonNull(userId, "userId"));
    }

    @Override
    public RelationCheckParams build() {
      require("object", "object");
      require("relation", "relation");
      require("user_id", "userId");
      return new RelationCheckParams(this);
    }
  }
}
