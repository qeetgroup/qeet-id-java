package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages webhook subscriptions. To verify the deliveries your endpoint receives, use {@link
 * com.qeetgroup.qeetid.webhook.WebhookVerifier}.
 *
 * <p>Get it from {@code qeetId.webhooks()}. Safe for concurrent use.
 */
public final class Webhooks {
  private final Transport http;
  private final WebhookDeliveries deliveries;

  /**
   * Used by the client; get this from {@code qeetId.webhooks()}.
   *
   * @param http the client's transport
   */
  public Webhooks(Transport http) {
    this.http = http;
    this.deliveries = new WebhookDeliveries(http);
  }

  /**
   * A webhook subscription's deliveries.
   *
   * @return the deliveries
   */
  public WebhookDeliveries deliveries() {
    return deliveries;
  }

  /**
   * Subscribes an endpoint. The signing secret is in the answer, once: store it. The API makes it
   * idempotent: an {@code Idempotency-Key} is sent with it, generated per call unless you give one
   * in {@link RequestOptions}, so a retry never acts twice.
   *
   * @param params the parameters
   * @return the webhook subscription
   */
  public WebhookSubscription create(WebhookCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /**
   * Subscribes an endpoint. With per-call settings: see {@link #create(WebhookCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the webhook subscription
   */
  public WebhookSubscription create(WebhookCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/webhooks").json(params).idempotent(),
        options,
        WebhookSubscription.class);
  }

  /**
   * Fetches a webhook.
   *
   * @param webhookId the webhook's id
   * @return the webhook subscription
   */
  public WebhookSubscription get(String webhookId) {
    return get(webhookId, RequestOptions.none());
  }

  /**
   * Fetches a webhook. With per-call settings: see {@link #get(String)}.
   *
   * @param webhookId the webhook's id
   * @param options per-call settings
   * @return the webhook subscription
   */
  public WebhookSubscription get(String webhookId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/webhooks/{webhookId}", webhookId)),
        options,
        WebhookSubscription.class);
  }

  /**
   * Lists the webhooks.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<WebhookSubscription> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the webhooks. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<WebhookSubscription> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/webhooks", organizationId)),
        WebhookSubscription.class,
        options);
  }

  /**
   * Changes the URL, the events, or both.
   *
   * @param organizationId the organization's id
   * @param webhookId the webhook's id
   * @param params the parameters
   * @return the webhook subscription
   */
  public WebhookSubscription update(
      String organizationId, String webhookId, WebhookUpdateParams params) {
    return update(organizationId, webhookId, params, RequestOptions.none());
  }

  /**
   * Changes the URL, the events, or both. With per-call settings: see {@link #update(String,
   * String, WebhookUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param webhookId the webhook's id
   * @param params the parameters
   * @param options per-call settings
   * @return the webhook subscription
   */
  public WebhookSubscription update(
      String organizationId, String webhookId, WebhookUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/organizations/{organizationId}/webhooks/{webhookId}",
                    organizationId,
                    webhookId))
            .json(params),
        options,
        WebhookSubscription.class);
  }

  /**
   * Stops deliveries to the subscription. Undo with {@code enable}.
   *
   * @param webhookId the webhook's id
   */
  public void disable(String webhookId) {
    disable(webhookId, RequestOptions.none());
  }

  /**
   * Stops deliveries to the subscription. With per-call settings: see {@link #disable(String)}.
   *
   * @param webhookId the webhook's id
   * @param options per-call settings
   */
  public void disable(String webhookId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/webhooks/{webhookId}", webhookId)), options);
  }

  /**
   * Turns a disabled subscription back on; deliveries held meanwhile are then sent.
   *
   * @param organizationId the organization's id
   * @param webhookId the webhook's id
   * @return the webhook subscription
   */
  public WebhookSubscription enable(String organizationId, String webhookId) {
    return enable(organizationId, webhookId, RequestOptions.none());
  }

  /**
   * Turns a disabled subscription back on; deliveries held meanwhile are then sent. With per-call
   * settings: see {@link #enable(String, String)}.
   *
   * @param organizationId the organization's id
   * @param webhookId the webhook's id
   * @param options per-call settings
   * @return the webhook subscription
   */
  public WebhookSubscription enable(
      String organizationId, String webhookId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of(
                "/v1/organizations/{organizationId}/webhooks/{webhookId}/enable",
                organizationId,
                webhookId)),
        options,
        WebhookSubscription.class);
  }

  /**
   * Replaces the signing secret and returns the new one, once. The old one keeps signing alongside
   * it until {@code previous_secret_expires_at} (24 hours): verify with both meanwhile.
   *
   * @param organizationId the organization's id
   * @param webhookId the webhook's id
   * @return the webhook secret
   */
  public WebhookSecret rotateSecret(String organizationId, String webhookId) {
    return rotateSecret(organizationId, webhookId, RequestOptions.none());
  }

  /**
   * Replaces the signing secret and returns the new one, once. With per-call settings: see {@link
   * #rotateSecret(String, String)}.
   *
   * @param organizationId the organization's id
   * @param webhookId the webhook's id
   * @param options per-call settings
   * @return the webhook secret
   */
  public WebhookSecret rotateSecret(
      String organizationId, String webhookId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of(
                "/v1/organizations/{organizationId}/webhooks/{webhookId}/rotate-secret",
                organizationId,
                webhookId)),
        options,
        WebhookSecret.class);
  }

  /**
   * Queues a {@code test.ping} event for this subscription alone.
   *
   * @param webhookId the webhook's id
   * @return the webhook test result
   */
  public WebhookTestResult test(String webhookId) {
    return test(webhookId, RequestOptions.none());
  }

  /**
   * Queues a {@code test.ping} event for this subscription alone. With per-call settings: see
   * {@link #test(String)}.
   *
   * @param webhookId the webhook's id
   * @param options per-call settings
   * @return the webhook test result
   */
  public WebhookTestResult test(String webhookId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/webhooks/{webhookId}/test", webhookId)),
        options,
        WebhookTestResult.class);
  }

  /**
   * The catalogue of events a subscription can name.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<WebhookEventType> listEventTypes(String organizationId) {
    return listEventTypes(organizationId, RequestOptions.none());
  }

  /**
   * The catalogue of events a subscription can name. With per-call settings: see {@link
   * #listEventTypes(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<WebhookEventType> listEventTypes(
      String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/webhooks/events", organizationId)),
        WebhookEventType.class,
        options);
  }
}
