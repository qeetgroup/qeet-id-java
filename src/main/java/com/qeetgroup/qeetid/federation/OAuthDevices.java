package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Device-flow authorizations, for review.
 *
 * <p>Get it from its parent resource. Safe for concurrent use.
 */
public final class OAuthDevices {
  private final Transport http;

  /**
   * Used by the client; get this from its parent resource.
   *
   * @param http the client's transport
   */
  public OAuthDevices(Transport http) {
    this.http = http;
  }

  /**
   * Lists the OAuth devices.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<OAuthDeviceAuthorization> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the OAuth devices. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<OAuthDeviceAuthorization> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/oauth/devices", tenantId)),
        OAuthDeviceAuthorization.class,
        options);
  }

  /**
   * Revokes a device authorization; a grant it already bought ends too.
   *
   * @param tenantId the organisation's id
   * @param deviceAuthorizationId the device authorization's id
   */
  public void revoke(String tenantId, String deviceAuthorizationId) {
    revoke(tenantId, deviceAuthorizationId, RequestOptions.none());
  }

  /**
   * Revokes a device authorization; a grant it already bought ends too. With per-call settings: see
   * {@link #revoke(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param deviceAuthorizationId the device authorization's id
   * @param options per-call settings
   */
  public void revoke(String tenantId, String deviceAuthorizationId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/oauth/devices/{deviceAuthorizationId}",
                tenantId,
                deviceAuthorizationId)),
        options);
  }
}
