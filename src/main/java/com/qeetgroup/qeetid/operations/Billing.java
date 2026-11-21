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
 * The organization's Qeet ID billing: subscription, invoices and plan.
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
   * @param organizationId the organization's id
   * @return the subscription
   */
  public Subscription getSubscription(String organizationId) {
    return getSubscription(organizationId, RequestOptions.none());
  }

  /**
   * Get subscription. With per-call settings: see {@link #getSubscription(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the subscription
   */
  public Subscription getSubscription(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/billing/subscription", organizationId)),
        options,
        Subscription.class);
  }

  /**
   * Changes the plan or currency.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the subscription
   */
  public Subscription updateSubscription(String organizationId, SubscriptionUpdateParams params) {
    return updateSubscription(organizationId, params, RequestOptions.none());
  }

  /**
   * Changes the plan or currency. With per-call settings: see {@link #updateSubscription(String,
   * SubscriptionUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the subscription
   */
  public Subscription updateSubscription(
      String organizationId, SubscriptionUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT",
                Paths.of("/v1/organizations/{organizationId}/billing/subscription", organizationId))
            .json(params),
        options,
        Subscription.class);
  }

  /**
   * Ends the subscription at the end of the current period.
   *
   * @param organizationId the organization's id
   * @return whether the subscription ends with the period
   */
  public boolean cancelSubscription(String organizationId) {
    return cancelSubscription(organizationId, RequestOptions.none());
  }

  /**
   * Ends the subscription at the end of the current period. With per-call settings: see {@link
   * #cancelSubscription(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return whether the subscription ends with the period
   */
  public boolean cancelSubscription(String organizationId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/billing/subscription/cancel",
                    organizationId)),
            options)
        .path("cancel_at_period_end")
        .asBoolean();
  }

  /**
   * Starts a checkout for a plan. The API makes it idempotent: an {@code Idempotency-Key} is sent
   * with it, generated per call unless you give one in {@link RequestOptions}, so a retry never
   * acts twice.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the checkout result
   */
  public CheckoutResult checkout(String organizationId, CheckoutParams params) {
    return checkout(organizationId, params, RequestOptions.none());
  }

  /**
   * Starts a checkout for a plan. With per-call settings: see {@link #checkout(String,
   * CheckoutParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the checkout result
   */
  public CheckoutResult checkout(
      String organizationId, CheckoutParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/billing/checkout", organizationId))
            .json(params)
            .idempotent(),
        options,
        CheckoutResult.class);
  }

  /**
   * List invoices.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<Invoice> listInvoices(String organizationId) {
    return listInvoices(organizationId, RequestOptions.none());
  }

  /**
   * List invoices. With per-call settings: see {@link #listInvoices(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Invoice> listInvoices(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/billing/invoices", organizationId)),
        Invoice.class,
        options);
  }

  /**
   * Starts paying an open invoice. The API makes it idempotent: an {@code Idempotency-Key} is sent
   * with it, generated per call unless you give one in {@link RequestOptions}, so a retry never
   * acts twice.
   *
   * @param organizationId the organization's id
   * @param invoiceId the invoice's id
   * @param params the parameters
   * @return the checkout result
   */
  public CheckoutResult payInvoice(
      String organizationId, String invoiceId, InvoicePayParams params) {
    return payInvoice(organizationId, invoiceId, params, RequestOptions.none());
  }

  /**
   * Starts paying an open invoice. With per-call settings: see {@link #payInvoice(String, String,
   * InvoicePayParams)}.
   *
   * @param organizationId the organization's id
   * @param invoiceId the invoice's id
   * @param params the parameters
   * @param options per-call settings
   * @return the checkout result
   */
  public CheckoutResult payInvoice(
      String organizationId, String invoiceId, InvoicePayParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/billing/invoices/{invoiceId}/pay",
                    organizationId,
                    invoiceId))
            .json(params)
            .idempotent(),
        options,
        CheckoutResult.class);
  }

  /**
   * The card or mandate on file.
   *
   * @param organizationId the organization's id
   * @return the payment method, or null when there is none
   */
  public @Nullable PaymentMethod getPaymentMethod(String organizationId) {
    return getPaymentMethod(organizationId, RequestOptions.none());
  }

  /**
   * The card or mandate on file, with per-call settings. See {@link #getPaymentMethod(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the payment method, or null when there is none
   */
  public @Nullable PaymentMethod getPaymentMethod(String organizationId, RequestOptions options) {
    JsonNode method =
        http.tree(
                ApiRequest.get(
                    Paths.of(
                        "/v1/organizations/{organizationId}/billing/payment-method",
                        organizationId)),
                options)
            .get("payment_method");
    return method == null || method.isNull() ? null : Json.convert(method, PaymentMethod.class);
  }

  /**
   * Get profile.
   *
   * @param organizationId the organization's id
   * @return the billing profile
   */
  public BillingProfile getProfile(String organizationId) {
    return getProfile(organizationId, RequestOptions.none());
  }

  /**
   * Get profile. With per-call settings: see {@link #getProfile(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the billing profile
   */
  public BillingProfile getProfile(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/billing/profile", organizationId)),
        options,
        BillingProfile.class);
  }

  /**
   * Update profile.
   *
   * @param organizationId the organization's id
   * @param profile the profile
   * @return the billing profile
   */
  public BillingProfile updateProfile(String organizationId, BillingProfileUpdateParams profile) {
    return updateProfile(organizationId, profile, RequestOptions.none());
  }

  /**
   * Update profile. With per-call settings: see {@link #updateProfile(String,
   * BillingProfileUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param profile the profile
   * @param options per-call settings
   * @return the billing profile
   */
  public BillingProfile updateProfile(
      String organizationId, BillingProfileUpdateParams profile, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT",
                Paths.of("/v1/organizations/{organizationId}/billing/profile", organizationId))
            .json(profile),
        options,
        BillingProfile.class);
  }

  /**
   * The plan's features and limits.
   *
   * @param organizationId the organization's id
   * @return the entitlements
   */
  public Entitlements getEntitlements(String organizationId) {
    return getEntitlements(organizationId, RequestOptions.none());
  }

  /**
   * The plan's features and limits. With per-call settings: see {@link #getEntitlements(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the entitlements
   */
  public Entitlements getEntitlements(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/entitlements", organizationId)),
        options,
        Entitlements.class);
  }

  /**
   * How much of each limit is used.
   *
   * @param organizationId the organization's id
   * @return each limit's usage, by name
   */
  public Map<String, Long> getUsage(String organizationId) {
    return getUsage(organizationId, RequestOptions.none());
  }

  /**
   * How much of each limit is used, with per-call settings. See {@link #getUsage(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return each limit's usage, by name
   */
  public Map<String, Long> getUsage(String organizationId, RequestOptions options) {
    JsonNode usage =
        http.tree(
                ApiRequest.get(
                    Paths.of(
                        "/v1/organizations/{organizationId}/entitlements/usage", organizationId)),
                options)
            .path("usage");
    Map<String, Long> out = new LinkedHashMap<>();
    usage.properties().forEach(entry -> out.put(entry.getKey(), entry.getValue().asLong()));
    return Collections.unmodifiableMap(out);
  }
}
