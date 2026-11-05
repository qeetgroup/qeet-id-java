package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's branding. Shown only while the plan includes custom branding.
 *
 * <p>Get it from {@code qeetId.branding()}. Safe for concurrent use.
 */
public final class Branding {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.branding()}.
   *
   * @param http the client's transport
   */
  public Branding(Transport http) {
    this.http = http;
  }

  /**
   * Fetches a branding.
   *
   * @param tenantId the organisation's id
   * @return the tenant branding
   */
  public TenantBranding get(String tenantId) {
    return get(tenantId, RequestOptions.none());
  }

  /**
   * Fetches a branding. With per-call settings: see {@link #get(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the tenant branding
   */
  public TenantBranding get(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/branding", tenantId)),
        options,
        TenantBranding.class);
  }

  /**
   * Replaces the branding.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the tenant branding
   */
  public TenantBranding update(String tenantId, BrandingUpdateParams params) {
    return update(tenantId, params, RequestOptions.none());
  }

  /**
   * Replaces the branding. With per-call settings: see {@link #update(String,
   * BrandingUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the tenant branding
   */
  public TenantBranding update(
      String tenantId, BrandingUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/branding", tenantId)).json(params),
        options,
        TenantBranding.class);
  }
}
