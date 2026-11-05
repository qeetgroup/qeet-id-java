package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * The organisation's registered RFC 8707 resources.
 *
 * <p>Get it from its parent resource. Safe for concurrent use.
 */
public final class OAuthResources {
  private final Transport http;

  /**
   * Used by the client; get this from its parent resource.
   *
   * @param http the client's transport
   */
  public OAuthResources(Transport http) {
    this.http = http;
  }

  /**
   * Lists the OAuth resources.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<OAuthResource> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the OAuth resources. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<OAuthResource> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/oidc/resources", tenantId)),
        OAuthResource.class,
        options);
  }

  /**
   * Creates an OAuth resource.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the OAuth resource
   */
  public OAuthResource create(String tenantId, OAuthResourceCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates an OAuth resource. With per-call settings: see {@link #create(String,
   * OAuthResourceCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the OAuth resource
   */
  public OAuthResource create(
      String tenantId, OAuthResourceCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/oidc/resources", tenantId)).json(params),
        options,
        OAuthResource.class);
  }

  /**
   * No new token is issued for it; tokens already issued last out their short lifetime.
   *
   * @param tenantId the organisation's id
   * @param resourceId the resource's id
   */
  public void delete(String tenantId, String resourceId) {
    delete(tenantId, resourceId, RequestOptions.none());
  }

  /**
   * No new token is issued for it; tokens already issued last out their short lifetime. With
   * per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param resourceId the resource's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String resourceId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of("/v1/tenants/{tenantId}/oidc/resources/{resourceId}", tenantId, resourceId)),
        options);
  }
}
