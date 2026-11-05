package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;
import java.util.List;

/** The changes to a webhook subscription: only the fields set are sent. */
public final class WebhookUpdateParams extends Params {
  private WebhookUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link WebhookUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link WebhookUpdateParams}. */
  public static final class Builder extends Params.Builder<WebhookUpdateParams, Builder> {
    private Builder() {}

    /**
     * The URL.
     *
     * @param url the value
     * @return this builder
     */
    public Builder url(String url) {
      return set("url", nonNull(url, "url"));
    }

    /**
     * The events.
     *
     * @param events the value
     * @return this builder
     */
    public Builder events(List<String> events) {
      return set("events", listOf(events));
    }

    @Override
    public WebhookUpdateParams build() {
      return new WebhookUpdateParams(this);
    }
  }
}
