package com.qeetgroup.qeetid.federation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qeetgroup.qeetid.QeetID;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.exception.AuthenticationException;
import com.qeetgroup.qeetid.exception.OAuthException;
import com.qeetgroup.qeetid.exception.PermissionDeniedException;
import com.qeetgroup.qeetid.exception.RateLimitException;
import com.qeetgroup.qeetid.testing.MockServer;
import com.qeetgroup.qeetid.testing.TestClients;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OAuthTest {
  private static final String TOKEN_ANSWER =
      "{\"access_token\":\"at_1\",\"token_type\":\"Bearer\",\"expires_in\":900,\"scope\":\"openid users:read\","
          + "\"id_token\":\"id_1\",\"refresh_token\":\"rt_1\"}";

  private final MockServer server = new MockServer();
  private final QeetID qeetId = TestClients.client(server);
  private final OAuthClient client = new OAuthClient("app_1", "cs_secret_1");

  @AfterEach
  void stop() {
    server.close();
  }

  private MockServer.Recorded onlyFormRequest(String path) {
    MockServer.Recorded request = server.onlyRequest();
    assertEquals("POST", request.method());
    assertEquals(path, request.path());
    assertEquals("application/x-www-form-urlencoded", request.header("content-type"));
    assertNull(request.header("authorization"), "OAuth endpoints never get the API key");
    return request;
  }

  @Test
  void clientCredentialsGrantReturnsTheLifetime() {
    server.enqueue(
        200,
        "{\"access_token\":\"at_1\",\"token_type\":\"Bearer\",\"expires_in\":3600,"
            + "\"expires_at\":\"2026-11-05T11:00:00Z\",\"scope\":\"users:read\"}");
    ClientCredentialsToken token = qeetId.oauth().clientCredentialsGrant("svc_1", "cs_1");
    assertEquals("at_1", token.accessToken());
    assertEquals(3600, token.expiresIn());
    assertEquals(Instant.parse("2026-11-05T11:00:00Z"), token.expiresAt());
    assertFalse(token.toString().contains("at_1"));
    MockServer.Recorded request = onlyFormRequest("/v1/oauth/token");
    assertEquals(
        Map.of(
            "grant_type",
            List.of("client_credentials"),
            "client_id",
            List.of("svc_1"),
            "client_secret",
            List.of("cs_1")),
        request.form());
  }

  @Test
  void authorizationCodeGrant() {
    server.enqueue(200, TOKEN_ANSWER);
    TokenResponse tokens =
        qeetId
            .oauth()
            .authorizationCodeGrant(
                AuthorizationCodeGrantParams.builder()
                    .clientId("app_1")
                    .clientSecret("cs_1")
                    .code("code_1")
                    .redirectUri("https://app.brightleaffoods.in/callback")
                    .codeVerifier("verifier_1")
                    .build());
    assertEquals(900, tokens.expiresIn());
    assertEquals("rt_1", tokens.refreshToken());
    assertEquals("id_1", tokens.idToken());
    for (String secret : List.of("at_1", "rt_1", "id_1")) {
      assertFalse(tokens.toString().contains(secret));
    }
    Map<String, List<String>> form = onlyFormRequest("/v1/oauth/token-code").form();
    assertEquals(List.of("authorization_code"), form.get("grant_type"));
    assertEquals(List.of("code_1"), form.get("code"));
    assertEquals(List.of("verifier_1"), form.get("code_verifier"));
    assertEquals(List.of("https://app.brightleaffoods.in/callback"), form.get("redirect_uri"));
  }

  @Test
  void refreshTokenGrant() {
    server.enqueue(200, TOKEN_ANSWER);
    qeetId
        .oauth()
        .refreshTokenGrant(
            RefreshTokenGrantParams.builder()
                .clientId("app_1")
                .refreshToken("rt_0")
                .scope("openid")
                .build());
    Map<String, List<String>> form = onlyFormRequest("/v1/oauth/token-code").form();
    assertEquals(List.of("refresh_token"), form.get("grant_type"));
    assertEquals(List.of("rt_0"), form.get("refresh_token"));
    assertFalse(form.containsKey("client_secret"), "a public client sends no secret");
  }

  @Test
  void tokenExchangeDefaultsTheTokenTypes() {
    server.enqueue(200, TOKEN_ANSWER).enqueue(200, TOKEN_ANSWER);
    qeetId
        .oauth()
        .tokenExchangeGrant(
            TokenExchangeGrantParams.builder()
                .clientId("app_1")
                .clientSecret("cs")
                .subjectToken("st")
                .build());
    Map<String, List<String>> form = server.lastRequest().form();
    assertEquals(
        List.of("urn:ietf:params:oauth:grant-type:token-exchange"), form.get("grant_type"));
    assertEquals(
        List.of("urn:ietf:params:oauth:token-type:access_token"), form.get("subject_token_type"));
    assertFalse(form.containsKey("actor_token_type"));

    qeetId
        .oauth()
        .tokenExchangeGrant(
            TokenExchangeGrantParams.builder()
                .clientId("app_1")
                .clientSecret("cs")
                .subjectToken("st")
                .actorToken("act")
                .scope("files:read")
                .build());
    form = server.lastRequest().form();
    assertEquals(
        List.of("urn:ietf:params:oauth:token-type:access_token"), form.get("actor_token_type"));
    assertEquals(List.of("files:read"), form.get("scope"));
  }

  @Test
  void devicePollingSurfacesThePendingStates() {
    server.enqueue(400, "{\"error\":\"authorization_pending\",\"error_description\":\"Not yet.\"}");
    OAuthException pending =
        assertThrows(
            OAuthException.class,
            () ->
                qeetId
                    .oauth()
                    .deviceCodeGrant(
                        DeviceCodeGrantParams.builder()
                            .clientId("tv_1")
                            .deviceCode("dc_1")
                            .build()));
    assertEquals(OAuthException.AUTHORIZATION_PENDING, pending.error());
    assertEquals(400, pending.status());
    assertEquals(
        List.of("urn:ietf:params:oauth:grant-type:device_code"),
        onlyFormRequest("/v1/oauth/token-code").form().get("grant_type"));
  }

  @Test
  void cibaPolling() {
    server.enqueue(400, "{\"error\":\"slow_down\"}");
    OAuthException slow =
        assertThrows(
            OAuthException.class,
            () ->
                qeetId
                    .oauth()
                    .cibaGrant(
                        CibaGrantParams.builder()
                            .clientId("app_1")
                            .clientSecret("cs")
                            .authReqId("req_1")
                            .build()));
    assertEquals(OAuthException.SLOW_DOWN, slow.code());
    assertEquals(
        List.of("urn:openid:params:grant-type:ciba"),
        onlyFormRequest("/v1/oauth/token-code").form().get("grant_type"));
  }

  @Test
  void aRefusedGrantIsTypedAndNeverRetried() {
    server.enqueue(401, "{\"error\":\"invalid_client\",\"error_description\":\"Unknown client.\"}");
    OAuthException error =
        assertThrows(
            OAuthException.class,
            () -> qeetId.oauth().clientCredentialsGrant("svc_1", "wrong-secret"));
    assertEquals(OAuthException.INVALID_CLIENT, error.code());
    assertEquals(401, error.status());
    assertFalse(error.getMessage().contains("wrong-secret"));
    assertEquals(1, server.requests().size());

    server.enqueue(503, "{\"error\":{\"code\":\"server.overloaded\",\"message\":\"m\"}}");
    assertThrows(
        com.qeetgroup.qeetid.exception.ServiceUnavailableException.class,
        () ->
            qeetId
                .oauth()
                .refreshTokenGrant(
                    RefreshTokenGrantParams.builder().clientId("a").refreshToken("r").build()));
    assertEquals(2, server.requests().size(), "a grant is single-use and is not retried");
  }

  @Test
  void aRateLimitedGrantIsTheEnvelopeError() {
    server.enqueue(429, "{\"error\":{\"code\":\"too_many_requests\",\"message\":\"Slow down.\"}}");
    assertThrows(
        RateLimitException.class, () -> qeetId.oauth().clientCredentialsGrant("svc_1", "cs"));
  }

  @Test
  void startsTheDeviceAndBackchannelFlows() {
    server.enqueue(
        200,
        "{\"device_code\":\"dc_1\",\"user_code\":\"ABCD-EFGH\",\"verification_uri\":\"https://id.qeet.in/device\","
            + "\"verification_uri_complete\":\"https://id.qeet.in/device?code=ABCD-EFGH\",\"expires_in\":600,\"interval\":5}");
    DeviceAuthorization device =
        qeetId
            .oauth()
            .deviceAuthorization(
                DeviceAuthorizationParams.builder().clientId("tv_1").scope("openid").build());
    assertEquals(5, device.interval());
    assertFalse(device.toString().contains("dc_1"));
    assertFalse(device.toString().contains("ABCD-EFGH"));
    assertEquals(
        List.of("tv_1"), onlyFormRequest("/v1/oauth/device_authorization").form().get("client_id"));

    server.enqueue(200, "{\"auth_req_id\":\"req_1\",\"expires_in\":120,\"interval\":5}");
    BackchannelAuthentication ciba =
        qeetId
            .oauth()
            .backchannelAuthentication(
                BackchannelAuthenticationParams.builder()
                    .clientId("app_1")
                    .clientSecret("cs")
                    .loginHint("meera@brightleaffoods.in")
                    .bindingMessage("INV-2041")
                    .build());
    assertEquals(120, ciba.expiresIn());
    assertEquals("/v1/oauth/bc-authorize", server.lastRequest().path());
    assertEquals(
        List.of("meera@brightleaffoods.in"), server.lastRequest().form().get("login_hint"));
  }

  @Test
  void introspectsAsTheApplicationAndRetriesSafely() {
    server
        .enqueue(503, "{}")
        .enqueue(
            200,
            "{\"active\":true,\"sub\":\"u_1\",\"scope\":\"files:read files:write\",\"aud\":[\"https://mcp.brightleaffoods.in\"],"
                + "\"client_id\":\"app_1\",\"iss\":\"https://api.id.qeet.in\",\"token_type\":\"access_token\",\"exp\":1793870000,"
                + "\"iat\":1793869000,\"tenant_id\":\"t_1\",\"sid\":\"s_1\",\"actor_type\":\"agent\",\"agent_id\":\"agt_1\","
                + "\"act\":{\"sub\":\"svc_1\"},\"resource\":\"https://mcp.brightleaffoods.in\"}");
    Introspection answer = qeetId.oauth().introspect("at_1", client);
    assertTrue(answer.active());
    assertEquals("u_1", answer.subject());
    assertEquals(java.util.Set.of("files:read", "files:write"), answer.scopes());
    assertEquals(List.of("https://mcp.brightleaffoods.in"), answer.audience());
    assertEquals(List.of("https://mcp.brightleaffoods.in"), answer.resource());
    assertEquals("app_1", answer.clientId());
    assertEquals("https://api.id.qeet.in", answer.issuer());
    assertEquals("access_token", answer.tokenType());
    assertEquals(Instant.ofEpochSecond(1793870000), answer.expiresAt());
    assertEquals(Instant.ofEpochSecond(1793869000), answer.issuedAt());
    assertEquals("t_1", answer.tenantId());
    assertEquals("s_1", answer.sessionId());
    assertEquals("agent", answer.actorType());
    assertEquals("agt_1", answer.agentId());
    assertEquals("svc_1", answer.actor());
    assertTrue(answer.claims().containsKey("act"));
    assertEquals(2, server.requests().size());
    Map<String, List<String>> form = server.lastRequest().form();
    assertEquals(List.of("at_1"), form.get("token"));
    assertEquals(List.of("app_1"), form.get("client_id"));
    assertEquals(List.of("cs_secret_1"), form.get("client_secret"));
    assertNull(server.lastRequest().header("authorization"));
    assertEquals("/oauth/introspect", server.lastRequest().path());
  }

  @Test
  void anInactiveTokenHasNoClaims() {
    server.enqueue(200, "{\"active\":false}");
    Introspection answer =
        qeetId.oauth().introspect("at_1", client, "refresh_token", RequestOptions.none());
    assertFalse(answer.active());
    assertNull(answer.subject());
    assertTrue(answer.scopes().isEmpty());
    assertTrue(answer.audience().isEmpty());
    assertNull(answer.expiresAt());
    assertNull(answer.actor());
    assertEquals(List.of("refresh_token"), server.lastRequest().form().get("token_type_hint"));
  }

  @Test
  void revokesARefreshToken() {
    server.enqueue(MockServer.Reply.empty(200));
    qeetId.oauth().revoke("rt_1", new OAuthClient("spa_1", null));
    Map<String, List<String>> form = onlyFormRequest("/oauth/revoke").form();
    assertEquals(List.of("rt_1"), form.get("token"));
    assertFalse(form.containsKey("client_secret"));
    server.enqueue(400, "{\"error\":\"unsupported_token_type\"}");
    assertEquals(
        OAuthException.UNSUPPORTED_TOKEN_TYPE,
        assertThrows(OAuthException.class, () -> qeetId.oauth().revoke("at_1", client)).code());
  }

  @Test
  void theTokenGuardAdmitsOnlyActiveTokensForItsAudienceWithTheScopes() {
    TokenGuard guard = qeetId.oauth().tokenGuard("https://mcp.brightleaffoods.in", client);
    assertEquals("https://mcp.brightleaffoods.in", guard.audience());
    assertFalse(guard.toString().contains("cs_secret_1"));
    String active =
        "{\"active\":true,\"sub\":\"u_1\",\"scope\":\"files:read\",\"aud\":\"https://mcp.brightleaffoods.in\"}";

    server.enqueue(200, active);
    assertEquals("u_1", guard.verify("t", "files:read").subject());
    assertEquals(List.of("access_token"), server.lastRequest().form().get("token_type_hint"));

    server.enqueue(200, "{\"active\":false}");
    AuthenticationException inactive =
        assertThrows(AuthenticationException.class, () -> guard.verify("t"));
    assertEquals("invalid_token", inactive.code());
    assertEquals(401, inactive.status());

    server.enqueue(200, "{\"active\":true,\"aud\":\"https://other.example\"}");
    assertEquals(
        "invalid_token",
        assertThrows(AuthenticationException.class, () -> guard.verify("t")).code());

    server.enqueue(200, active);
    PermissionDeniedException scope =
        assertThrows(PermissionDeniedException.class, () -> guard.verify("t", "files:write"));
    assertEquals("insufficient_scope", scope.code());
    assertEquals(403, scope.status());
  }

  @Test
  void theTokenGuardNeedsAnAudienceAndAConfidentialClient() {
    assertThrows(IllegalArgumentException.class, () -> qeetId.oauth().tokenGuard(" ", client));
    assertThrows(
        IllegalArgumentException.class,
        () -> qeetId.oauth().tokenGuard("aud", new OAuthClient("spa", null)));
    assertThrows(IllegalArgumentException.class, () -> new OAuthClient(" ", null));
    assertFalse(client.toString().contains("cs_secret_1"));
    assertEquals(
        "OAuthClient[clientId=spa, clientSecret=null]", new OAuthClient("spa", null).toString());
  }

  @Test
  void administersGrantsDevicesAndSigningKeys() {
    server.enqueue(200, "{\"revoked\":3}");
    assertEquals(3, qeetId.oauth().grants().revokeMany("t1", List.of("g1", "g2", "g3")));
    assertEquals("{\"ids\":[\"g1\",\"g2\",\"g3\"]}", server.lastRequest().bodyText());
    assertEquals("/v1/tenants/t1/oauth/grants/revoke", server.lastRequest().path());

    server.enqueue(200, "{\"revoked\":5}");
    assertEquals(5, qeetId.oauth().grants().revokeForApplication("t1", "app_1"));
    assertEquals("{\"client_id\":\"app_1\"}", server.lastRequest().bodyText());

    server.enqueue(
        200,
        "{\"keys\":[{\"kid\":\"k1\",\"alg\":\"ES256\",\"use\":\"sig\",\"status\":\"active\",\"retire_at\":null}]}");
    var keys = qeetId.oauth().signingKeys().list();
    assertEquals("k1", keys.data().get(0).kid());
    assertFalse(keys.truncated());
    assertEquals("ApiKey " + TestClients.API_KEY, server.lastRequest().header("authorization"));
  }
}
