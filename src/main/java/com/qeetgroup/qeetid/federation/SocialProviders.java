package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the API key's organisation's social sign-in providers, and linked social accounts.
 *
 * <p>Get it from {@code qeetId.socialProviders()}. Safe for concurrent use.
 */
public final class SocialProviders {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.socialProviders()}.
   *
   * @param http the client's transport
   */
  public SocialProviders(Transport http) {
    this.http = http;
  }

  /**
   * Lists the social providers.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<SocialProvider> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the social providers. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<SocialProvider> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/social/providers", tenantId)),
        SocialProvider.class,
        options);
  }

  /**
   * Adds a provider, or replaces the settings of one the organisation has.
   *
   * @param params the parameters
   * @return the social provider
   */
  public SocialProvider upsert(SocialProviderUpsertParams params) {
    return upsert(params, RequestOptions.none());
  }

  /**
   * Adds a provider, or replaces the settings of one the organisation has. With per-call settings:
   * see {@link #upsert(SocialProviderUpsertParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the social provider
   */
  public SocialProvider upsert(SocialProviderUpsertParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/social/providers").json(params), options, SocialProvider.class);
  }

  /**
   * Changes only the fields given; {@code enabled} switches the provider on or off.
   *
   * @param provider the provider's name
   * @param params the parameters
   * @return the social provider
   */
  public SocialProvider update(String provider, SocialProviderUpdateParams params) {
    return update(provider, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given; {@code enabled} switches the provider on or off. With per-call
   * settings: see {@link #update(String, SocialProviderUpdateParams)}.
   *
   * @param provider the provider's name
   * @param params the parameters
   * @param options per-call settings
   * @return the social provider
   */
  public SocialProvider update(
      String provider, SocialProviderUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PATCH", Paths.of("/v1/social/providers/{provider}", provider)).json(params),
        options,
        SocialProvider.class);
  }

  /**
   * Removes a provider. Refused with 409 {@code social.provider_in_use} while anyone can sign in
   * only through it.
   *
   * @param provider the provider's name
   */
  public void delete(String provider) {
    delete(provider, RequestOptions.none());
  }

  /**
   * Removes a provider. With per-call settings: see {@link #delete(String)}.
   *
   * @param provider the provider's name
   * @param options per-call settings
   */
  public void delete(String provider, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/social/providers/{provider}", provider)), options);
  }

  /**
   * Unlinks a social account from the person it belongs to.
   *
   * @param identityId the identity's id
   */
  public void unlinkIdentity(String identityId) {
    unlinkIdentity(identityId, RequestOptions.none());
  }

  /**
   * Unlinks a social account from the person it belongs to. With per-call settings: see {@link
   * #unlinkIdentity(String)}.
   *
   * @param identityId the identity's id
   * @param options per-call settings
   */
  public void unlinkIdentity(String identityId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/social/identities/{identityId}", identityId)),
        options);
  }
}
