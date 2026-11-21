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
   * @param organizationId the organization's id
   * @return the SCIM config
   */
  public ScimConfig getConfig(String organizationId) {
    return getConfig(organizationId, RequestOptions.none());
  }

  /**
   * Get config. With per-call settings: see {@link #getConfig(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the SCIM config
   */
  public ScimConfig getConfig(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/scim", organizationId)),
        options,
        ScimConfig.class);
  }

  /**
   * List tokens.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<ScimToken> listTokens(String organizationId) {
    return listTokens(organizationId, RequestOptions.none());
  }

  /**
   * List tokens. With per-call settings: see {@link #listTokens(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<ScimToken> listTokens(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/scim/tokens", organizationId)),
        ScimToken.class,
        options);
  }

  /**
   * Issues an additional token, shown once.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the SCIM token created
   */
  public ScimTokenCreated createToken(String organizationId, ScimTokenCreateParams params) {
    return createToken(organizationId, params, RequestOptions.none());
  }

  /**
   * Issues an additional token, shown once. With per-call settings: see {@link #createToken(String,
   * ScimTokenCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SCIM token created
   */
  public ScimTokenCreated createToken(
      String organizationId, ScimTokenCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/scim/tokens", organizationId))
            .json(params),
        options,
        ScimTokenCreated.class);
  }

  /**
   * Replaces a token with a new one, shown once; the old one keeps working for {@code
   * overlap_hours}.
   *
   * @param organizationId the organization's id
   * @param tokenId the token's id
   * @return the SCIM token rotated
   */
  public ScimTokenRotated rotateToken(String organizationId, String tokenId) {
    return rotateToken(
        organizationId, tokenId, ScimTokenRotateParams.builder().build(), RequestOptions.none());
  }

  /**
   * Replaces a token with a new one, shown once; the old one keeps working for {@code
   * overlap_hours}.
   *
   * @param organizationId the organization's id
   * @param tokenId the token's id
   * @param params the parameters
   * @return the SCIM token rotated
   */
  public ScimTokenRotated rotateToken(
      String organizationId, String tokenId, ScimTokenRotateParams params) {
    return rotateToken(organizationId, tokenId, params, RequestOptions.none());
  }

  /**
   * Replaces a token with a new one, shown once; the old one keeps working for {@code
   * overlap_hours}. With per-call settings: see {@link #rotateToken(String, String,
   * ScimTokenRotateParams)}.
   *
   * @param organizationId the organization's id
   * @param tokenId the token's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SCIM token rotated
   */
  public ScimTokenRotated rotateToken(
      String organizationId, String tokenId, ScimTokenRotateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/scim/tokens/{tokenId}/rotate",
                    organizationId,
                    tokenId))
            .json(params),
        options,
        ScimTokenRotated.class);
  }

  /**
   * Revokes one token.
   *
   * @param organizationId the organization's id
   * @param tokenId the token's id
   */
  public void revokeToken(String organizationId, String tokenId) {
    revokeToken(organizationId, tokenId, RequestOptions.none());
  }

  /**
   * Revokes one token. With per-call settings: see {@link #revokeToken(String, String)}.
   *
   * @param organizationId the organization's id
   * @param tokenId the token's id
   * @param options per-call settings
   */
  public void revokeToken(String organizationId, String tokenId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/scim/tokens/{tokenId}",
                organizationId,
                tokenId)),
        options);
  }

  /**
   * Revokes every working token, which stops provisioning.
   *
   * @param organizationId the organization's id
   */
  public void revokeAllTokens(String organizationId) {
    revokeAllTokens(organizationId, RequestOptions.none());
  }

  /**
   * Revokes every working token, which stops provisioning. With per-call settings: see {@link
   * #revokeAllTokens(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   */
  public void revokeAllTokens(String organizationId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/organizations/{organizationId}/scim/token", organizationId)),
        options);
  }

  /**
   * List users.
   *
   * @param organizationId the organization's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimUser> listUsers(String organizationId) {
    return listUsers(organizationId, PageParams.builder().build(), RequestOptions.none());
  }

  /**
   * List users.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimUser> listUsers(String organizationId, PageParams params) {
    return listUsers(organizationId, params, RequestOptions.none());
  }

  /**
   * List users. With per-call settings: see {@link #listUsers(String, PageParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimUser> listUsers(
      String organizationId, PageParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/organizations/{organizationId}/scim/users", organizationId),
        params,
        ScimUser.class,
        options);
  }

  /**
   * List groups.
   *
   * @param organizationId the organization's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimGroup> listGroups(String organizationId) {
    return listGroups(organizationId, PageParams.builder().build(), RequestOptions.none());
  }

  /**
   * List groups.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimGroup> listGroups(String organizationId, PageParams params) {
    return listGroups(organizationId, params, RequestOptions.none());
  }

  /**
   * List groups. With per-call settings: see {@link #listGroups(String, PageParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<ScimGroup> listGroups(
      String organizationId, PageParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/organizations/{organizationId}/scim/groups", organizationId),
        params,
        ScimGroup.class,
        options);
  }
}
