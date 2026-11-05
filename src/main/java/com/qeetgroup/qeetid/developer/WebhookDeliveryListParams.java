package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;

/** Paging and a filter for a subscription's deliveries. */
public final class WebhookDeliveryListParams extends Params {
  private WebhookDeliveryListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link WebhookDeliveryListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link WebhookDeliveryListParams}. */
  public static final class Builder extends Params.Builder<WebhookDeliveryListParams, Builder> {
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
     * One of {@code "delivered"}, {@code "pending"}, {@code "retrying"}, {@code "dead"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    @Override
    public WebhookDeliveryListParams build() {
      return new WebhookDeliveryListParams(this);
    }
  }
}
