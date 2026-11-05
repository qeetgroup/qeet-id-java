package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** Paging and a filter for the bot-detection verdicts. */
public final class BotActivityParams extends Params {
  private BotActivityParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link BotActivityParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link BotActivityParams}. */
  public static final class Builder extends Params.Builder<BotActivityParams, Builder> {
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
     * One of {@code "allowed"}, {@code "challenged"}, {@code "blocked"}.
     *
     * @param verdict the value
     * @return this builder
     */
    public Builder verdict(String verdict) {
      return set("verdict", nonNull(verdict, "verdict"));
    }

    @Override
    public BotActivityParams build() {
      return new BotActivityParams(this);
    }
  }
}
