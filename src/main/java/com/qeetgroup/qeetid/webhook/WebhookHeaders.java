package com.qeetgroup.qeetid.webhook;

/** The headers of a webhook delivery. */
public final class WebhookHeaders {
  /** {@code sha256=<hex>}; during a secret rotation two such values, comma-separated. */
  public static final String SIGNATURE = "X-Qeet-Signature";

  /** Unix seconds when the delivery was signed. */
  public static final String TIMESTAMP = "X-Qeet-Timestamp";

  /** The event type. Not signed: read {@link WebhookEvent#type()} instead. */
  public static final String EVENT = "X-Qeet-Event";

  /** This delivery attempt's id. Not signed. Deduplicate on {@link WebhookEvent#id()}. */
  public static final String DELIVERY_ID = "X-Qeet-Delivery-ID";

  private WebhookHeaders() {}
}
