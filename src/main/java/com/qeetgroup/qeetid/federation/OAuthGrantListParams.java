package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** Paging and a filter for the consent grants. */
public final class OAuthGrantListParams extends Params {
  private OAuthGrantListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link OAuthGrantListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link OAuthGrantListParams}. */
  public static final class Builder extends Params.Builder<OAuthGrantListParams, Builder> {
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
     * Only grants to this application.
     *
     * @param clientId the value
     * @return this builder
     */
    public Builder clientId(String clientId) {
      return set("client_id", nonNull(clientId, "clientId"));
    }

    @Override
    public OAuthGrantListParams build() {
      return new OAuthGrantListParams(this);
    }
  }
}
