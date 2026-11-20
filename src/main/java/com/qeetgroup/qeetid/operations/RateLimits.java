package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organization's rate limits.
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
   * @param organizationId the organization's id
   * @return the organization rate limits
   */
  public OrganizationRateLimits get(String organizationId) {
    return get(organizationId, RequestOptions.none());
  }

  /**
   * Fetches a rate limit. With per-call settings: see {@link #get(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the organization rate limits
   */
  public OrganizationRateLimits get(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/rate-limits", organizationId)),
        options,
        OrganizationRateLimits.class);
  }

  /**
   * Sets the buckets given; the others keep their values.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the organization rate limits
   */
  public OrganizationRateLimits update(String organizationId, RateLimitsUpdateParams params) {
    return update(organizationId, params, RequestOptions.none());
  }

  /**
   * Sets the buckets given; the others keep their values. With per-call settings: see {@link
   * #update(String, RateLimitsUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the organization rate limits
   */
  public OrganizationRateLimits update(
      String organizationId, RateLimitsUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT", Paths.of("/v1/organizations/{organizationId}/rate-limits", organizationId))
            .json(params),
        options,
        OrganizationRateLimits.class);
  }

  /**
   * Goes back to the platform defaults.
   *
   * @param organizationId the organization's id
   */
  public void reset(String organizationId) {
    reset(organizationId, RequestOptions.none());
  }

  /**
   * Goes back to the platform defaults. With per-call settings: see {@link #reset(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   */
  public void reset(String organizationId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/organizations/{organizationId}/rate-limits", organizationId)),
        options);
  }
}
