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
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the admin portal link created
   */
  public AdminPortalLinkCreated create(String organizationId, AdminPortalLinkCreateParams params) {
    return create(organizationId, params, RequestOptions.none());
  }

  /**
   * Creates an admin portal link. With per-call settings: see {@link #create(String,
   * AdminPortalLinkCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the admin portal link created
   */
  public AdminPortalLinkCreated create(
      String organizationId, AdminPortalLinkCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/admin-portal/links", organizationId))
            .json(params),
        options,
        AdminPortalLinkCreated.class);
  }

  /**
   * Lists the admin portal links.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<AdminPortalLink> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the admin portal links. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<AdminPortalLink> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/admin-portal/links", organizationId)),
        AdminPortalLink.class,
        options);
  }

  /**
   * Revokes an admin portal link.
   *
   * @param organizationId the organization's id
   * @param linkId the link's id
   */
  public void revoke(String organizationId, String linkId) {
    revoke(organizationId, linkId, RequestOptions.none());
  }

  /**
   * Revokes an admin portal link. With per-call settings: see {@link #revoke(String, String)}.
   *
   * @param organizationId the organization's id
   * @param linkId the link's id
   * @param options per-call settings
   */
  public void revoke(String organizationId, String linkId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/admin-portal/links/{linkId}",
                organizationId,
                linkId)),
        options);
  }
}
