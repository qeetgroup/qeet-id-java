package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** The range of the analytics overview. */
public final class AnalyticsOverviewParams extends Params {
  private AnalyticsOverviewParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AnalyticsOverviewParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AnalyticsOverviewParams}. */
  public static final class Builder extends Params.Builder<AnalyticsOverviewParams, Builder> {
    private Builder() {}

    /**
     * RFC 3339 times bounding the range.
     *
     * @param from the value
     * @return this builder
     */
    public Builder from(String from) {
      return set("from", nonNull(from, "from"));
    }

    /**
     * The to.
     *
     * @param to the value
     * @return this builder
     */
    public Builder to(String to) {
      return set("to", nonNull(to, "to"));
    }

    /**
     * A range of this many days back.
     *
     * @param days the value
     * @return this builder
     */
    public Builder days(int days) {
      return set("days", days);
    }

    /**
     * Only this sign-in method.
     *
     * @param method the value
     * @return this builder
     */
    public Builder method(String method) {
      return set("method", nonNull(method, "method"));
    }

    @Override
    public AnalyticsOverviewParams build() {
      return new AnalyticsOverviewParams(this);
    }
  }
}
