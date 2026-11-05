package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.PageParams;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Administers SCIM provisioning: tokens, and what was provisioned. The {@code /scim/v2} protocol
 * itself is called by the identity provider, not by this SDK.
 *
 * <p>Get it from {@code qeetId.scim()}. Safe for concurrent use.
 */
public final class Scim {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.scim()}.
   *
   * @param http the client's transport
   */
  public Scim(Transport http) {
    this.http = http;
  }

  /**
   * Get config.
   *
   * @param tenantId the organisation's id
   * @return the SCIM config
   */
  public ScimConfig getConfig(String tenantId) {
    return getConfig(tenantId, RequestOptions.none());
  }

  /**
   * Get config. With per-call settings: see {@link #getConfig(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the SCIM config
   */
  public ScimConfig getConfig(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/scim", tenantId)),
        options,
        ScimConfig.class);
  }

  /**
   * List tokens.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<ScimToken> listTokens(String tenantId) {
    return listTokens(tenantId, RequestOptions.none());
  }

  /**
   * List tokens. With per-call settings: see {@link #listTokens(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<ScimToken> listTokens(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/scim/tokens", tenantId)),
        ScimToken.class,
        options);
  }

  /**
   * Issues an additional token, shown once.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the SCIM token created
   */
  public ScimTokenCreated createToken(String tenantId, ScimTokenCreateParams params) {
    return createToken(tenantId, params, RequestOptions.none());
  }

  /**
   * Issues an additional token, shown once. With per-call settings: see {@link #createToken(String,
   * ScimTokenCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SCIM token created
   */
  public ScimTokenCreated createToken(
      String tenantId, ScimTokenCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/scim/tokens", tenantId)).json(params),
        options,
        ScimTokenCreated.class);
  }

  /**
   * Replaces a token with a new one, shown once; the old one keeps working for {@code
   * overlap_hours}.
   *
   * @param tenantId the organisation's id
   * @param tokenId the token's id
   * @return the SCIM token rotated
   */
  public ScimTokenRotated rotateToken(String tenantId, String tokenId) {
    return rotateToken(
        tenantId, tokenId, ScimTokenRotateParams.builder().build(), RequestOptions.none());
  }

  /**
   * Replaces a token with a new one, shown once; the old one keeps working for {@code
   * overlap_hours}.
   *
   * @param tenantId the organisation's id
   * @param tokenId the token's id
   * @param params the parameters
   * @return the SCIM token rotated
   */
  public ScimTokenRotated rotateToken(
      String tenantId, String tokenId, ScimTokenRotateParams params) {
    return rotateToken(tenantId, tokenId, params, RequestOptions.none());
  }

  /**
   * Replaces a token with a new one, shown once; the old one keeps working for {@code
   * overlap_hours}. With per-call settings: see {@link #rotateToken(String, String,
   * ScimTokenRotateParams)}.
   *
   * @param tenantId the organisation's id
   * @param tokenId the token's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SCIM token rotated
   */
  public ScimTokenRotated rotateToken(
      String tenantId, String tokenId, ScimTokenRotateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/scim/tokens/{tokenId}/rotate", tenantId, tokenId))
            .json(params),
        options,
        ScimTokenRotated.class);
  }

  /**
   * Revokes one token.
   *
   * @param tenantId the organisation's id
   * @param tokenId the token's id
   */
  public void revokeToken(String tenantId, String tokenId) {
    revokeToken(tenantId, tokenId, RequestOptions.none());
  }

  /**
   * Revokes one token. With per-call settings: see {@link #revokeToken(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param tokenId the token's id
   * @param options per-call settings
   */
  public void revokeToken(String tenantId, String tokenId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/scim/tokens/{tokenId}", tenantId, tokenId)),
        options);
  }

  /**
   * Revokes every working token, which stops provisioning.
   *
   * @param tenantId the organisation's id
   */
  public void revokeAllTokens(String tenantId) {
    revokeAllTokens(tenantId, RequestOptions.none());
  }

  /**
   * Revokes every working token, which stops provisioning. With per-call settings: see {@link
   * #revokeAllTokens(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   */
  public void revokeAllTokens(String tenantId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/tenants/{tenantId}/scim/token", tenantId)), options);
  }

  /**
   * List users.
   *
   * @param tenantId the organisation's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimUser> listUsers(String tenantId) {
    return listUsers(tenantId, PageParams.builder().build(), RequestOptions.none());
  }

  /**
   * List users.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimUser> listUsers(String tenantId, PageParams params) {
    return listUsers(tenantId, params, RequestOptions.none());
  }

  /**
   * List users. With per-call settings: see {@link #listUsers(String, PageParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimUser> listUsers(String tenantId, PageParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/tenants/{tenantId}/scim/users", tenantId), params, ScimUser.class, options);
  }

  /**
   * List groups.
   *
   * @param tenantId the organisation's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimGroup> listGroups(String tenantId) {
    return listGroups(tenantId, PageParams.builder().build(), RequestOptions.none());
  }

  /**
   * List groups.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimGroup> listGroups(String tenantId, PageParams params) {
    return listGroups(tenantId, params, RequestOptions.none());
  }

  /**
   * List groups. With per-call settings: see {@link #listGroups(String, PageParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimGroup> listGroups(String tenantId, PageParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/tenants/{tenantId}/scim/groups", tenantId), params, ScimGroup.class, options);
  }
}
