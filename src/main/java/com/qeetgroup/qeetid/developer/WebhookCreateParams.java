package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;
import java.util.List;

/** A new webhook subscription. */
public final class WebhookCreateParams extends Params {
  private WebhookCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link WebhookCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link WebhookCreateParams}. */
  public static final class Builder extends Params.Builder<WebhookCreateParams, Builder> {
    private Builder() {}

    /**
     * A public https URL. Required.
     *
     * @param url the value
     * @return this builder
     */
    public Builder url(String url) {
      return set("url", nonNull(url, "url"));
    }

    /**
     * At least one event from the catalogue. Required.
     *
     * @param events the value
     * @return this builder
     */
    public Builder events(List<String> events) {
      return set("events", listOf(events));
    }

    /**
     * The organization's id.
     *
     * @param organizationId the value
     * @return this builder
     */
    public Builder organizationId(String organizationId) {
      return set("organization_id", nonNull(organizationId, "organizationId"));
    }

    @Override
    public WebhookCreateParams build() {
      require("url", "url");
      require("events", "events");
      return new WebhookCreateParams(this);
    }
  }
}
