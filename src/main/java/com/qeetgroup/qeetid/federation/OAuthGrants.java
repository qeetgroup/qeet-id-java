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
   * @param tenantId the organisation's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<OAuthGrant> list(String tenantId) {
    return list(tenantId, OAuthGrantListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists the grants, latest used first.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<OAuthGrant> list(String tenantId, OAuthGrantListParams params) {
    return list(tenantId, params, RequestOptions.none());
  }

  /**
   * Lists the grants, latest used first. With per-call settings: see {@link #list(String,
   * OAuthGrantListParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<OAuthGrant> list(
      String tenantId, OAuthGrantListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/tenants/{tenantId}/oauth/grants", tenantId),
        params,
        OAuthGrant.class,
        options);
  }

  /**
   * Revokes one grant; the application must ask for consent again.
   *
   * @param tenantId the organisation's id
   * @param grantId the grant's id
   */
  public void revoke(String tenantId, String grantId) {
    revoke(tenantId, grantId, RequestOptions.none());
  }

  /**
   * Revokes one grant; the application must ask for consent again. With per-call settings: see
   * {@link #revoke(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param grantId the grant's id
   * @param options per-call settings
   */
  public void revoke(String tenantId, String grantId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/oauth/grants/{grantId}", tenantId, grantId)),
        options);
  }

  /**
   * Revokes several grants by id (up to 100) in one step.
   *
   * @param tenantId the organisation's id
   * @param grantIds the grants
   * @return how many grants were revoked
   */
  public int revokeMany(String tenantId, List<String> grantIds) {
    return revokeMany(tenantId, grantIds, RequestOptions.none());
  }

  /**
   * Revokes several grants, with per-call settings. See {@link #revokeMany(String, List)}.
   *
   * @param tenantId the organisation's id
   * @param grantIds the grants
   * @param options per-call settings
   * @return how many grants were revoked
   */
  public int revokeMany(String tenantId, List<String> grantIds, RequestOptions options) {
    return revoke(tenantId, Bodies.of("ids", List.copyOf(grantIds)), options);
  }

  /**
   * Revokes every grant to one application, so it must ask for consent again.
   *
   * @param tenantId the organisation's id
   * @param clientId the application's client ID
   * @return how many grants were revoked
   */
  public int revokeForApplication(String tenantId, String clientId) {
    return revokeForApplication(tenantId, clientId, RequestOptions.none());
  }

  /**
   * Revokes every grant to one application, with per-call settings. See {@link
   * #revokeForApplication(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param clientId the application's client ID
   * @param options per-call settings
   * @return how many grants were revoked
   */
  public int revokeForApplication(String tenantId, String clientId, RequestOptions options) {
    return revoke(tenantId, Bodies.of("client_id", clientId), options);
  }

  private int revoke(String tenantId, Object body, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/oauth/grants/revoke", tenantId))
            .json(body);
    return http.tree(request, options).path("revoked").asInt();
  }
}
