package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** Paging and a filter for the sign-in anomalies. */
public final class AnomalyListParams extends Params {
  private AnomalyListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AnomalyListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AnomalyListParams}. */
  public static final class Builder extends Params.Builder<AnomalyListParams, Builder> {
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
     * One of {@code "open"}, {@code "resolved"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    @Override
    public AnomalyListParams build() {
      return new AnomalyListParams(this);
    }
  }
}
