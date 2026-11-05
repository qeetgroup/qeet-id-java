package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.databind.JsonNode;
import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Json;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages directory connections, and signs people in with their directory password.
 *
 * <p>Get it from {@code qeetId.ldapConnections()}. Safe for concurrent use.
 */
public final class LdapConnections {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.ldapConnections()}.
   *
   * @param http the client's transport
   */
  public LdapConnections(Transport http) {
    this.http = http;
  }

  /**
   * Creates an LDAP connection.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the LDAP connection
   */
  public LdapConnection create(String tenantId, LdapConnectionCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates an LDAP connection. With per-call settings: see {@link #create(String,
   * LdapConnectionCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the LDAP connection
   */
  public LdapConnection create(
      String tenantId, LdapConnectionCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/ldap", tenantId)).json(params),
        options,
        LdapConnection.class);
  }

  /**
   * Fetches an LDAP connection.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @return the LDAP connection
   */
  public LdapConnection get(String tenantId, String connectionId) {
    return get(tenantId, connectionId, RequestOptions.none());
  }

  /**
   * Fetches an LDAP connection. With per-call settings: see {@link #get(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return the LDAP connection
   */
  public LdapConnection get(String tenantId, String connectionId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/tenants/{tenantId}/ldap/{connectionId}", tenantId, connectionId)),
        options,
        LdapConnection.class);
  }

  /**
   * Lists the LDAP connections.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<LdapConnection> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the LDAP connections. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<LdapConnection> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/ldap", tenantId)),
        LdapConnection.class,
        options);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @return the LDAP connection
   */
  public LdapConnection update(
      String tenantId, String connectionId, LdapConnectionUpdateParams params) {
    return update(tenantId, connectionId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * LdapConnectionUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @param options per-call settings
   * @return the LDAP connection
   */
  public LdapConnection update(
      String tenantId,
      String connectionId,
      LdapConnectionUpdateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of("/v1/tenants/{tenantId}/ldap/{connectionId}", tenantId, connectionId))
            .json(params),
        options,
        LdapConnection.class);
  }

  /**
   * Deletes an LDAP connection.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   */
  public void delete(String tenantId, String connectionId) {
    delete(tenantId, connectionId, RequestOptions.none());
  }

  /**
   * Deletes an LDAP connection. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String connectionId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of("/v1/tenants/{tenantId}/ldap/{connectionId}", tenantId, connectionId)),
        options);
  }

  /**
   * Binds to the directory with the stored credentials. A failure throws.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @return whether the bind succeeded
   */
  public boolean test(String tenantId, String connectionId) {
    return test(tenantId, connectionId, RequestOptions.none());
  }

  /**
   * Binds to the directory with the stored credentials. With per-call settings: see {@link
   * #test(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return whether the bind succeeded
   */
  public boolean test(String tenantId, String connectionId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                    Paths.of(
                        "/v1/tenants/{tenantId}/ldap/{connectionId}/test", tenantId, connectionId))
                .retrySafe(),
            options)
        .path("ok")
        .asBoolean();
  }

  /**
   * List group mappings.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @return the list
   */
  public ListResult<SsoGroupMapping> listGroupMappings(String tenantId, String connectionId) {
    return listGroupMappings(tenantId, connectionId, RequestOptions.none());
  }

  /**
   * List group mappings. With per-call settings: see {@link #listGroupMappings(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<SsoGroupMapping> listGroupMappings(
      String tenantId, String connectionId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of(
                "/v1/tenants/{tenantId}/ldap/{connectionId}/group-mappings",
                tenantId,
                connectionId)),
        SsoGroupMapping.class,
        options);
  }

  /**
   * Create group mapping.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @return the SSO group mapping
   */
  public SsoGroupMapping createGroupMapping(
      String tenantId, String connectionId, SsoGroupMappingCreateParams params) {
    return createGroupMapping(tenantId, connectionId, params, RequestOptions.none());
  }

  /**
   * Create group mapping. With per-call settings: see {@link #createGroupMapping(String, String,
   * SsoGroupMappingCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SSO group mapping
   */
  public SsoGroupMapping createGroupMapping(
      String tenantId,
      String connectionId,
      SsoGroupMappingCreateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/ldap/{connectionId}/group-mappings",
                    tenantId,
                    connectionId))
            .json(params),
        options,
        SsoGroupMapping.class);
  }

  /**
   * Delete group mapping.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param mappingId the mapping's id
   */
  public void deleteGroupMapping(String tenantId, String connectionId, String mappingId) {
    deleteGroupMapping(tenantId, connectionId, mappingId, RequestOptions.none());
  }

  /**
   * Delete group mapping. With per-call settings: see {@link #deleteGroupMapping(String, String,
   * String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param mappingId the mapping's id
   * @param options per-call settings
   */
  public void deleteGroupMapping(
      String tenantId, String connectionId, String mappingId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/ldap/{connectionId}/group-mappings/{mappingId}",
                tenantId,
                connectionId,
                mappingId)),
        options);
  }

  /**
   * Signs a person in with their directory username and password, under the organisation's lockout,
   * network, risk and second-factor rules. The result is tokens, or a challenge to complete at
   * {@code POST /v1/auth/mfa}:
   *
   * <pre>{@code
   * LdapAuthenticationResult result = qeetId.ldapConnections().authenticate(connectionId, username, password);
   * if (result instanceof LdapAuthenticationResult.Challenge challenge) {
   *   return askForSecondFactor(challenge.challenge().mfaToken());
   * }
   * useTokens(((LdapAuthenticationResult.Tokens) result).tokens());
   * }</pre>
   *
   * <p>Sends no API key: the endpoint is public. Never retried, as each failed attempt counts
   * towards the lockout.
   *
   * @param connectionId the connection's id
   * @param username the person's directory username
   * @param password their directory password
   * @return the tokens, or a second-factor challenge
   */
  public LdapAuthenticationResult authenticate(
      String connectionId, String username, String password) {
    return authenticate(connectionId, username, password, RequestOptions.none());
  }

  /**
   * Signs a person in with their directory password, with per-call settings. See {@link
   * #authenticate(String, String, String)}.
   *
   * @param connectionId the connection's id
   * @param username the person's directory username
   * @param password their directory password
   * @param options per-call settings
   * @return the tokens, or a second-factor challenge
   */
  public LdapAuthenticationResult authenticate(
      String connectionId, String username, String password, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post(Paths.of("/ldap/{connectionId}/authenticate", connectionId))
            .json(Bodies.of("username", username, "password", password))
            .unauthenticated()
            .neverRetry();
    JsonNode answer = http.tree(request, options);
    if (answer.path("mfa_required").asBoolean(false)) {
      return new LdapAuthenticationResult.Challenge(Json.convert(answer, MfaChallenge.class));
    }
    return new LdapAuthenticationResult.Tokens(Json.convert(answer, TokenPair.class));
  }
}
