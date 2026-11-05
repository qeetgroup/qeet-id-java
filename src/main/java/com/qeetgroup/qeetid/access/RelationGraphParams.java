package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** Where to start a relationship graph, and how far to follow it. */
public final class RelationGraphParams extends Params {
  private RelationGraphParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RelationGraphParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RelationGraphParams}. */
  public static final class Builder extends Params.Builder<RelationGraphParams, Builder> {
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
     * Hops to follow, 1 to 10.
     *
     * @param depth the value
     * @return this builder
     */
    public Builder depth(int depth) {
      return set("depth", depth);
    }

    @Override
    public RelationGraphParams build() {
      require("object", "object");
      require("relation", "relation");
      return new RelationGraphParams(this);
    }
  }
}
