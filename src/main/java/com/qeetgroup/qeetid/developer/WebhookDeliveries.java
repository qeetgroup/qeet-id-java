package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * A webhook subscription's deliveries.
 *
 * <p>Get it from its parent resource. Safe for concurrent use.
 */
public final class WebhookDeliveries {
  private final Transport http;

  /**
   * Used by the client; get this from its parent resource.
   *
   * @param http the client's transport
   */
  public WebhookDeliveries(Transport http) {
    this.http = http;
  }

  /**
   * Lists a subscription's deliveries, newest first.
   *
   * @param webhookId the webhook's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<WebhookDelivery> list(String webhookId) {
    return list(webhookId, WebhookDeliveryListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists a subscription's deliveries, newest first.
   *
   * @param webhookId the webhook's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<WebhookDelivery> list(String webhookId, WebhookDeliveryListParams params) {
    return list(webhookId, params, RequestOptions.none());
  }

  /**
   * Lists a subscription's deliveries, newest first. With per-call settings: see {@link
   * #list(String, WebhookDeliveryListParams)}.
   *
   * @param webhookId the webhook's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<WebhookDelivery> list(
      String webhookId, WebhookDeliveryListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/webhooks/{webhookId}/deliveries", webhookId),
        params,
        WebhookDelivery.class,
        options);
  }

  /**
   * Sends a delivery again now.
   *
   * @param webhookId the webhook's id
   * @param deliveryId the delivery's id
   */
  public void retry(String webhookId, String deliveryId) {
    retry(webhookId, deliveryId, RequestOptions.none());
  }

  /**
   * Sends a delivery again now. With per-call settings: see {@link #retry(String, String)}.
   *
   * @param webhookId the webhook's id
   * @param deliveryId the delivery's id
   * @param options per-call settings
   */
  public void retry(String webhookId, String deliveryId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/webhooks/{webhookId}/deliveries/{deliveryId}/retry", webhookId, deliveryId)),
        options);
  }

  /**
   * Sends again, each with fresh attempts, up to 1,000 of the subscription's given-up deliveries.
   *
   * @param tenantId the organisation's id
   * @param webhookId the webhook's id
   * @return how many deliveries were queued again
   */
  public int retryFailed(String tenantId, String webhookId) {
    return retryFailed(tenantId, webhookId, RequestOptions.none());
  }

  /**
   * Sends again, each with fresh attempts, up to 1,000 of the subscription's given-up deliveries.
   * With per-call settings: see {@link #retryFailed(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param webhookId the webhook's id
   * @param options per-call settings
   * @return how many deliveries were queued again
   */
  public int retryFailed(String tenantId, String webhookId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/webhooks/{webhookId}/deliveries/retry",
                    tenantId,
                    webhookId)),
            options)
        .path("requeued")
        .asInt();
  }

  /**
   * Every attempt to send a delivery, oldest first.
   *
   * @param tenantId the organisation's id
   * @param webhookId the webhook's id
   * @param deliveryId the delivery's id
   * @return the list
   */
  public ListResult<WebhookDeliveryAttempt> listAttempts(
      String tenantId, String webhookId, String deliveryId) {
    return listAttempts(tenantId, webhookId, deliveryId, RequestOptions.none());
  }

  /**
   * Every attempt to send a delivery, oldest first. With per-call settings: see {@link
   * #listAttempts(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param webhookId the webhook's id
   * @param deliveryId the delivery's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<WebhookDeliveryAttempt> listAttempts(
      String tenantId, String webhookId, String deliveryId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of(
                "/v1/tenants/{tenantId}/webhooks/{webhookId}/deliveries/{deliveryId}/attempts",
                tenantId,
                webhookId,
                deliveryId)),
        WebhookDeliveryAttempt.class,
        options);
  }
}
