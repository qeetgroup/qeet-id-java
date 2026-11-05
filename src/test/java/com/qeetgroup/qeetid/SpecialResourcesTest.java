package com.qeetgroup.qeetid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qeetgroup.qeetid.access.IpRuleList;
import com.qeetgroup.qeetid.access.PermissionCheckParams;
import com.qeetgroup.qeetid.access.PermissionDecision;
import com.qeetgroup.qeetid.access.RelationCheckParams;
import com.qeetgroup.qeetid.access.RelationDecision;
import com.qeetgroup.qeetid.exception.ApiException;
import com.qeetgroup.qeetid.exception.NotFoundException;
import com.qeetgroup.qeetid.exception.ValidationException;
import com.qeetgroup.qeetid.federation.ApplicationCreateParams;
import com.qeetgroup.qeetid.federation.LdapAuthenticationResult;
import com.qeetgroup.qeetid.identity.BulkImportResult;
import com.qeetgroup.qeetid.identity.BulkImportSource;
import com.qeetgroup.qeetid.identity.User;
import com.qeetgroup.qeetid.identity.UserCreateParams;
import com.qeetgroup.qeetid.identity.UserListParams;
import com.qeetgroup.qeetid.testing.MockServer;
import com.qeetgroup.qeetid.testing.TestClients;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** The resource methods written by hand, and how answers decode. */
class SpecialResourcesTest {
  private static final String USER =
      "{\"id\":\"u_1\",\"tenant_id\":\"t_1\",\"email\":\"meera@brightleaffoods.in\",\"email_verified_at\":\"2026-11-01T09:00:00Z\","
          + "\"phone\":null,\"phone_verified_at\":null,\"display_name\":\"Meera Iyer\",\"avatar_url\":null,\"status\":\"active\","
          + "\"metadata\":{\"department\":\"finance\"},\"created_at\":\"2026-11-01T09:00:00Z\",\"updated_at\":\"2026-11-02T09:00:00Z\","
          + "\"roles\":[\"admin\"],\"assigned_roles\":[{\"id\":\"r_1\",\"name\":\"admin\"}],\"some_new_field\":true}";

  private final MockServer server = new MockServer();
  private final QeetID qeetId = TestClients.client(server);

  @AfterEach
  void stop() {
    server.close();
  }

  @Test
  void decodesAUserAndToleratesNewFields() {
    server.enqueue(200, USER);
    User user = qeetId.users().get("u_1");
    assertEquals("Meera Iyer", user.displayName());
    assertEquals(Instant.parse("2026-11-01T09:00:00Z"), user.emailVerifiedAt());
    assertNull(user.phone());
    assertEquals(Map.of("department", "finance"), user.metadata());
    assertEquals("admin", user.assignedRoles().get(0).name());
    assertNull(user.mfaEnabled());
    assertNull(user.suspension());
  }

  @Test
  void walksEveryUserAcrossPages() {
    server
        .enqueue(200, "{\"items\":[" + USER + "],\"next_cursor\":\"c2\"}")
        .enqueue(200, "{\"items\":[" + USER.replace("u_1", "u_2") + "],\"next_cursor\":null}");
    List<String> ids = new ArrayList<>();
    for (User user :
        qeetId.users().list(UserListParams.builder().status("active").limit(1).build())) {
      ids.add(user.id());
    }
    assertEquals(List.of("u_1", "u_2"), ids);
    assertEquals(List.of("c2"), server.lastRequest().query().get("cursor"));
    assertEquals(List.of("active"), server.lastRequest().query().get("status"));
    assertEquals(List.of("1"), server.lastRequest().query().get("limit"));
  }

  @Test
  void createsAUserIdempotentlyAndReportsFieldErrors() {
    server.enqueue(
        422,
        "{\"error\":{\"code\":\"validation_failed\",\"message\":\"Check the fields.\","
            + "\"fields\":[{\"field\":\"email\",\"code\":\"invalid_email\",\"message\":\"Must be a valid email address.\"}]}}");
    ValidationException error =
        assertThrows(
            ValidationException.class,
            () ->
                qeetId
                    .users()
                    .create(
                        UserCreateParams.builder()
                            .tenantId("t_1")
                            .email("not-an-email")
                            .password("hunter2")
                            .build(),
                        RequestOptions.builder().idempotencyKey("onboarding-42").build()));
    assertEquals("invalid_email", error.fields().get(0).code());
    assertEquals("onboarding-42", server.onlyRequest().header("idempotency-key"));
    assertEquals(1, server.requests().size(), "a 422 is not retried");
  }

  @Test
  void suspendsThroughAnUpdate() {
    server.enqueue(200, USER);
    qeetId.users().suspend("u_1");
    assertEquals("PATCH", server.onlyRequest().method());
    assertEquals("/v1/users/u_1", server.onlyRequest().path());
    assertEquals("{\"status\":\"suspended\"}", server.onlyRequest().bodyText());
  }

  @Test
  void importsAFileAsItIs() {
    server.enqueue(
        200,
        "{\"succeeded\":1,\"failed\":0,\"created\":[{\"line\":1,\"email\":\"a@b.in\",\"id\":\"u_9\"}]}");
    byte[] file = "{\"email\":\"a@b.in\"}\n".getBytes(StandardCharsets.UTF_8);
    BulkImportResult result = qeetId.users().bulkImport(BulkImportSource.AUTH0, file);
    assertEquals(1, result.succeeded());
    MockServer.Recorded request = server.onlyRequest();
    assertEquals("/v1/users/bulk/import", request.path());
    assertEquals("source=auth0", request.rawQuery());
    assertEquals("application/x-ndjson", request.header("content-type"));
    assertEquals("{\"email\":\"a@b.in\"}\n", request.bodyText());
    assertEquals("text/csv", BulkImportSource.COGNITO.contentType());
    assertEquals("azure_b2c", BulkImportSource.AZURE_B2C.wireName());
  }

  @Test
  void checksAndExplainsPermissions() {
    PermissionCheckParams params =
        PermissionCheckParams.builder()
            .userId("u_1")
            .tenantId("t_1")
            .permission("invoice.approve")
            .build();
    server.enqueue(200, "{\"allowed\":true}");
    assertTrue(qeetId.permissions().check(params));
    assertEquals("GET", server.lastRequest().method());
    assertEquals("/v1/check", server.lastRequest().path());
    assertEquals(
        Map.of(
            "user_id",
            List.of("u_1"),
            "tenant_id",
            List.of("t_1"),
            "permission",
            List.of("invoice.approve")),
        server.lastRequest().query());

    server.enqueue(
        200,
        "{\"allowed\":false,\"reason_code\":\"authz.no_role_grant\",\"decided_by\":{\"model\":\"rbac\"}}");
    PermissionDecision decision = qeetId.permissions().explain(params);
    assertFalse(decision.allowed());
    assertEquals("rbac", decision.decidedBy().model());
    assertEquals(List.of("true"), server.lastRequest().query().get("explain"));
  }

  @Test
  void checksRelationshipsWithARetrySafePost() {
    RelationCheckParams params =
        RelationCheckParams.builder().object("doc:1").relation("viewer").userId("u_1").build();
    server.enqueue(503, "{}").enqueue(200, "{\"allowed\":true}");
    assertTrue(qeetId.relationships().check("t_1", params));
    assertEquals(2, server.requests().size(), "a check changes nothing, so it is retried");
    assertNull(server.lastRequest().rawQuery());
    assertEquals(
        "{\"object\":\"doc:1\",\"relation\":\"viewer\",\"user_id\":\"u_1\"}",
        server.lastRequest().bodyText());

    server.enqueue(
        200,
        "{\"allowed\":true,\"path\":[{\"object\":\"doc:1\",\"relation\":\"viewer\",\"subject\":\"user:u_1\",\"depth\":1}]}");
    RelationDecision decision = qeetId.relationships().explain("t_1", params);
    assertEquals(1, decision.path().get(0).depth());
    assertEquals("explain=true", server.lastRequest().rawQuery());
  }

  @Test
  void listsTuplesOnEitherSide() {
    server.enqueue(
        200,
        "{\"items\":[{\"id\":\"rt_1\",\"object\":\"doc:1\",\"relation\":\"viewer\",\"subject\":\"user:u_1\"}]}");
    assertEquals("rt_1", qeetId.relationships().listOnObject("t_1", "doc:1").data().get(0).id());
    assertEquals("object=doc%3A1", server.lastRequest().rawQuery());
    server.enqueue(200, "{\"items\":[]}");
    qeetId.relationships().listOfSubject("t_1", "group:eng#member");
    assertEquals(List.of("group:eng#member"), server.lastRequest().query().get("subject"));
  }

  @Test
  void replacesARolesPermissions() {
    server.enqueue(200, "{\"items\":[{\"id\":\"p_1\",\"key\":\"invoice.approve\"}]}");
    ListResult<com.qeetgroup.qeetid.access.Permission> permissions =
        qeetId.roles().setPermissions("t_1", "r_1", List.of("p_1"));
    assertEquals("invoice.approve", permissions.data().get(0).key());
    assertEquals("PUT", server.lastRequest().method());
    assertEquals("/v1/tenants/t_1/roles/r_1/permissions", server.lastRequest().path());
    assertEquals("{\"permission_ids\":[\"p_1\"]}", server.lastRequest().bodyText());
  }

  @Test
  void createsAnApplicationInTheTenantOfThePath() {
    server.enqueue(
        200,
        "{\"client\":{\"id\":\"a_1\",\"client_id\":\"app_1\",\"type\":\"confidential\"},\"client_secret\":\"cs_shown_once\"}");
    var created =
        qeetId
            .applications()
            .create(
                "t_1",
                ApplicationCreateParams.builder().name("Payroll").type("confidential").build());
    assertEquals("app_1", created.client().clientId());
    assertFalse(created.toString().contains("cs_shown_once"));
    assertEquals(
        "{\"name\":\"Payroll\",\"type\":\"confidential\",\"tenant_id\":\"t_1\"}",
        server.lastRequest().bodyText());
  }

  @Test
  void signsInWithADirectoryPasswordWithoutTheApiKeyOrRetries() {
    server.enqueue(
        200,
        "{\"access_token\":\"at_1\",\"token_type\":\"Bearer\",\"expires_at\":\"2026-11-05T11:00:00Z\",\"refresh_token\":\"rt_1\","
            + "\"session_expires_at\":\"2026-11-06T10:00:00Z\",\"idle_expires_at\":\"2026-11-05T12:00:00Z\",\"session_id\":\"s_1\",\"user_id\":\"u_1\"}");
    LdapAuthenticationResult result =
        qeetId.ldapConnections().authenticate("c_1", "meera", "directory-password");
    LdapAuthenticationResult.Tokens tokens =
        assertInstanceOf(LdapAuthenticationResult.Tokens.class, result);
    assertEquals("s_1", tokens.tokens().sessionId());
    assertFalse(tokens.toString().contains("at_1"));
    MockServer.Recorded request = server.lastRequest();
    assertEquals("/ldap/c_1/authenticate", request.path());
    assertNull(request.header("authorization"));
    assertEquals(
        "{\"username\":\"meera\",\"password\":\"directory-password\"}", request.bodyText());

    server.enqueue(
        200,
        "{\"mfa_required\":true,\"mfa_token\":\"mfa_1\",\"methods\":[\"totp\"],\"factors\":[{\"type\":\"totp\"}]}");
    LdapAuthenticationResult.Challenge challenge =
        assertInstanceOf(
            LdapAuthenticationResult.Challenge.class,
            qeetId.ldapConnections().authenticate("c_1", "meera", "pw"));
    assertEquals("mfa_1", challenge.challenge().mfaToken());
    assertEquals(List.of("totp"), challenge.challenge().methods());
    assertFalse(challenge.toString().contains("mfa_1"));

    server.enqueue(503, "{}");
    assertThrows(
        ApiException.class, () -> qeetId.ldapConnections().authenticate("c_1", "meera", "pw"));
    assertEquals(
        3, server.requests().size(), "each attempt counts towards the lockout: never retried");
  }

  @Test
  void readsBillingAnswersThatAreWrapped() {
    server.enqueue(200, "{\"payment_method\":null}");
    assertNull(qeetId.billing().getPaymentMethod("t_1"));
    server.enqueue(
        200,
        "{\"payment_method\":{\"provider\":\"razorpay\",\"type\":\"card\",\"last4\":\"4242\",\"last_used_at\":\"2026-11-01T09:00:00Z\"}}");
    assertEquals("4242", qeetId.billing().getPaymentMethod("t_1").last4());
    server.enqueue(200, "{\"usage\":{\"users\":42,\"applications\":3}}");
    assertEquals(Map.of("users", 42L, "applications", 3L), qeetId.billing().getUsage("t_1"));
    server.enqueue(200, "{}");
    assertTrue(qeetId.billing().getUsage("t_1").isEmpty());
  }

  @Test
  @SuppressWarnings("deprecation")
  void stillReturnsARawVaultToken() {
    server.enqueue(200, "{\"access_token\":\"xoxb-raw\"}");
    assertEquals("xoxb-raw", qeetId.tokenVault().getAccessToken("slack"));
    assertEquals("/v1/vault/tokens/slack/access-token", server.lastRequest().path());
  }

  @Test
  void readsTheAddressRulesAndTheirEnforcement() {
    server.enqueue(
        200,
        "{\"enabled\":true,\"items\":[{\"id\":\"ip_1\",\"action\":\"deny\",\"cidr\":\"203.0.113.0/24\",\"label\":\"x\"}]}");
    IpRuleList rules = qeetId.ipRules().list("t_1");
    assertTrue(rules.enabled());
    assertEquals("deny", rules.data().get(0).action());
  }

  @Test
  void unwrapsSingleValueAnswers() {
    server.enqueue(200, "{\"suspended\":4}");
    assertEquals(4, qeetId.agents().suspendAll("t_1"));
    server.enqueue(200, "{\"status\":\"suspended\"}");
    assertEquals("suspended", qeetId.agents().suspend("t_1", "a_1"));
    server.enqueue(200, "{\"value\":\"db-password\"}");
    assertEquals("db-password", qeetId.secrets().reveal("t_1", "s_1"));
    server.enqueue(200, "{\"mfa_required\":true}");
    assertTrue(qeetId.users().setMfaRequired("u_1", true));
    assertEquals("{\"required\":true}", server.lastRequest().bodyText());
  }

  @Test
  void refusesABlankIdentifierBeforeSending() {
    IllegalArgumentException error =
        assertThrows(IllegalArgumentException.class, () -> qeetId.users().get(" "));
    assertTrue(error.getMessage().startsWith("userId"));
    assertTrue(server.requests().isEmpty());
    server.enqueue(404, "{\"error\":{\"code\":\"not_found\",\"message\":\"No such user.\"}}");
    assertThrows(NotFoundException.class, () -> qeetId.users().get("a/b"));
    assertEquals("/v1/users/a%2Fb", server.lastRequest().path());
  }
}
