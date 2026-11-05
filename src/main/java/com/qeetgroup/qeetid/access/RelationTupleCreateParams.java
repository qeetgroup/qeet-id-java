package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** A new relationship tuple: {@code subject} holds {@code relation} on {@code object}. */
public final class RelationTupleCreateParams extends Params {
  private RelationTupleCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RelationTupleCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RelationTupleCreateParams}. */
  public static final class Builder extends Params.Builder<RelationTupleCreateParams, Builder> {
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
     * The subject. Required.
     *
     * @param subject the value
     * @return this builder
     */
    public Builder subject(String subject) {
      return set("subject", nonNull(subject, "subject"));
    }

    @Override
    public RelationTupleCreateParams build() {
      require("object", "object");
      require("relation", "relation");
      require("subject", "subject");
      return new RelationTupleCreateParams(this);
    }
  }
}
