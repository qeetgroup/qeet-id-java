package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;
import java.util.List;

/**
 * Consent grants people gave applications.
 *
 * <p>Get it from its parent resource. Safe for concurrent use.
 */
public final class OAuthGrants {
  private final Transport http;

  /**
   * Used by the client; get this from its parent resource.
   *
   * @param http the client's transport
   */
  public OAuthGrants(Transport http) {
    this.http = http;
  }

  /**
   * Lists the grants, latest used first.
   *
   * @param organizationId the organization's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<OAuthGrant> list(String organizationId) {
    return list(organizationId, OAuthGrantListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists the grants, latest used first.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<OAuthGrant> list(String organizationId, OAuthGrantListParams params) {
    return list(organizationId, params, RequestOptions.none());
  }

  /**
   * Lists the grants, latest used first. With per-call settings: see {@link #list(String,
   * OAuthGrantListParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<OAuthGrant> list(
      String organizationId, OAuthGrantListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/organizations/{organizationId}/oauth/grants", organizationId),
        params,
        OAuthGrant.class,
        options);
  }

  /**
   * Revokes one grant; the application must ask for consent again.
   *
   * @param organizationId the organization's id
   * @param grantId the grant's id
   */
  public void revoke(String organizationId, String grantId) {
    revoke(organizationId, grantId, RequestOptions.none());
  }

  /**
   * Revokes one grant; the application must ask for consent again. With per-call settings: see
   * {@link #revoke(String, String)}.
   *
   * @param organizationId the organization's id
   * @param grantId the grant's id
   * @param options per-call settings
   */
  public void revoke(String organizationId, String grantId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/oauth/grants/{grantId}",
                organizationId,
                grantId)),
        options);
  }

  /**
   * Revokes several grants by id (up to 100) in one step.
   *
   * @param organizationId the organization's id
   * @param grantIds the grants
   * @return how many grants were revoked
   */
  public int revokeMany(String organizationId, List<String> grantIds) {
    return revokeMany(organizationId, grantIds, RequestOptions.none());
  }

  /**
   * Revokes several grants, with per-call settings. See {@link #revokeMany(String, List)}.
   *
   * @param organizationId the organization's id
   * @param grantIds the grants
   * @param options per-call settings
   * @return how many grants were revoked
   */
  public int revokeMany(String organizationId, List<String> grantIds, RequestOptions options) {
    return revoke(organizationId, Bodies.of("ids", List.copyOf(grantIds)), options);
  }

  /**
   * Revokes every grant to one application, so it must ask for consent again.
   *
   * @param organizationId the organization's id
   * @param clientId the application's client ID
   * @return how many grants were revoked
   */
  public int revokeForApplication(String organizationId, String clientId) {
    return revokeForApplication(organizationId, clientId, RequestOptions.none());
  }

  /**
   * Revokes every grant to one application, with per-call settings. See {@link
   * #revokeForApplication(String, String)}.
   *
   * @param organizationId the organization's id
   * @param clientId the application's client ID
   * @param options per-call settings
   * @return how many grants were revoked
   */
  public int revokeForApplication(String organizationId, String clientId, RequestOptions options) {
    return revoke(organizationId, Bodies.of("client_id", clientId), options);
  }

  private int revoke(String organizationId, Object body, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/oauth/grants/revoke", organizationId))
            .json(body);
    return http.tree(request, options).path("revoked").asInt();
  }
}
