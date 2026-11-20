package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Sign-in analytics.
 *
 * <p>Get it from {@code qeetId.analytics()}. Safe for concurrent use.
 */
public final class Analytics {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.analytics()}.
   *
   * @param http the client's transport
   */
  public Analytics(Transport http) {
    this.http = http;
  }

  /**
   * Get overview.
   *
   * @param organizationId the organization's id
   * @return the analytics overview
   */
  public AnalyticsOverview getOverview(String organizationId) {
    return getOverview(
        organizationId, AnalyticsOverviewParams.builder().build(), RequestOptions.none());
  }

  /**
   * Get overview.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the analytics overview
   */
  public AnalyticsOverview getOverview(String organizationId, AnalyticsOverviewParams params) {
    return getOverview(organizationId, params, RequestOptions.none());
  }

  /**
   * Get overview. With per-call settings: see {@link #getOverview(String,
   * AnalyticsOverviewParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the analytics overview
   */
  public AnalyticsOverview getOverview(
      String organizationId, AnalyticsOverviewParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
                Paths.of("/v1/organizations/{organizationId}/analytics/overview", organizationId))
            .query(params),
        options,
        AnalyticsOverview.class);
  }
}
