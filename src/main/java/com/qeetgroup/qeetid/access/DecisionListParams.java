package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** Filters for the decision log. They combine. */
public final class DecisionListParams extends Params {
  private DecisionListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link DecisionListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link DecisionListParams}. */
  public static final class Builder extends Params.Builder<DecisionListParams, Builder> {
    private Builder() {}

    /**
     * How many items per page. Each list documents its own default and maximum.
     *
     * @param limit the value
     * @return this builder
     */
    public Builder limit(int limit) {
      return set("limit", limit);
    }

    /**
     * Where to start: a page's {@code nextCursor}. Omit for the first page.
     *
     * @param cursor the value
     * @return this builder
     */
    public Builder cursor(String cursor) {
      return set("cursor", nonNull(cursor, "cursor"));
    }

    /**
     * The subject.
     *
     * @param subject the value
     * @return this builder
     */
    public Builder subject(String subject) {
      return set("subject", nonNull(subject, "subject"));
    }

    /**
     * One of {@code "check"}, {@code "relation_check"}, {@code "authzen"}, {@code "abac_evaluate"},
     * {@code "abac_draft"}.
     *
     * @param source the value
     * @return this builder
     */
    public Builder source(String source) {
      return set("source", nonNull(source, "source"));
    }

    /**
     * Whether allowed.
     *
     * @param allowed the value
     * @return this builder
     */
    public Builder allowed(boolean allowed) {
      return set("allowed", allowed);
    }

    /**
     * How many days back to look.
     *
     * @param days the value
     * @return this builder
     */
    public Builder days(int days) {
      return set("days", days);
    }

    @Override
    public DecisionListParams build() {
      return new DecisionListParams(this);
    }
  }
}
