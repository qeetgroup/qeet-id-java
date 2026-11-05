package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Json;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * The organisation's Qeet ID billing: subscription, invoices and plan.
 *
 * <p>Get it from {@code qeetId.billing()}. Safe for concurrent use.
 */
public final class Billing {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.billing()}.
   *
   * @param http the client's transport
   */
  public Billing(Transport http) {
    this.http = http;
  }

  /**
   * Get subscription.
   *
   * @param tenantId the organisation's id
   * @return the subscription
   */
  public Subscription getSubscription(String tenantId) {
    return getSubscription(tenantId, RequestOptions.none());
  }

  /**
   * Get subscription. With per-call settings: see {@link #getSubscription(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the subscription
   */
  public Subscription getSubscription(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/billing/subscription", tenantId)),
        options,
        Subscription.class);
  }

  /**
   * Changes the plan or currency.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the subscription
   */
  public Subscription updateSubscription(String tenantId, SubscriptionUpdateParams params) {
    return updateSubscription(tenantId, params, RequestOptions.none());
  }

  /**
   * Changes the plan or currency. With per-call settings: see {@link #updateSubscription(String,
   * SubscriptionUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the subscription
   */
  public Subscription updateSubscription(
      String tenantId, SubscriptionUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/billing/subscription", tenantId))
            .json(params),
        options,
        Subscription.class);
  }

  /**
   * Ends the subscription at the end of the current period.
   *
   * @param tenantId the organisation's id
   * @return whether the subscription ends with the period
   */
  public boolean cancelSubscription(String tenantId) {
    return cancelSubscription(tenantId, RequestOptions.none());
  }

  /**
   * Ends the subscription at the end of the current period. With per-call settings: see {@link
   * #cancelSubscription(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return whether the subscription ends with the period
   */
  public boolean cancelSubscription(String tenantId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/billing/subscription/cancel", tenantId)),
            options)
        .path("cancel_at_period_end")
        .asBoolean();
  }

  /**
   * Starts a checkout for a plan. The API makes it idempotent: an {@code Idempotency-Key} is sent
   * with it, generated per call unless you give one in {@link RequestOptions}, so a retry never
   * acts twice.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the checkout result
   */
  public CheckoutResult checkout(String tenantId, CheckoutParams params) {
    return checkout(tenantId, params, RequestOptions.none());
  }

  /**
   * Starts a checkout for a plan. With per-call settings: see {@link #checkout(String,
   * CheckoutParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the checkout result
   */
  public CheckoutResult checkout(String tenantId, CheckoutParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/billing/checkout", tenantId))
            .json(params)
            .idempotent(),
        options,
        CheckoutResult.class);
  }

  /**
   * List invoices.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<Invoice> listInvoices(String tenantId) {
    return listInvoices(tenantId, RequestOptions.none());
  }

  /**
   * List invoices. With per-call settings: see {@link #listInvoices(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Invoice> listInvoices(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/billing/invoices", tenantId)),
        Invoice.class,
        options);
  }

  /**
   * Starts paying an open invoice. The API makes it idempotent: an {@code Idempotency-Key} is sent
   * with it, generated per call unless you give one in {@link RequestOptions}, so a retry never
   * acts twice.
   *
   * @param tenantId the organisation's id
   * @param invoiceId the invoice's id
   * @param params the parameters
   * @return the checkout result
   */
  public CheckoutResult payInvoice(String tenantId, String invoiceId, InvoicePayParams params) {
    return payInvoice(tenantId, invoiceId, params, RequestOptions.none());
  }

  /**
   * Starts paying an open invoice. With per-call settings: see {@link #payInvoice(String, String,
   * InvoicePayParams)}.
   *
   * @param tenantId the organisation's id
   * @param invoiceId the invoice's id
   * @param params the parameters
   * @param options per-call settings
   * @return the checkout result
   */
  public CheckoutResult payInvoice(
      String tenantId, String invoiceId, InvoicePayParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/billing/invoices/{invoiceId}/pay", tenantId, invoiceId))
            .json(params)
            .idempotent(),
        options,
        CheckoutResult.class);
  }

  /**
   * The card or mandate on file.
   *
   * @param tenantId the organisation's id
   * @return the payment method, or null when there is none
   */
  public @Nullable PaymentMethod getPaymentMethod(String tenantId) {
    return getPaymentMethod(tenantId, RequestOptions.none());
  }

  /**
   * The card or mandate on file, with per-call settings. See {@link #getPaymentMethod(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the payment method, or null when there is none
   */
  public @Nullable PaymentMethod getPaymentMethod(String tenantId, RequestOptions options) {
    JsonNode method =
        http.tree(
                ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/billing/payment-method", tenantId)),
                options)
            .get("payment_method");
    return method == null || method.isNull() ? null : Json.convert(method, PaymentMethod.class);
  }

  /**
   * Get profile.
   *
   * @param tenantId the organisation's id
   * @return the billing profile
   */
  public BillingProfile getProfile(String tenantId) {
    return getProfile(tenantId, RequestOptions.none());
  }

  /**
   * Get profile. With per-call settings: see {@link #getProfile(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the billing profile
   */
  public BillingProfile getProfile(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/billing/profile", tenantId)),
        options,
        BillingProfile.class);
  }

  /**
   * Update profile.
   *
   * @param tenantId the organisation's id
   * @param profile the profile
   * @return the billing profile
   */
  public BillingProfile updateProfile(String tenantId, BillingProfileUpdateParams profile) {
    return updateProfile(tenantId, profile, RequestOptions.none());
  }

  /**
   * Update profile. With per-call settings: see {@link #updateProfile(String,
   * BillingProfileUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param profile the profile
   * @param options per-call settings
   * @return the billing profile
   */
  public BillingProfile updateProfile(
      String tenantId, BillingProfileUpdateParams profile, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/billing/profile", tenantId))
            .json(profile),
        options,
        BillingProfile.class);
  }

  /**
   * The plan's features and limits.
   *
   * @param tenantId the organisation's id
   * @return the entitlements
   */
  public Entitlements getEntitlements(String tenantId) {
    return getEntitlements(tenantId, RequestOptions.none());
  }

  /**
   * The plan's features and limits. With per-call settings: see {@link #getEntitlements(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the entitlements
   */
  public Entitlements getEntitlements(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/entitlements", tenantId)),
        options,
        Entitlements.class);
  }

  /**
   * How much of each limit is used.
   *
   * @param tenantId the organisation's id
   * @return each limit's usage, by name
   */
  public Map<String, Long> getUsage(String tenantId) {
    return getUsage(tenantId, RequestOptions.none());
  }

  /**
   * How much of each limit is used, with per-call settings. See {@link #getUsage(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return each limit's usage, by name
   */
  public Map<String, Long> getUsage(String tenantId, RequestOptions options) {
    JsonNode usage =
        http.tree(
                ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/entitlements/usage", tenantId)),
                options)
            .path("usage");
    Map<String, Long> out = new LinkedHashMap<>();
    usage.properties().forEach(entry -> out.put(entry.getKey(), entry.getValue().asLong()));
    return Collections.unmodifiableMap(out);
  }
}
