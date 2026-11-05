package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's rate limits.
 *
 * <p>Get it from {@code qeetId.rateLimits()}. Safe for concurrent use.
 */
public final class RateLimits {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.rateLimits()}.
   *
   * @param http the client's transport
   */
  public RateLimits(Transport http) {
    this.http = http;
  }

  /**
   * Fetches a rate limit.
   *
   * @param tenantId the organisation's id
   * @return the tenant rate limits
   */
  public TenantRateLimits get(String tenantId) {
    return get(tenantId, RequestOptions.none());
  }

  /**
   * Fetches a rate limit. With per-call settings: see {@link #get(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the tenant rate limits
   */
  public TenantRateLimits get(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/rate-limits", tenantId)),
        options,
        TenantRateLimits.class);
  }

  /**
   * Sets the buckets given; the others keep their values.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the tenant rate limits
   */
  public TenantRateLimits update(String tenantId, RateLimitsUpdateParams params) {
    return update(tenantId, params, RequestOptions.none());
  }

  /**
   * Sets the buckets given; the others keep their values. With per-call settings: see {@link
   * #update(String, RateLimitsUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the tenant rate limits
   */
  public TenantRateLimits update(
      String tenantId, RateLimitsUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/rate-limits", tenantId)).json(params),
        options,
        TenantRateLimits.class);
  }

  /**
   * Goes back to the platform defaults.
   *
   * @param tenantId the organisation's id
   */
  public void reset(String tenantId) {
    reset(tenantId, RequestOptions.none());
  }

  /**
   * Goes back to the platform defaults. With per-call settings: see {@link #reset(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   */
  public void reset(String tenantId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/tenants/{tenantId}/rate-limits", tenantId)), options);
  }
}
