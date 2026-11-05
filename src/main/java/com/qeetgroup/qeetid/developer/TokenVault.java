package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Third-party OAuth tokens held for your users (Slack, GitHub, Google, or any OAuth 2.0 provider an
 * administrator registers). People connect their accounts in the browser; this SDK manages the
 * providers and fetches live access tokens.
 *
 * <p>Get it from {@code qeetId.tokenVault()}. Safe for concurrent use.
 */
public final class TokenVault {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.tokenVault()}.
   *
   * @param http the client's transport
   */
  public TokenVault(Transport http) {
    this.http = http;
  }

  /**
   * Registers a provider, or replaces one's settings.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the token vault provider
   */
  public TokenVaultProvider createProvider(String tenantId, TokenVaultProviderCreateParams params) {
    return createProvider(tenantId, params, RequestOptions.none());
  }

  /**
   * Registers a provider, or replaces one's settings. With per-call settings: see {@link
   * #createProvider(String, TokenVaultProviderCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the token vault provider
   */
  public TokenVaultProvider createProvider(
      String tenantId, TokenVaultProviderCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/vault/tokens/providers", tenantId))
            .json(params),
        options,
        TokenVaultProvider.class);
  }

  /**
   * List providers.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<TokenVaultProvider> listProviders(String tenantId) {
    return listProviders(tenantId, RequestOptions.none());
  }

  /**
   * List providers. With per-call settings: see {@link #listProviders(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<TokenVaultProvider> listProviders(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/vault/tokens/providers", tenantId)),
        TokenVaultProvider.class,
        options);
  }

  /**
   * Delete provider.
   *
   * @param tenantId the organisation's id
   * @param provider the provider's name
   */
  public void deleteProvider(String tenantId, String provider) {
    deleteProvider(tenantId, provider, RequestOptions.none());
  }

  /**
   * Delete provider. With per-call settings: see {@link #deleteProvider(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param provider the provider's name
   * @param options per-call settings
   */
  public void deleteProvider(String tenantId, String provider, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/vault/tokens/providers/{provider}", tenantId, provider)),
        options);
  }

  /**
   * Returns a live access token for {@code provider}, refreshed first when it has expired. Needs
   * the {@code vault:read} scope.
   *
   * @param provider the provider's name
   * @return the access token
   * @deprecated This hands your code the raw third-party token, which then tends to be stored,
   *     logged or put in a model's prompt, outside the vault's controls; an AI agent must never
   *     receive it. Keep it in memory for one request only. It will be replaced by a brokered call,
   *     where Qeet ID makes the provider request for you, once the API offers one.
   */
  @Deprecated
  public String getAccessToken(String provider) {
    return getAccessToken(provider, RequestOptions.none());
  }

  /**
   * Returns a live access token for {@code provider}, with per-call settings. See {@link
   * #getAccessToken(String)}.
   *
   * @param provider the provider's name
   * @param options per-call settings
   * @return the access token
   * @deprecated See {@link #getAccessToken(String)}.
   */
  @Deprecated
  public String getAccessToken(String provider, RequestOptions options) {
    return http.tree(
            ApiRequest.get(Paths.of("/v1/vault/tokens/{provider}/access-token", provider)), options)
        .path("access_token")
        .asText();
  }
}
