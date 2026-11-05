package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages admin portal links.
 *
 * <p>Get it from {@code qeetId.adminPortalLinks()}. Safe for concurrent use.
 */
public final class AdminPortalLinks {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.adminPortalLinks()}.
   *
   * @param http the client's transport
   */
  public AdminPortalLinks(Transport http) {
    this.http = http;
  }

  /**
   * Creates an admin portal link.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the admin portal link created
   */
  public AdminPortalLinkCreated create(String tenantId, AdminPortalLinkCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates an admin portal link. With per-call settings: see {@link #create(String,
   * AdminPortalLinkCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the admin portal link created
   */
  public AdminPortalLinkCreated create(
      String tenantId, AdminPortalLinkCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/admin-portal/links", tenantId))
            .json(params),
        options,
        AdminPortalLinkCreated.class);
  }

  /**
   * Lists the admin portal links.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<AdminPortalLink> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the admin portal links. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<AdminPortalLink> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/admin-portal/links", tenantId)),
        AdminPortalLink.class,
        options);
  }

  /**
   * Revokes an admin portal link.
   *
   * @param tenantId the organisation's id
   * @param linkId the link's id
   */
  public void revoke(String tenantId, String linkId) {
    revoke(tenantId, linkId, RequestOptions.none());
  }

  /**
   * Revokes an admin portal link. With per-call settings: see {@link #revoke(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param linkId the link's id
   * @param options per-call settings
   */
  public void revoke(String tenantId, String linkId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of("/v1/tenants/{tenantId}/admin-portal/links/{linkId}", tenantId, linkId)),
        options);
  }
}
