package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organization's branding. Shown only while the plan includes custom branding.
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
   * @param organizationId the organization's id
   * @return the organization branding
   */
  public OrganizationBranding get(String organizationId) {
    return get(organizationId, RequestOptions.none());
  }

  /**
   * Fetches a branding. With per-call settings: see {@link #get(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the organization branding
   */
  public OrganizationBranding get(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/branding", organizationId)),
        options,
        OrganizationBranding.class);
  }

  /**
   * Replaces the branding.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the organization branding
   */
  public OrganizationBranding update(String organizationId, BrandingUpdateParams params) {
    return update(organizationId, params, RequestOptions.none());
  }

  /**
   * Replaces the branding. With per-call settings: see {@link #update(String,
   * BrandingUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the organization branding
   */
  public OrganizationBranding update(
      String organizationId, BrandingUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT", Paths.of("/v1/organizations/{organizationId}/branding", organizationId))
            .json(params),
        options,
        OrganizationBranding.class);
  }
}
