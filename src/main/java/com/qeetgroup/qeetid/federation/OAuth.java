package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.databind.JsonNode;
import com.qeetgroup.qeetid.Params;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Json;
import com.qeetgroup.qeetid.internal.Transport;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * The OAuth 2.0 and OpenID endpoints, with Qeet ID as the authorization server: the token grants,
 * introspection, revocation, the device and backchannel (CIBA) flows, and the administration of
 * consent grants, device authorizations and signing keys.
 *
 * <p>The token endpoints answer errors in the RFC 6749 shape; they throw an {@link
 * com.qeetgroup.qeetid.exception.OAuthException} whose code is the RFC {@code error} ({@code
 * invalid_grant}, {@code invalid_client}, ...). Grants are never retried: a grant is single-use, so
 * a retry after a lost answer would be refused. Client credentials travel in the form body ({@code
 * client_secret_post}) and are never logged.
 *
 * <p>Get it from {@code qeetId.oauth()}. Safe for concurrent use.
 */
public final class OAuth {
  private static final String GRANT_TOKEN_EXCHANGE =
      "urn:ietf:params:oauth:grant-type:token-exchange";
  private static final String GRANT_DEVICE_CODE = "urn:ietf:params:oauth:grant-type:device_code";
  private static final String GRANT_CIBA = "urn:openid:params:grant-type:ciba";
  private static final String ACCESS_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:access_token";

  private final Transport http;
  private final OAuthGrants grants;
  private final OAuthDevices devices;
  private final SigningKeys signingKeys;

  /**
   * Used by the client; get this from {@code qeetId.oauth()}.
   *
   * @param http the client's transport
   */
  public OAuth(Transport http) {
    this.http = http;
    this.grants = new OAuthGrants(http);
    this.devices = new OAuthDevices(http);
    this.signingKeys = new SigningKeys(http);
  }

  /**
   * People's consent grants to applications.
   *
   * @return the grants
   */
  public OAuthGrants grants() {
    return grants;
  }

  /**
   * Device-flow authorizations, for review.
   *
   * @return the devices
   */
  public OAuthDevices devices() {
    return devices;
  }

  /**
   * The issuer's signing keys. Rotation is an operator task and isn't part of the API.
   *
   * @return the signing keys
   */
  public SigningKeys signingKeys() {
    return signingKeys;
  }

  /**
   * Gets a machine-to-machine token for a service principal: the {@code client_credentials} grant,
   * at {@code POST /v1/oauth/token}. The token carries every scope the principal holds. A client
   * built with {@code clientCredentials(...)} does this, and caches the token, by itself.
   *
   * @param clientId the principal's client ID
   * @param clientSecret its secret
   * @return the token, with {@code expiresIn} and {@code expiresAt}
   */
  public ClientCredentialsToken clientCredentialsGrant(String clientId, String clientSecret) {
    return clientCredentialsGrant(clientId, clientSecret, RequestOptions.none());
  }

  /**
   * The {@code client_credentials} grant, with per-call settings. See {@link
   * #clientCredentialsGrant(String, String)}.
   *
   * @param clientId the principal's client ID
   * @param clientSecret its secret
   * @param options per-call settings
   * @return the token
   */
  public ClientCredentialsToken clientCredentialsGrant(
      String clientId, String clientSecret, RequestOptions options) {
    Map<String, @Nullable String> form = new LinkedHashMap<>();
    form.put("grant_type", "client_credentials");
    form.put("client_id", Objects.requireNonNull(clientId, "clientId"));
    form.put("client_secret", Objects.requireNonNull(clientSecret, "clientSecret"));
    return grant("/v1/oauth/token", form, ClientCredentialsToken.class, options);
  }

  /**
   * Redeems an authorization code, with its PKCE verifier, for tokens.
   *
   * @param params the code, redirect URI, verifier and client
   * @return the tokens
   */
  public TokenResponse authorizationCodeGrant(AuthorizationCodeGrantParams params) {
    return authorizationCodeGrant(params, RequestOptions.none());
  }

  /**
   * Redeems an authorization code, with per-call settings. See {@link
   * #authorizationCodeGrant(AuthorizationCodeGrantParams)}.
   *
   * @param params the code, redirect URI, verifier and client
   * @param options per-call settings
   * @return the tokens
   */
  public TokenResponse authorizationCodeGrant(
      AuthorizationCodeGrantParams params, RequestOptions options) {
    return grant(
        "/v1/oauth/token-code", form("authorization_code", params), TokenResponse.class, options);
  }

  /**
   * Renews tokens with a refresh token. A refused refresh ends the application's refresh tokens for
   * that person.
   *
   * @param params the refresh token and client
   * @return the tokens
   */
  public TokenResponse refreshTokenGrant(RefreshTokenGrantParams params) {
    return refreshTokenGrant(params, RequestOptions.none());
  }

  /**
   * Renews tokens, with per-call settings. See {@link #refreshTokenGrant(RefreshTokenGrantParams)}.
   *
   * @param params the refresh token and client
   * @param options per-call settings
   * @return the tokens
   */
  public TokenResponse refreshTokenGrant(RefreshTokenGrantParams params, RequestOptions options) {
    return grant(
        "/v1/oauth/token-code", form("refresh_token", params), TokenResponse.class, options);
  }

  /**
   * RFC 8693 token exchange: downscope a token, or get one that acts for its subject. The token
   * types default to access tokens.
   *
   * @param params the subject token, optional actor token, scope and client
   * @return the issued token
   */
  public TokenResponse tokenExchangeGrant(TokenExchangeGrantParams params) {
    return tokenExchangeGrant(params, RequestOptions.none());
  }

  /**
   * RFC 8693 token exchange, with per-call settings. See {@link
   * #tokenExchangeGrant(TokenExchangeGrantParams)}.
   *
   * @param params the subject token, optional actor token, scope and client
   * @param options per-call settings
   * @return the issued token
   */
  public TokenResponse tokenExchangeGrant(TokenExchangeGrantParams params, RequestOptions options) {
    Map<String, @Nullable String> form = form(GRANT_TOKEN_EXCHANGE, params);
    form.putIfAbsent("subject_token_type", ACCESS_TOKEN_TYPE);
    if (form.get("actor_token") != null) {
      form.putIfAbsent("actor_token_type", ACCESS_TOKEN_TYPE);
    }
    return grant("/v1/oauth/token-code", form, TokenResponse.class, options);
  }

  /**
   * Polls for the device flow's tokens. Until the person decides, this throws an {@code
   * OAuthException} with code {@code authorization_pending} (or {@code slow_down}: wait five
   * seconds longer); {@code access_denied} and {@code expired_token} end it.
   *
   * @param params the device code and client
   * @return the tokens, once the person approved
   */
  public TokenResponse deviceCodeGrant(DeviceCodeGrantParams params) {
    return deviceCodeGrant(params, RequestOptions.none());
  }

  /**
   * Polls for the device flow's tokens, with per-call settings. See {@link
   * #deviceCodeGrant(DeviceCodeGrantParams)}.
   *
   * @param params the device code and client
   * @param options per-call settings
   * @return the tokens, once the person approved
   */
  public TokenResponse deviceCodeGrant(DeviceCodeGrantParams params, RequestOptions options) {
    return grant(
        "/v1/oauth/token-code", form(GRANT_DEVICE_CODE, params), TokenResponse.class, options);
  }

  /**
   * Polls for a CIBA request's tokens. Pending and refused requests throw like {@link
   * #deviceCodeGrant(DeviceCodeGrantParams)}.
   *
   * @param params the request id and client
   * @return the tokens, once the person approved
   */
  public TokenResponse cibaGrant(CibaGrantParams params) {
    return cibaGrant(params, RequestOptions.none());
  }

  /**
   * Polls for a CIBA request's tokens, with per-call settings. See {@link
   * #cibaGrant(CibaGrantParams)}.
   *
   * @param params the request id and client
   * @param options per-call settings
   * @return the tokens, once the person approved
   */
  public TokenResponse cibaGrant(CibaGrantParams params, RequestOptions options) {
    return grant("/v1/oauth/token-code", form(GRANT_CIBA, params), TokenResponse.class, options);
  }

  /**
   * Starts the RFC 8628 device flow for an input-constrained device.
   *
   * @param params the client and scope
   * @return what the device shows the person, and how it polls
   */
  public DeviceAuthorization deviceAuthorization(DeviceAuthorizationParams params) {
    return deviceAuthorization(params, RequestOptions.none());
  }

  /**
   * Starts the device flow, with per-call settings. See {@link
   * #deviceAuthorization(DeviceAuthorizationParams)}.
   *
   * @param params the client and scope
   * @param options per-call settings
   * @return what the device shows the person, and how it polls
   */
  public DeviceAuthorization deviceAuthorization(
      DeviceAuthorizationParams params, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post("/v1/oauth/device_authorization")
            .form(form(null, params))
            .unauthenticated();
    return http.execute(request, options, DeviceAuthorization.class);
  }

  /**
   * Starts an OpenID CIBA request (poll mode): the person is asked to approve it on their own
   * device.
   *
   * @param params the confidential client, the person's email and the scope
   * @return the request to poll with
   */
  public BackchannelAuthentication backchannelAuthentication(
      BackchannelAuthenticationParams params) {
    return backchannelAuthentication(params, RequestOptions.none());
  }

  /**
   * Starts an OpenID CIBA request, with per-call settings. See {@link
   * #backchannelAuthentication(BackchannelAuthenticationParams)}.
   *
   * @param params the confidential client, the person's email and the scope
   * @param options per-call settings
   * @return the request to poll with
   */
  public BackchannelAuthentication backchannelAuthentication(
      BackchannelAuthenticationParams params, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post("/v1/oauth/bc-authorize").form(form(null, params)).unauthenticated();
    return http.execute(request, options, BackchannelAuthentication.class);
  }

  /**
   * RFC 7662: whether a token is active, and its claims, authenticated as a confidential
   * application. Introspection is the authoritative answer on revocation: use it where a revoked
   * session must stop working at once, rather than verifying the token offline.
   *
   * @param token the token
   * @param client the confidential application asking
   * @return the answer
   */
  public Introspection introspect(String token, OAuthClient client) {
    return introspect(token, client, null, RequestOptions.none());
  }

  /**
   * RFC 7662 introspection, with a token type hint and per-call settings. See {@link
   * #introspect(String, OAuthClient)}.
   *
   * @param token the token
   * @param client the confidential application asking
   * @param tokenTypeHint {@code access_token} or {@code refresh_token}, or null
   * @param options per-call settings
   * @return the answer
   */
  public Introspection introspect(
      String token, OAuthClient client, @Nullable String tokenTypeHint, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post("/oauth/introspect")
            .form(tokenForm(token, client, tokenTypeHint))
            .unauthenticated()
            .retrySafe();
    JsonNode answer = http.tree(request, options);
    @SuppressWarnings("unchecked")
    Map<String, Object> claims = Json.mapper().convertValue(answer, LinkedHashMap.class);
    return new Introspection(claims);
  }

  /**
   * RFC 7009: revokes a refresh token and its whole grant. An access token can't be revoked (400
   * {@code unsupported_token_type}): it lasts out its short lifetime. Succeeds for an unknown token
   * too.
   *
   * @param token the refresh token
   * @param client the application it was issued to
   */
  public void revoke(String token, OAuthClient client) {
    revoke(token, client, null, RequestOptions.none());
  }

  /**
   * RFC 7009 revocation, with a token type hint and per-call settings. See {@link #revoke(String,
   * OAuthClient)}.
   *
   * @param token the refresh token
   * @param client the application it was issued to
   * @param tokenTypeHint {@code refresh_token}, or null
   * @param options per-call settings
   */
  public void revoke(
      String token, OAuthClient client, @Nullable String tokenTypeHint, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post("/oauth/revoke")
            .form(tokenForm(token, client, tokenTypeHint))
            .unauthenticated()
            .retrySafe();
    http.executeVoid(request, options);
  }

  /**
   * A guard for one protected service (an API or an MCP server): it introspects each token and
   * admits it only when it is active, issued for {@code audience}, and carries the required scopes.
   *
   * @param audience the service's identifier, as the tokens issued for it name it in {@code aud}:
   *     its registered RFC 8707 resource
   * @param client the confidential application the guard introspects as
   * @return the guard
   */
  public TokenGuard tokenGuard(String audience, OAuthClient client) {
    return new TokenGuard(this, audience, client);
  }

  @Override
  public String toString() {
    return "OAuth";
  }

  private <T> T grant(
      String endpoint, Map<String, @Nullable String> form, Class<T> type, RequestOptions options) {
    // A grant is single-use: a retry after a lost answer would be refused, so none is made.
    return http.execute(
        ApiRequest.post(endpoint).form(form).unauthenticated().neverRetry(), options, type);
  }

  private static Map<String, @Nullable String> form(@Nullable String grantType, Params params) {
    Map<String, @Nullable String> form = new LinkedHashMap<>();
    if (grantType != null) {
      form.put("grant_type", grantType);
    }
    params
        .toMap()
        .forEach((name, value) -> form.put(name, value == null ? null : String.valueOf(value)));
    return form;
  }

  private static Map<String, @Nullable String> tokenForm(
      String token, OAuthClient client, @Nullable String tokenTypeHint) {
    Map<String, @Nullable String> form = new LinkedHashMap<>();
    form.put("token", Objects.requireNonNull(token, "token"));
    form.put("token_type_hint", tokenTypeHint);
    form.put("client_id", client.clientId());
    form.put("client_secret", client.clientSecret());
    return form;
  }
}
