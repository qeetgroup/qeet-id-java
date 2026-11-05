package com.qeetgroup.qeetid;

/**
 * Paging for a cursor-paginated list with no filters of its own: how many items per page, and where
 * to start.
 */
public final class PageParams extends Params {
  private PageParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link PageParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link PageParams}. */
  public static final class Builder extends Params.Builder<PageParams, Builder> {
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

    @Override
    public PageParams build() {
      return new PageParams(this);
    }
  }
}
