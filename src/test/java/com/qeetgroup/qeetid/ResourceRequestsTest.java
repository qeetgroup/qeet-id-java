package com.qeetgroup.qeetid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qeetgroup.qeetid.access.AnomalyListParams;
import com.qeetgroup.qeetid.access.AuthPolicyUpdateParams;
import com.qeetgroup.qeetid.access.BotActivityParams;
import com.qeetgroup.qeetid.access.BotSettings;
import com.qeetgroup.qeetid.access.DecisionListParams;
import com.qeetgroup.qeetid.access.EvaluationParams;
import com.qeetgroup.qeetid.access.EvaluationsParams;
import com.qeetgroup.qeetid.access.IpRuleCreateParams;
import com.qeetgroup.qeetid.access.RelationGraphParams;
import com.qeetgroup.qeetid.access.RelationObjectListParams;
import com.qeetgroup.qeetid.access.RelationTupleCreateParams;
import com.qeetgroup.qeetid.access.RiskSettingsUpdateParams;
import com.qeetgroup.qeetid.access.RoleCreateParams;
import com.qeetgroup.qeetid.access.RoleHoldersParams;
import com.qeetgroup.qeetid.access.RoleUpdateParams;
import com.qeetgroup.qeetid.access.SecurityPolicyUpdateParams;
import com.qeetgroup.qeetid.developer.AgentCreateParams;
import com.qeetgroup.qeetid.developer.AgentRotateSecretParams;
import com.qeetgroup.qeetid.developer.ApiKeyCreateParams;
import com.qeetgroup.qeetid.developer.ApiKeyRotateParams;
import com.qeetgroup.qeetid.developer.AuthHookCreateParams;
import com.qeetgroup.qeetid.developer.AuthHookUpdateParams;
import com.qeetgroup.qeetid.developer.SecretCreateParams;
import com.qeetgroup.qeetid.developer.SecretUpdateParams;
import com.qeetgroup.qeetid.developer.TokenVaultProviderCreateParams;
import com.qeetgroup.qeetid.developer.VerifiableCredentialIssueParams;
import com.qeetgroup.qeetid.developer.WebhookCreateParams;
import com.qeetgroup.qeetid.developer.WebhookDeliveryListParams;
import com.qeetgroup.qeetid.developer.WebhookUpdateParams;
import com.qeetgroup.qeetid.federation.AdminPortalLinkCreateParams;
import com.qeetgroup.qeetid.federation.ApplicationUpdateParams;
import com.qeetgroup.qeetid.federation.LdapConnectionCreateParams;
import com.qeetgroup.qeetid.federation.LdapConnectionUpdateParams;
import com.qeetgroup.qeetid.federation.OAuthGrantListParams;
import com.qeetgroup.qeetid.federation.OAuthResourceCreateParams;
import com.qeetgroup.qeetid.federation.SamlConnectionCreateParams;
import com.qeetgroup.qeetid.federation.SamlConnectionImportParams;
import com.qeetgroup.qeetid.federation.SamlConnectionUpdateParams;
import com.qeetgroup.qeetid.federation.SamlMetadataParams;
import com.qeetgroup.qeetid.federation.SamlServiceProviderCreateParams;
import com.qeetgroup.qeetid.federation.SamlServiceProviderUpdateParams;
import com.qeetgroup.qeetid.federation.ScimTokenCreateParams;
import com.qeetgroup.qeetid.federation.ScimTokenRotateParams;
import com.qeetgroup.qeetid.federation.ServicePrincipalCreateParams;
import com.qeetgroup.qeetid.federation.ServicePrincipalRotateSecretParams;
import com.qeetgroup.qeetid.federation.SocialProviderUpdateParams;
import com.qeetgroup.qeetid.federation.SocialProviderUpsertParams;
import com.qeetgroup.qeetid.federation.SsoGroupMappingCreateParams;
import com.qeetgroup.qeetid.identity.BrandingUpdateParams;
import com.qeetgroup.qeetid.identity.BulkUserRow;
import com.qeetgroup.qeetid.identity.DeletedUserListParams;
import com.qeetgroup.qeetid.identity.DomainUpdateParams;
import com.qeetgroup.qeetid.identity.GroupCreateParams;
import com.qeetgroup.qeetid.identity.GroupListParams;
import com.qeetgroup.qeetid.identity.GroupUpdateParams;
import com.qeetgroup.qeetid.identity.InvitationCreateParams;
import com.qeetgroup.qeetid.identity.TenantUpdateParams;
import com.qeetgroup.qeetid.identity.UserBulkCreateParams;
import com.qeetgroup.qeetid.identity.UserCreateParams;
import com.qeetgroup.qeetid.identity.UserListParams;
import com.qeetgroup.qeetid.identity.UserUpdateParams;
import com.qeetgroup.qeetid.operations.AnalyticsOverviewParams;
import com.qeetgroup.qeetid.operations.AuditAnomalyListParams;
import com.qeetgroup.qeetid.operations.AuditAnomalySettings;
import com.qeetgroup.qeetid.operations.AuditLogListParams;
import com.qeetgroup.qeetid.operations.BillingProfileUpdateParams;
import com.qeetgroup.qeetid.operations.CheckoutParams;
import com.qeetgroup.qeetid.operations.EmailTemplateContent;
import com.qeetgroup.qeetid.operations.EmailTemplatePreviewParams;
import com.qeetgroup.qeetid.operations.EmailTemplateTestParams;
import com.qeetgroup.qeetid.operations.ErasureCreateParams;
import com.qeetgroup.qeetid.operations.ExportCreateParams;
import com.qeetgroup.qeetid.operations.InvoicePayParams;
import com.qeetgroup.qeetid.operations.LogSinkCreateParams;
import com.qeetgroup.qeetid.operations.RateLimitsUpdateParams;
import com.qeetgroup.qeetid.operations.RetentionPolicyUpdateParams;
import com.qeetgroup.qeetid.operations.SubscriptionUpdateParams;
import com.qeetgroup.qeetid.testing.MockServer;
import com.qeetgroup.qeetid.testing.TestClients;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Every generated resource method sends the request the API documents: its method, encoded path,
 * query, JSON body, credential and, on the nine idempotent creates, an idempotency key. The routes
 * themselves were checked against the server's OpenAPI documents when the methods were written.
 */
class ResourceRequestsTest {
  private static final ObjectMapper JSON = new ObjectMapper();
  private final MockServer server =
      new MockServer().handler(request -> MockServer.Reply.json(200, "{\"items\":[]}"));

  record Row(
      String name,
      Consumer<QeetID> call,
      String method,
      String path,
      String query,
      String body,
      boolean idempotent,
      boolean authenticated) {
    @Override
    public String toString() {
      return name;
    }
  }

  private static Row row(
      String name,
      Consumer<QeetID> call,
      String method,
      String path,
      String query,
      String body,
      boolean idempotent,
      boolean authenticated) {
    return new Row(name, call, method, path, query, body, idempotent, authenticated);
  }

  @AfterEach
  void stop() {
    server.close();
  }

  @ParameterizedTest
  @MethodSource("rows")
  void sendsTheDocumentedRequest(Row row) throws Exception {
    row.call().accept(TestClients.client(server));
    MockServer.Recorded request = server.onlyRequest();
    assertEquals(row.method(), request.method());
    assertEquals(row.path(), request.path());
    assertEquals(row.query(), request.rawQuery());
    if (row.body() == null) {
      assertEquals("", request.bodyText());
    } else {
      assertEquals(JSON.readTree(row.body()), JSON.readTree(request.body()));
      assertEquals("application/json", request.header("content-type"));
    }
    if (row.idempotent()) {
      assertNotNull(request.header("idempotency-key"));
    } else {
      assertNull(request.header("idempotency-key"));
    }
    if (row.authenticated()) {
      assertEquals("ApiKey " + TestClients.API_KEY, request.header("authorization"));
    } else {
      assertNull(request.header("authorization"));
    }
  }

  static Stream<Row> rows() {
    return Stream.of(
        row(
            "AuthPolicies.get",
            qeetId -> qeetId.authPolicies().get("p1"),
            "GET",
            "/v1/tenants/p1/auth-policy",
            null,
            null,
            false,
            true),
        row(
            "AuthPolicies.update",
            qeetId -> qeetId.authPolicies().update("p1", AuthPolicyUpdateParams.builder().build()),
            "PUT",
            "/v1/tenants/p1/auth-policy",
            null,
            "{}",
            false,
            true),
        row(
            "AuthPolicies.getUsage",
            qeetId -> qeetId.authPolicies().getUsage("p1"),
            "GET",
            "/v1/tenants/p1/auth-policy/usage",
            null,
            null,
            false,
            true),
        row(
            "Decisions.evaluate",
            qeetId -> qeetId.decisions().evaluate("p1", EvaluationParams.builder().build()),
            "POST",
            "/v1/tenants/p1/access/v1/evaluation",
            null,
            "{}",
            false,
            true),
        row(
            "Decisions.evaluateMany",
            qeetId ->
                qeetId
                    .decisions()
                    .evaluateMany(
                        "p1",
                        EvaluationsParams.builder()
                            .evaluations(List.of(EvaluationParams.builder().build()))
                            .build()),
            "POST",
            "/v1/tenants/p1/access/v1/evaluations",
            null,
            "{\"evaluations\":[{}]}",
            false,
            true),
        row(
            "Decisions.list without parameters",
            qeetId -> qeetId.decisions().list("p1"),
            "GET",
            "/v1/tenants/p1/authorization/decisions",
            null,
            null,
            false,
            true),
        row(
            "Decisions.list",
            qeetId -> qeetId.decisions().list("p1", DecisionListParams.builder().build()),
            "GET",
            "/v1/tenants/p1/authorization/decisions",
            null,
            null,
            false,
            true),
        row(
            "IpRules.create",
            qeetId ->
                qeetId
                    .ipRules()
                    .create(
                        "p1",
                        IpRuleCreateParams.builder().action("v").cidr("v").label("v").build()),
            "POST",
            "/v1/tenants/p1/ip-rules",
            null,
            "{\"action\":\"v\",\"cidr\":\"v\",\"label\":\"v\"}",
            false,
            true),
        row(
            "IpRules.delete",
            qeetId -> qeetId.ipRules().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/ip-rules/p2",
            null,
            null,
            false,
            true),
        row(
            "IpRules.check",
            qeetId -> qeetId.ipRules().check("p1", "v"),
            "POST",
            "/v1/tenants/p1/ip-rules/check",
            null,
            "{\"ip\":\"v\"}",
            false,
            true),
        row(
            "IpRules.setEnforcement",
            qeetId -> qeetId.ipRules().setEnforcement("p1", true),
            "PUT",
            "/v1/tenants/p1/ip-rules/config",
            null,
            "{\"enabled\":true}",
            false,
            true),
        row(
            "Permissions.list",
            qeetId -> qeetId.permissions().list(),
            "GET",
            "/v1/permissions",
            null,
            null,
            false,
            true),
        row(
            "Permissions.listEffective",
            qeetId -> qeetId.permissions().listEffective("p1", "p2"),
            "GET",
            "/v1/users/p2/tenants/p1/permissions",
            null,
            null,
            false,
            true),
        row(
            "Relationships.create",
            qeetId ->
                qeetId
                    .relationships()
                    .create(
                        "p1",
                        RelationTupleCreateParams.builder()
                            .object("v")
                            .relation("v")
                            .subject("v")
                            .build()),
            "POST",
            "/v1/tenants/p1/relation-tuples",
            null,
            "{\"object\":\"v\",\"relation\":\"v\",\"subject\":\"v\"}",
            false,
            true),
        row(
            "Relationships.delete",
            qeetId -> qeetId.relationships().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/relation-tuples/p2",
            null,
            null,
            false,
            true),
        row(
            "Relationships.graph",
            qeetId ->
                qeetId
                    .relationships()
                    .graph("p1", RelationGraphParams.builder().object("v").relation("v").build()),
            "GET",
            "/v1/tenants/p1/relation-tuples/graph",
            "object=v&relation=v",
            null,
            false,
            true),
        row(
            "Relationships.listTypes",
            qeetId -> qeetId.relationships().listTypes("p1"),
            "GET",
            "/v1/tenants/p1/relation-tuples/types",
            null,
            null,
            false,
            true),
        row(
            "Relationships.listObjects",
            qeetId ->
                qeetId
                    .relationships()
                    .listObjects("p1", RelationObjectListParams.builder().type("v").build()),
            "GET",
            "/v1/tenants/p1/relation-tuples/objects",
            "type=v",
            null,
            false,
            true),
        row(
            "Roles.list",
            qeetId -> qeetId.roles().list("p1"),
            "GET",
            "/v1/tenants/p1/roles",
            null,
            null,
            false,
            true),
        row(
            "Roles.create",
            qeetId -> qeetId.roles().create("p1", RoleCreateParams.builder().name("v").build()),
            "POST",
            "/v1/tenants/p1/roles",
            null,
            "{\"name\":\"v\"}",
            false,
            true),
        row(
            "Roles.update",
            qeetId -> qeetId.roles().update("p1", "p2", RoleUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/roles/p2",
            null,
            "{}",
            false,
            true),
        row(
            "Roles.delete",
            qeetId -> qeetId.roles().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/roles/p2",
            null,
            null,
            false,
            true),
        row(
            "Roles.listPermissions",
            qeetId -> qeetId.roles().listPermissions("p1"),
            "GET",
            "/v1/roles/p1/permissions",
            null,
            null,
            false,
            true),
        row(
            "Roles.grantPermission",
            qeetId -> qeetId.roles().grantPermission("p1", "p2"),
            "POST",
            "/v1/roles/p1/permissions/p2",
            null,
            null,
            false,
            true),
        row(
            "Roles.revokePermission",
            qeetId -> qeetId.roles().revokePermission("p1", "p2"),
            "DELETE",
            "/v1/roles/p1/permissions/p2",
            null,
            null,
            false,
            true),
        row(
            "Roles.listHolders without parameters",
            qeetId -> qeetId.roles().listHolders("p1", "p2"),
            "GET",
            "/v1/tenants/p1/roles/p2/holders",
            null,
            null,
            false,
            true),
        row(
            "Roles.listHolders",
            qeetId -> qeetId.roles().listHolders("p1", "p2", RoleHoldersParams.builder().build()),
            "GET",
            "/v1/tenants/p1/roles/p2/holders",
            null,
            null,
            false,
            true),
        row(
            "Roles.assign",
            qeetId -> qeetId.roles().assign("p1", "p2", "p3"),
            "POST",
            "/v1/users/p2/tenants/p1/roles/p3",
            null,
            null,
            false,
            true),
        row(
            "Roles.unassign",
            qeetId -> qeetId.roles().unassign("p1", "p2", "p3"),
            "DELETE",
            "/v1/users/p2/tenants/p1/roles/p3",
            null,
            null,
            false,
            true),
        row(
            "SecurityPolicies.get",
            qeetId -> qeetId.securityPolicies().get("p1"),
            "GET",
            "/v1/tenants/p1/policy",
            null,
            null,
            false,
            true),
        row(
            "SecurityPolicies.update",
            qeetId ->
                qeetId
                    .securityPolicies()
                    .update("p1", SecurityPolicyUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/policy",
            null,
            "{}",
            false,
            true),
        row(
            "SecurityPolicies.replace",
            qeetId ->
                qeetId
                    .securityPolicies()
                    .replace("p1", SecurityPolicyUpdateParams.builder().build()),
            "PUT",
            "/v1/tenants/p1/policy",
            null,
            "{}",
            false,
            true),
        row(
            "ThreatDetection.getBotActivity without parameters",
            qeetId -> qeetId.threatDetection().getBotActivity("p1"),
            "GET",
            "/v1/tenants/p1/security/bots",
            null,
            null,
            false,
            true),
        row(
            "ThreatDetection.getBotActivity",
            qeetId ->
                qeetId.threatDetection().getBotActivity("p1", BotActivityParams.builder().build()),
            "GET",
            "/v1/tenants/p1/security/bots",
            null,
            null,
            false,
            true),
        row(
            "ThreatDetection.getBotSettings",
            qeetId -> qeetId.threatDetection().getBotSettings("p1"),
            "GET",
            "/v1/tenants/p1/security/bots/settings",
            null,
            null,
            false,
            true),
        row(
            "ThreatDetection.updateBotSettings",
            qeetId ->
                qeetId
                    .threatDetection()
                    .updateBotSettings("p1", new BotSettings(true, true, true, true, 0.5)),
            "PUT",
            "/v1/tenants/p1/security/bots/settings",
            null,
            "{\"ua_check\":true,\"honeypot\":true,\"captcha\":true,\"signature\":true,\"score_threshold\":0.5}",
            false,
            true),
        row(
            "ThreatDetection.getRiskSettings",
            qeetId -> qeetId.threatDetection().getRiskSettings("p1"),
            "GET",
            "/v1/tenants/p1/security/risk-settings",
            null,
            null,
            false,
            true),
        row(
            "ThreatDetection.updateRiskSettings",
            qeetId ->
                qeetId
                    .threatDetection()
                    .updateRiskSettings(
                        "p1",
                        RiskSettingsUpdateParams.builder()
                            .mediumThreshold(0.5)
                            .highThreshold(0.5)
                            .forceMfaAtLevel("v")
                            .impossibleTravelEnabled(true)
                            .minTravelHours(0.5)
                            .deviceReputationEnabled(true)
                            .build()),
            "PUT",
            "/v1/tenants/p1/security/risk-settings",
            null,
            "{\"medium_threshold\":0.5,\"high_threshold\":0.5,\"force_mfa_at_level\":\"v\",\"impossible_travel_enabled\":true,\"min_travel_hours\":0.5,\"device_reputation_enabled\":true}",
            false,
            true),
        row(
            "ThreatDetection.listAnomalies without parameters",
            qeetId -> qeetId.threatDetection().listAnomalies("p1"),
            "GET",
            "/v1/tenants/p1/security/anomalies",
            null,
            null,
            false,
            true),
        row(
            "ThreatDetection.listAnomalies",
            qeetId ->
                qeetId.threatDetection().listAnomalies("p1", AnomalyListParams.builder().build()),
            "GET",
            "/v1/tenants/p1/security/anomalies",
            null,
            null,
            false,
            true),
        row(
            "ThreatDetection.getAnomalySummary",
            qeetId -> qeetId.threatDetection().getAnomalySummary("p1"),
            "GET",
            "/v1/tenants/p1/security/anomalies/summary",
            null,
            null,
            false,
            true),
        row(
            "ThreatDetection.resolveAnomaly",
            qeetId -> qeetId.threatDetection().resolveAnomaly("p1", "p2"),
            "POST",
            "/v1/tenants/p1/security/anomalies/p2/resolve",
            null,
            null,
            false,
            true),
        row(
            "Agents.create",
            qeetId ->
                qeetId
                    .agents()
                    .create("p1", AgentCreateParams.builder().name("v").sponsorUserId("v").build()),
            "POST",
            "/v1/tenants/p1/agents",
            null,
            "{\"name\":\"v\",\"sponsor_user_id\":\"v\"}",
            true,
            true),
        row(
            "Agents.list",
            qeetId -> qeetId.agents().list("p1"),
            "GET",
            "/v1/tenants/p1/agents",
            null,
            null,
            false,
            true),
        row(
            "Agents.delete",
            qeetId -> qeetId.agents().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/agents/p2",
            null,
            null,
            false,
            true),
        row(
            "Agents.setDisabled",
            qeetId -> qeetId.agents().setDisabled("p1", "p2", true),
            "PATCH",
            "/v1/tenants/p1/agents/p2",
            null,
            "{\"disabled\":true}",
            false,
            true),
        row(
            "Agents.suspend",
            qeetId -> qeetId.agents().suspend("p1", "p2"),
            "POST",
            "/v1/tenants/p1/agents/p2/suspend",
            null,
            null,
            false,
            true),
        row(
            "Agents.resume",
            qeetId -> qeetId.agents().resume("p1", "p2"),
            "POST",
            "/v1/tenants/p1/agents/p2/resume",
            null,
            null,
            false,
            true),
        row(
            "Agents.decommission",
            qeetId -> qeetId.agents().decommission("p1", "p2"),
            "POST",
            "/v1/tenants/p1/agents/p2/decommission",
            null,
            null,
            false,
            true),
        row(
            "Agents.suspendAll",
            qeetId -> qeetId.agents().suspendAll("p1"),
            "POST",
            "/v1/tenants/p1/agents/kill-all",
            null,
            null,
            false,
            true),
        row(
            "Agents.resumeAll",
            qeetId -> qeetId.agents().resumeAll("p1"),
            "POST",
            "/v1/tenants/p1/agents/resume-all",
            null,
            null,
            false,
            true),
        row(
            "Agents.rotateSecret without parameters",
            qeetId -> qeetId.agents().rotateSecret("p1", "p2"),
            "POST",
            "/v1/tenants/p1/agents/p2/rotate-secret",
            null,
            "{}",
            false,
            true),
        row(
            "Agents.rotateSecret",
            qeetId ->
                qeetId.agents().rotateSecret("p1", "p2", AgentRotateSecretParams.builder().build()),
            "POST",
            "/v1/tenants/p1/agents/p2/rotate-secret",
            null,
            "{}",
            false,
            true),
        row(
            "Agents.listSponsoredBy",
            qeetId -> qeetId.agents().listSponsoredBy("p1", "p2"),
            "GET",
            "/v1/tenants/p1/agents/sponsored-by/p2",
            null,
            null,
            false,
            true),
        row(
            "Agents.transferSponsorship",
            qeetId -> qeetId.agents().transferSponsorship("p1", "p2", "v"),
            "POST",
            "/v1/tenants/p1/agents/sponsored-by/p2/transfer",
            null,
            "{\"to_user_id\":\"v\"}",
            false,
            true),
        row(
            "Agents.createToken",
            qeetId -> qeetId.agents().createToken("v", "v"),
            "POST",
            "/v1/agents/token",
            null,
            "{\"agent_id\":\"v\",\"secret\":\"v\"}",
            false,
            false),
        row(
            "ApiKeys.create",
            qeetId ->
                qeetId
                    .apiKeys()
                    .create(ApiKeyCreateParams.builder().tenantId("v").name("v").build()),
            "POST",
            "/v1/api-keys",
            null,
            "{\"tenant_id\":\"v\",\"name\":\"v\"}",
            true,
            true),
        row(
            "ApiKeys.list",
            qeetId -> qeetId.apiKeys().list("p1"),
            "GET",
            "/v1/tenants/p1/api-keys",
            null,
            null,
            false,
            true),
        row(
            "ApiKeys.rotate without parameters",
            qeetId -> qeetId.apiKeys().rotate("p1", "p2"),
            "POST",
            "/v1/tenants/p1/api-keys/p2/rotate",
            null,
            "{}",
            false,
            true),
        row(
            "ApiKeys.rotate",
            qeetId -> qeetId.apiKeys().rotate("p1", "p2", ApiKeyRotateParams.builder().build()),
            "POST",
            "/v1/tenants/p1/api-keys/p2/rotate",
            null,
            "{}",
            false,
            true),
        row(
            "ApiKeys.revoke",
            qeetId -> qeetId.apiKeys().revoke("p1"),
            "DELETE",
            "/v1/api-keys/p1",
            null,
            null,
            false,
            true),
        row(
            "AuthHooks.list",
            qeetId -> qeetId.authHooks().list("p1"),
            "GET",
            "/v1/tenants/p1/auth-hooks",
            null,
            null,
            false,
            true),
        row(
            "AuthHooks.create",
            qeetId ->
                qeetId.authHooks().create("p1", AuthHookCreateParams.builder().url("v").build()),
            "POST",
            "/v1/tenants/p1/auth-hooks",
            null,
            "{\"url\":\"v\"}",
            false,
            true),
        row(
            "AuthHooks.update",
            qeetId -> qeetId.authHooks().update("p1", "p2", AuthHookUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/auth-hooks/p2",
            null,
            "{}",
            false,
            true),
        row(
            "AuthHooks.delete",
            qeetId -> qeetId.authHooks().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/auth-hooks/p2",
            null,
            null,
            false,
            true),
        row(
            "AuthHooks.test",
            qeetId -> qeetId.authHooks().test("p1", "p2"),
            "POST",
            "/v1/tenants/p1/auth-hooks/p2/test",
            null,
            null,
            false,
            true),
        row(
            "Secrets.list",
            qeetId -> qeetId.secrets().list("p1"),
            "GET",
            "/v1/tenants/p1/secrets",
            null,
            null,
            false,
            true),
        row(
            "Secrets.create",
            qeetId ->
                qeetId
                    .secrets()
                    .create(
                        "p1", SecretCreateParams.builder().name("v").scope("v").value("v").build()),
            "POST",
            "/v1/tenants/p1/secrets",
            null,
            "{\"name\":\"v\",\"scope\":\"v\",\"value\":\"v\"}",
            false,
            true),
        row(
            "Secrets.update",
            qeetId -> qeetId.secrets().update("p1", "p2", SecretUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/secrets/p2",
            null,
            "{}",
            false,
            true),
        row(
            "Secrets.delete",
            qeetId -> qeetId.secrets().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/secrets/p2",
            null,
            null,
            false,
            true),
        row(
            "Secrets.reveal",
            qeetId -> qeetId.secrets().reveal("p1", "p2"),
            "POST",
            "/v1/tenants/p1/secrets/p2/reveal",
            null,
            null,
            false,
            true),
        row(
            "Secrets.getValue",
            qeetId -> qeetId.secrets().getValue("p1"),
            "GET",
            "/v1/vault/p1",
            null,
            null,
            false,
            true),
        row(
            "TokenVault.createProvider",
            qeetId ->
                qeetId
                    .tokenVault()
                    .createProvider(
                        "p1",
                        TokenVaultProviderCreateParams.builder()
                            .provider("v")
                            .clientId("v")
                            .clientSecret("v")
                            .authorizeUrl("v")
                            .tokenUrl("v")
                            .build()),
            "POST",
            "/v1/tenants/p1/vault/tokens/providers",
            null,
            "{\"provider\":\"v\",\"client_id\":\"v\",\"client_secret\":\"v\",\"authorize_url\":\"v\",\"token_url\":\"v\"}",
            false,
            true),
        row(
            "TokenVault.listProviders",
            qeetId -> qeetId.tokenVault().listProviders("p1"),
            "GET",
            "/v1/tenants/p1/vault/tokens/providers",
            null,
            null,
            false,
            true),
        row(
            "TokenVault.deleteProvider",
            qeetId -> qeetId.tokenVault().deleteProvider("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/vault/tokens/providers/p2",
            null,
            null,
            false,
            true),
        row(
            "VerifiableCredentials.issue",
            qeetId ->
                qeetId
                    .verifiableCredentials()
                    .issue(
                        "p1",
                        VerifiableCredentialIssueParams.builder().subject("v").type("v").build()),
            "POST",
            "/v1/tenants/p1/credentials",
            null,
            "{\"subject\":\"v\",\"type\":\"v\"}",
            false,
            true),
        row(
            "VerifiableCredentials.list",
            qeetId -> qeetId.verifiableCredentials().list("p1"),
            "GET",
            "/v1/tenants/p1/credentials",
            null,
            null,
            false,
            true),
        row(
            "VerifiableCredentials.revoke",
            qeetId -> qeetId.verifiableCredentials().revoke("p1", "p2"),
            "POST",
            "/v1/tenants/p1/credentials/p2/revoke",
            null,
            null,
            false,
            true),
        row(
            "VerifiableCredentials.verify",
            qeetId -> qeetId.verifiableCredentials().verify("v"),
            "POST",
            "/v1/credentials/verify",
            null,
            "{\"credential\":\"v\"}",
            false,
            false),
        row(
            "WebhookDeliveries.list without parameters",
            qeetId -> qeetId.webhooks().deliveries().list("p1"),
            "GET",
            "/v1/webhooks/p1/deliveries",
            null,
            null,
            false,
            true),
        row(
            "WebhookDeliveries.list",
            qeetId ->
                qeetId
                    .webhooks()
                    .deliveries()
                    .list("p1", WebhookDeliveryListParams.builder().build()),
            "GET",
            "/v1/webhooks/p1/deliveries",
            null,
            null,
            false,
            true),
        row(
            "WebhookDeliveries.retry",
            qeetId -> qeetId.webhooks().deliveries().retry("p1", "p2"),
            "POST",
            "/v1/webhooks/p1/deliveries/p2/retry",
            null,
            null,
            false,
            true),
        row(
            "WebhookDeliveries.retryFailed",
            qeetId -> qeetId.webhooks().deliveries().retryFailed("p1", "p2"),
            "POST",
            "/v1/tenants/p1/webhooks/p2/deliveries/retry",
            null,
            null,
            false,
            true),
        row(
            "WebhookDeliveries.listAttempts",
            qeetId -> qeetId.webhooks().deliveries().listAttempts("p1", "p2", "p3"),
            "GET",
            "/v1/tenants/p1/webhooks/p2/deliveries/p3/attempts",
            null,
            null,
            false,
            true),
        row(
            "Webhooks.create",
            qeetId ->
                qeetId
                    .webhooks()
                    .create(WebhookCreateParams.builder().url("v").events(List.of("v")).build()),
            "POST",
            "/v1/webhooks",
            null,
            "{\"url\":\"v\",\"events\":[\"v\"]}",
            true,
            true),
        row(
            "Webhooks.get",
            qeetId -> qeetId.webhooks().get("p1"),
            "GET",
            "/v1/webhooks/p1",
            null,
            null,
            false,
            true),
        row(
            "Webhooks.list",
            qeetId -> qeetId.webhooks().list("p1"),
            "GET",
            "/v1/tenants/p1/webhooks",
            null,
            null,
            false,
            true),
        row(
            "Webhooks.update",
            qeetId -> qeetId.webhooks().update("p1", "p2", WebhookUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/webhooks/p2",
            null,
            "{}",
            false,
            true),
        row(
            "Webhooks.disable",
            qeetId -> qeetId.webhooks().disable("p1"),
            "DELETE",
            "/v1/webhooks/p1",
            null,
            null,
            false,
            true),
        row(
            "Webhooks.enable",
            qeetId -> qeetId.webhooks().enable("p1", "p2"),
            "POST",
            "/v1/tenants/p1/webhooks/p2/enable",
            null,
            null,
            false,
            true),
        row(
            "Webhooks.rotateSecret",
            qeetId -> qeetId.webhooks().rotateSecret("p1", "p2"),
            "POST",
            "/v1/tenants/p1/webhooks/p2/rotate-secret",
            null,
            null,
            false,
            true),
        row(
            "Webhooks.test",
            qeetId -> qeetId.webhooks().test("p1"),
            "POST",
            "/v1/webhooks/p1/test",
            null,
            null,
            false,
            true),
        row(
            "Webhooks.listEventTypes",
            qeetId -> qeetId.webhooks().listEventTypes("p1"),
            "GET",
            "/v1/tenants/p1/webhooks/events",
            null,
            null,
            false,
            true),
        row(
            "AdminPortalLinks.create",
            qeetId ->
                qeetId
                    .adminPortalLinks()
                    .create(
                        "p1",
                        AdminPortalLinkCreateParams.builder().capabilities(List.of("v")).build()),
            "POST",
            "/v1/tenants/p1/admin-portal/links",
            null,
            "{\"capabilities\":[\"v\"]}",
            false,
            true),
        row(
            "AdminPortalLinks.list",
            qeetId -> qeetId.adminPortalLinks().list("p1"),
            "GET",
            "/v1/tenants/p1/admin-portal/links",
            null,
            null,
            false,
            true),
        row(
            "AdminPortalLinks.revoke",
            qeetId -> qeetId.adminPortalLinks().revoke("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/admin-portal/links/p2",
            null,
            null,
            false,
            true),
        row(
            "OAuthResources.list",
            qeetId -> qeetId.applications().resources().list("p1"),
            "GET",
            "/v1/tenants/p1/oidc/resources",
            null,
            null,
            false,
            true),
        row(
            "OAuthResources.create",
            qeetId ->
                qeetId
                    .applications()
                    .resources()
                    .create("p1", OAuthResourceCreateParams.builder().identifier("v").build()),
            "POST",
            "/v1/tenants/p1/oidc/resources",
            null,
            "{\"identifier\":\"v\"}",
            false,
            true),
        row(
            "OAuthResources.delete",
            qeetId -> qeetId.applications().resources().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/oidc/resources/p2",
            null,
            null,
            false,
            true),
        row(
            "Applications.get",
            qeetId -> qeetId.applications().get("p1", "p2"),
            "GET",
            "/v1/tenants/p1/oidc/clients/p2",
            null,
            null,
            false,
            true),
        row(
            "Applications.list",
            qeetId -> qeetId.applications().list("p1"),
            "GET",
            "/v1/tenants/p1/oidc/clients",
            null,
            null,
            false,
            true),
        row(
            "Applications.update",
            qeetId ->
                qeetId.applications().update("p1", "p2", ApplicationUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/oidc/clients/p2",
            null,
            "{}",
            false,
            true),
        row(
            "Applications.delete",
            qeetId -> qeetId.applications().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/oidc/clients/p2",
            null,
            null,
            false,
            true),
        row(
            "Applications.rotateSecret",
            qeetId -> qeetId.applications().rotateSecret("p1", "p2"),
            "POST",
            "/v1/tenants/p1/oidc/clients/p2/rotate-secret",
            null,
            null,
            false,
            true),
        row(
            "Applications.getAuthFailures",
            qeetId -> qeetId.applications().getAuthFailures("p1", "p2"),
            "GET",
            "/v1/tenants/p1/oidc/clients/p2/auth-failures",
            null,
            null,
            false,
            true),
        row(
            "Applications.listShadowAi",
            qeetId -> qeetId.applications().listShadowAi("p1"),
            "GET",
            "/v1/tenants/p1/oidc/clients/shadow-ai",
            null,
            null,
            false,
            true),
        row(
            "Applications.markReviewed",
            qeetId -> qeetId.applications().markReviewed("p1", "p2"),
            "POST",
            "/v1/tenants/p1/oidc/clients/p2/review",
            null,
            null,
            false,
            true),
        row(
            "LdapConnections.create",
            qeetId ->
                qeetId
                    .ldapConnections()
                    .create(
                        "p1",
                        LdapConnectionCreateParams.builder()
                            .name("v")
                            .serverUrl("v")
                            .bindDn("v")
                            .bindPassword("v")
                            .baseDn("v")
                            .build()),
            "POST",
            "/v1/tenants/p1/ldap",
            null,
            "{\"name\":\"v\",\"server_url\":\"v\",\"bind_dn\":\"v\",\"bind_password\":\"v\",\"base_dn\":\"v\"}",
            false,
            true),
        row(
            "LdapConnections.get",
            qeetId -> qeetId.ldapConnections().get("p1", "p2"),
            "GET",
            "/v1/tenants/p1/ldap/p2",
            null,
            null,
            false,
            true),
        row(
            "LdapConnections.list",
            qeetId -> qeetId.ldapConnections().list("p1"),
            "GET",
            "/v1/tenants/p1/ldap",
            null,
            null,
            false,
            true),
        row(
            "LdapConnections.update",
            qeetId ->
                qeetId
                    .ldapConnections()
                    .update("p1", "p2", LdapConnectionUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/ldap/p2",
            null,
            "{}",
            false,
            true),
        row(
            "LdapConnections.delete",
            qeetId -> qeetId.ldapConnections().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/ldap/p2",
            null,
            null,
            false,
            true),
        row(
            "LdapConnections.test",
            qeetId -> qeetId.ldapConnections().test("p1", "p2"),
            "POST",
            "/v1/tenants/p1/ldap/p2/test",
            null,
            null,
            false,
            true),
        row(
            "LdapConnections.listGroupMappings",
            qeetId -> qeetId.ldapConnections().listGroupMappings("p1", "p2"),
            "GET",
            "/v1/tenants/p1/ldap/p2/group-mappings",
            null,
            null,
            false,
            true),
        row(
            "LdapConnections.createGroupMapping",
            qeetId ->
                qeetId
                    .ldapConnections()
                    .createGroupMapping(
                        "p1",
                        "p2",
                        SsoGroupMappingCreateParams.builder().idpGroup("v").groupId("v").build()),
            "POST",
            "/v1/tenants/p1/ldap/p2/group-mappings",
            null,
            "{\"idp_group\":\"v\",\"group_id\":\"v\"}",
            false,
            true),
        row(
            "LdapConnections.deleteGroupMapping",
            qeetId -> qeetId.ldapConnections().deleteGroupMapping("p1", "p2", "p3"),
            "DELETE",
            "/v1/tenants/p1/ldap/p2/group-mappings/p3",
            null,
            null,
            false,
            true),
        row(
            "OAuthGrants.list without parameters",
            qeetId -> qeetId.oauth().grants().list("p1"),
            "GET",
            "/v1/tenants/p1/oauth/grants",
            null,
            null,
            false,
            true),
        row(
            "OAuthGrants.list",
            qeetId -> qeetId.oauth().grants().list("p1", OAuthGrantListParams.builder().build()),
            "GET",
            "/v1/tenants/p1/oauth/grants",
            null,
            null,
            false,
            true),
        row(
            "OAuthGrants.revoke",
            qeetId -> qeetId.oauth().grants().revoke("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/oauth/grants/p2",
            null,
            null,
            false,
            true),
        row(
            "OAuthDevices.list",
            qeetId -> qeetId.oauth().devices().list("p1"),
            "GET",
            "/v1/tenants/p1/oauth/devices",
            null,
            null,
            false,
            true),
        row(
            "OAuthDevices.revoke",
            qeetId -> qeetId.oauth().devices().revoke("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/oauth/devices/p2",
            null,
            null,
            false,
            true),
        row(
            "SamlConnections.create",
            qeetId ->
                qeetId
                    .samlConnections()
                    .create("p1", SamlConnectionCreateParams.builder().name("v").build()),
            "POST",
            "/v1/tenants/p1/saml",
            null,
            "{\"name\":\"v\"}",
            false,
            true),
        row(
            "SamlConnections.createFromMetadata",
            qeetId ->
                qeetId
                    .samlConnections()
                    .createFromMetadata(
                        "p1", SamlConnectionImportParams.builder().name("v").build()),
            "POST",
            "/v1/tenants/p1/saml/import",
            null,
            "{\"name\":\"v\"}",
            false,
            true),
        row(
            "SamlConnections.get",
            qeetId -> qeetId.samlConnections().get("p1", "p2"),
            "GET",
            "/v1/tenants/p1/saml/p2",
            null,
            null,
            false,
            true),
        row(
            "SamlConnections.list",
            qeetId -> qeetId.samlConnections().list("p1"),
            "GET",
            "/v1/tenants/p1/saml",
            null,
            null,
            false,
            true),
        row(
            "SamlConnections.update",
            qeetId ->
                qeetId
                    .samlConnections()
                    .update("p1", "p2", SamlConnectionUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/saml/p2",
            null,
            "{}",
            false,
            true),
        row(
            "SamlConnections.delete",
            qeetId -> qeetId.samlConnections().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/saml/p2",
            null,
            null,
            false,
            true),
        row(
            "SamlConnections.updateMetadata",
            qeetId ->
                qeetId
                    .samlConnections()
                    .updateMetadata("p1", "p2", SamlMetadataParams.builder().build()),
            "POST",
            "/v1/tenants/p1/saml/p2/metadata",
            null,
            "{}",
            false,
            true),
        row(
            "SamlConnections.addCertificate",
            qeetId -> qeetId.samlConnections().addCertificate("p1", "p2", "v"),
            "POST",
            "/v1/tenants/p1/saml/p2/certificates",
            null,
            "{\"certificate\":\"v\"}",
            false,
            true),
        row(
            "SamlConnections.removeCertificate",
            qeetId -> qeetId.samlConnections().removeCertificate("p1", "p2", "p3"),
            "DELETE",
            "/v1/tenants/p1/saml/p2/certificates/p3",
            null,
            null,
            false,
            true),
        row(
            "SamlConnections.test",
            qeetId -> qeetId.samlConnections().test("p1", "p2"),
            "POST",
            "/v1/tenants/p1/saml/p2/test",
            null,
            null,
            false,
            true),
        row(
            "SamlConnections.listGroupMappings",
            qeetId -> qeetId.samlConnections().listGroupMappings("p1", "p2"),
            "GET",
            "/v1/tenants/p1/saml/p2/group-mappings",
            null,
            null,
            false,
            true),
        row(
            "SamlConnections.createGroupMapping",
            qeetId ->
                qeetId
                    .samlConnections()
                    .createGroupMapping(
                        "p1",
                        "p2",
                        SsoGroupMappingCreateParams.builder().idpGroup("v").groupId("v").build()),
            "POST",
            "/v1/tenants/p1/saml/p2/group-mappings",
            null,
            "{\"idp_group\":\"v\",\"group_id\":\"v\"}",
            false,
            true),
        row(
            "SamlConnections.deleteGroupMapping",
            qeetId -> qeetId.samlConnections().deleteGroupMapping("p1", "p2", "p3"),
            "DELETE",
            "/v1/tenants/p1/saml/p2/group-mappings/p3",
            null,
            null,
            false,
            true),
        row(
            "SamlServiceProviders.create",
            qeetId ->
                qeetId
                    .samlServiceProviders()
                    .create(
                        "p1",
                        SamlServiceProviderCreateParams.builder()
                            .name("v")
                            .entityId("v")
                            .acsUrl("v")
                            .build()),
            "POST",
            "/v1/tenants/p1/saml-providers",
            null,
            "{\"name\":\"v\",\"entity_id\":\"v\",\"acs_url\":\"v\"}",
            false,
            true),
        row(
            "SamlServiceProviders.get",
            qeetId -> qeetId.samlServiceProviders().get("p1", "p2"),
            "GET",
            "/v1/tenants/p1/saml-providers/p2",
            null,
            null,
            false,
            true),
        row(
            "SamlServiceProviders.list",
            qeetId -> qeetId.samlServiceProviders().list("p1"),
            "GET",
            "/v1/tenants/p1/saml-providers",
            null,
            null,
            false,
            true),
        row(
            "SamlServiceProviders.update",
            qeetId ->
                qeetId
                    .samlServiceProviders()
                    .update("p1", "p2", SamlServiceProviderUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/saml-providers/p2",
            null,
            "{}",
            false,
            true),
        row(
            "SamlServiceProviders.delete",
            qeetId -> qeetId.samlServiceProviders().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/saml-providers/p2",
            null,
            null,
            false,
            true),
        row(
            "SamlServiceProviders.listAssignments",
            qeetId -> qeetId.samlServiceProviders().listAssignments("p1", "p2"),
            "GET",
            "/v1/tenants/p1/saml-providers/p2/assignments",
            null,
            null,
            false,
            true),
        row(
            "SamlServiceProviders.assignUser",
            qeetId -> qeetId.samlServiceProviders().assignUser("p1", "p2", "p3"),
            "POST",
            "/v1/tenants/p1/saml-providers/p2/assignments/users/p3",
            null,
            null,
            false,
            true),
        row(
            "SamlServiceProviders.unassignUser",
            qeetId -> qeetId.samlServiceProviders().unassignUser("p1", "p2", "p3"),
            "DELETE",
            "/v1/tenants/p1/saml-providers/p2/assignments/users/p3",
            null,
            null,
            false,
            true),
        row(
            "SamlServiceProviders.assignGroup",
            qeetId -> qeetId.samlServiceProviders().assignGroup("p1", "p2", "p3"),
            "POST",
            "/v1/tenants/p1/saml-providers/p2/assignments/groups/p3",
            null,
            null,
            false,
            true),
        row(
            "SamlServiceProviders.unassignGroup",
            qeetId -> qeetId.samlServiceProviders().unassignGroup("p1", "p2", "p3"),
            "DELETE",
            "/v1/tenants/p1/saml-providers/p2/assignments/groups/p3",
            null,
            null,
            false,
            true),
        row(
            "Scim.getConfig",
            qeetId -> qeetId.scim().getConfig("p1"),
            "GET",
            "/v1/tenants/p1/scim",
            null,
            null,
            false,
            true),
        row(
            "Scim.listTokens",
            qeetId -> qeetId.scim().listTokens("p1"),
            "GET",
            "/v1/tenants/p1/scim/tokens",
            null,
            null,
            false,
            true),
        row(
            "Scim.createToken",
            qeetId ->
                qeetId.scim().createToken("p1", ScimTokenCreateParams.builder().name("v").build()),
            "POST",
            "/v1/tenants/p1/scim/tokens",
            null,
            "{\"name\":\"v\"}",
            false,
            true),
        row(
            "Scim.rotateToken without parameters",
            qeetId -> qeetId.scim().rotateToken("p1", "p2"),
            "POST",
            "/v1/tenants/p1/scim/tokens/p2/rotate",
            null,
            "{}",
            false,
            true),
        row(
            "Scim.rotateToken",
            qeetId ->
                qeetId.scim().rotateToken("p1", "p2", ScimTokenRotateParams.builder().build()),
            "POST",
            "/v1/tenants/p1/scim/tokens/p2/rotate",
            null,
            "{}",
            false,
            true),
        row(
            "Scim.revokeToken",
            qeetId -> qeetId.scim().revokeToken("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/scim/tokens/p2",
            null,
            null,
            false,
            true),
        row(
            "Scim.revokeAllTokens",
            qeetId -> qeetId.scim().revokeAllTokens("p1"),
            "DELETE",
            "/v1/tenants/p1/scim/token",
            null,
            null,
            false,
            true),
        row(
            "Scim.listUsers without parameters",
            qeetId -> qeetId.scim().listUsers("p1"),
            "GET",
            "/v1/tenants/p1/scim/users",
            null,
            null,
            false,
            true),
        row(
            "Scim.listUsers",
            qeetId -> qeetId.scim().listUsers("p1", PageParams.builder().build()),
            "GET",
            "/v1/tenants/p1/scim/users",
            null,
            null,
            false,
            true),
        row(
            "Scim.listGroups without parameters",
            qeetId -> qeetId.scim().listGroups("p1"),
            "GET",
            "/v1/tenants/p1/scim/groups",
            null,
            null,
            false,
            true),
        row(
            "Scim.listGroups",
            qeetId -> qeetId.scim().listGroups("p1", PageParams.builder().build()),
            "GET",
            "/v1/tenants/p1/scim/groups",
            null,
            null,
            false,
            true),
        row(
            "ServicePrincipals.create",
            qeetId ->
                qeetId
                    .servicePrincipals()
                    .create(ServicePrincipalCreateParams.builder().tenantId("v").name("v").build()),
            "POST",
            "/v1/service-principals",
            null,
            "{\"tenant_id\":\"v\",\"name\":\"v\"}",
            true,
            true),
        row(
            "ServicePrincipals.list",
            qeetId -> qeetId.servicePrincipals().list("p1"),
            "GET",
            "/v1/tenants/p1/service-principals",
            null,
            null,
            false,
            true),
        row(
            "ServicePrincipals.disable",
            qeetId -> qeetId.servicePrincipals().disable("p1"),
            "DELETE",
            "/v1/service-principals/p1",
            null,
            null,
            false,
            true),
        row(
            "ServicePrincipals.enable",
            qeetId -> qeetId.servicePrincipals().enable("p1", "p2"),
            "POST",
            "/v1/tenants/p1/service-principals/p2/enable",
            null,
            null,
            false,
            true),
        row(
            "ServicePrincipals.rotateSecret without parameters",
            qeetId -> qeetId.servicePrincipals().rotateSecret("p1", "p2"),
            "POST",
            "/v1/tenants/p1/service-principals/p2/rotate-secret",
            null,
            "{}",
            false,
            true),
        row(
            "ServicePrincipals.rotateSecret",
            qeetId ->
                qeetId
                    .servicePrincipals()
                    .rotateSecret("p1", "p2", ServicePrincipalRotateSecretParams.builder().build()),
            "POST",
            "/v1/tenants/p1/service-principals/p2/rotate-secret",
            null,
            "{}",
            false,
            true),
        row(
            "SocialProviders.list",
            qeetId -> qeetId.socialProviders().list("p1"),
            "GET",
            "/v1/tenants/p1/social/providers",
            null,
            null,
            false,
            true),
        row(
            "SocialProviders.upsert",
            qeetId ->
                qeetId
                    .socialProviders()
                    .upsert(
                        SocialProviderUpsertParams.builder().provider("v").clientId("v").build()),
            "POST",
            "/v1/social/providers",
            null,
            "{\"provider\":\"v\",\"client_id\":\"v\"}",
            false,
            true),
        row(
            "SocialProviders.update",
            qeetId ->
                qeetId.socialProviders().update("p1", SocialProviderUpdateParams.builder().build()),
            "PATCH",
            "/v1/social/providers/p1",
            null,
            "{}",
            false,
            true),
        row(
            "SocialProviders.delete",
            qeetId -> qeetId.socialProviders().delete("p1"),
            "DELETE",
            "/v1/social/providers/p1",
            null,
            null,
            false,
            true),
        row(
            "SocialProviders.unlinkIdentity",
            qeetId -> qeetId.socialProviders().unlinkIdentity("p1"),
            "DELETE",
            "/v1/social/identities/p1",
            null,
            null,
            false,
            true),
        row(
            "Branding.get",
            qeetId -> qeetId.branding().get("p1"),
            "GET",
            "/v1/tenants/p1/branding",
            null,
            null,
            false,
            true),
        row(
            "Branding.update",
            qeetId -> qeetId.branding().update("p1", BrandingUpdateParams.builder().build()),
            "PUT",
            "/v1/tenants/p1/branding",
            null,
            "{}",
            false,
            true),
        row(
            "Domains.create",
            qeetId -> qeetId.domains().create("p1", "v"),
            "POST",
            "/v1/tenants/p1/domains",
            null,
            "{\"domain\":\"v\"}",
            false,
            true),
        row(
            "Domains.list",
            qeetId -> qeetId.domains().list("p1"),
            "GET",
            "/v1/tenants/p1/domains",
            null,
            null,
            false,
            true),
        row(
            "Domains.verify",
            qeetId -> qeetId.domains().verify("p1", "p2"),
            "POST",
            "/v1/tenants/p1/domains/p2/verify",
            null,
            null,
            false,
            true),
        row(
            "Domains.update",
            qeetId -> qeetId.domains().update("p1", "p2", DomainUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1/domains/p2",
            null,
            "{}",
            false,
            true),
        row(
            "Domains.delete",
            qeetId -> qeetId.domains().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/domains/p2",
            null,
            null,
            false,
            true),
        row(
            "Groups.create",
            qeetId ->
                qeetId.groups().create(GroupCreateParams.builder().tenantId("v").name("v").build()),
            "POST",
            "/v1/groups",
            null,
            "{\"tenant_id\":\"v\",\"name\":\"v\"}",
            true,
            true),
        row(
            "Groups.update",
            qeetId -> qeetId.groups().update("p1", GroupUpdateParams.builder().build()),
            "PATCH",
            "/v1/groups/p1",
            null,
            "{}",
            false,
            true),
        row(
            "Groups.delete",
            qeetId -> qeetId.groups().delete("p1"),
            "DELETE",
            "/v1/groups/p1",
            null,
            null,
            false,
            true),
        row(
            "Groups.list without parameters",
            qeetId -> qeetId.groups().list("p1"),
            "GET",
            "/v1/tenants/p1/groups",
            null,
            null,
            false,
            true),
        row(
            "Groups.list",
            qeetId -> qeetId.groups().list("p1", GroupListParams.builder().build()),
            "GET",
            "/v1/tenants/p1/groups",
            null,
            null,
            false,
            true),
        row(
            "Groups.listMembers",
            qeetId -> qeetId.groups().listMembers("p1"),
            "GET",
            "/v1/groups/p1/members",
            null,
            null,
            false,
            true),
        row(
            "Groups.addMember",
            qeetId -> qeetId.groups().addMember("p1", "p2"),
            "POST",
            "/v1/groups/p1/members/p2",
            null,
            null,
            false,
            true),
        row(
            "Groups.removeMember",
            qeetId -> qeetId.groups().removeMember("p1", "p2"),
            "DELETE",
            "/v1/groups/p1/members/p2",
            null,
            null,
            false,
            true),
        row(
            "Groups.listRoles",
            qeetId -> qeetId.groups().listRoles("p1", "p2"),
            "GET",
            "/v1/tenants/p1/groups/p2/roles",
            null,
            null,
            false,
            true),
        row(
            "Groups.grantRole",
            qeetId -> qeetId.groups().grantRole("p1", "p2", "p3"),
            "POST",
            "/v1/tenants/p1/groups/p2/roles/p3",
            null,
            null,
            false,
            true),
        row(
            "Groups.revokeRole",
            qeetId -> qeetId.groups().revokeRole("p1", "p2", "p3"),
            "DELETE",
            "/v1/tenants/p1/groups/p2/roles/p3",
            null,
            null,
            false,
            true),
        row(
            "Invitations.create",
            qeetId ->
                qeetId
                    .invitations()
                    .create(InvitationCreateParams.builder().tenantId("v").email("v").build()),
            "POST",
            "/v1/invites",
            null,
            "{\"tenant_id\":\"v\",\"email\":\"v\"}",
            true,
            true),
        row(
            "Invitations.list",
            qeetId -> qeetId.invitations().list("p1"),
            "GET",
            "/v1/tenants/p1/invites",
            null,
            null,
            false,
            true),
        row(
            "Invitations.resend",
            qeetId -> qeetId.invitations().resend("p1"),
            "POST",
            "/v1/invites/p1/resend",
            null,
            null,
            false,
            true),
        row(
            "Invitations.delete",
            qeetId -> qeetId.invitations().delete("p1"),
            "DELETE",
            "/v1/invites/p1",
            null,
            null,
            false,
            true),
        row(
            "Tenants.get",
            qeetId -> qeetId.tenants().get("p1"),
            "GET",
            "/v1/tenants/p1",
            null,
            null,
            false,
            true),
        row(
            "Tenants.update",
            qeetId -> qeetId.tenants().update("p1", TenantUpdateParams.builder().build()),
            "PATCH",
            "/v1/tenants/p1",
            null,
            "{}",
            false,
            true),
        row(
            "Tenants.delete",
            qeetId -> qeetId.tenants().delete("p1"),
            "DELETE",
            "/v1/tenants/p1",
            null,
            null,
            false,
            true),
        row(
            "Tenants.cancelDeletion",
            qeetId -> qeetId.tenants().cancelDeletion("p1"),
            "POST",
            "/v1/tenants/p1/deletion/cancel",
            null,
            null,
            false,
            true),
        row(
            "Users.create",
            qeetId ->
                qeetId.users().create(UserCreateParams.builder().tenantId("v").email("v").build()),
            "POST",
            "/v1/users",
            null,
            "{\"tenant_id\":\"v\",\"email\":\"v\"}",
            true,
            true),
        row(
            "Users.get",
            qeetId -> qeetId.users().get("p1"),
            "GET",
            "/v1/users/p1",
            null,
            null,
            false,
            true),
        row(
            "Users.update",
            qeetId -> qeetId.users().update("p1", UserUpdateParams.builder().build()),
            "PATCH",
            "/v1/users/p1",
            null,
            "{}",
            false,
            true),
        row(
            "Users.list without parameters",
            qeetId -> qeetId.users().list(),
            "GET",
            "/v1/users",
            null,
            null,
            false,
            true),
        row(
            "Users.list",
            qeetId -> qeetId.users().list(UserListParams.builder().build()),
            "GET",
            "/v1/users",
            null,
            null,
            false,
            true),
        row(
            "Users.reactivate",
            qeetId -> qeetId.users().reactivate("p1"),
            "POST",
            "/v1/users/p1/reactivate",
            null,
            null,
            false,
            true),
        row(
            "Users.delete",
            qeetId -> qeetId.users().delete("p1"),
            "DELETE",
            "/v1/users/p1",
            null,
            null,
            false,
            true),
        row(
            "Users.listDeleted without parameters",
            qeetId -> qeetId.users().listDeleted(),
            "GET",
            "/v1/users/deleted",
            null,
            null,
            false,
            true),
        row(
            "Users.listDeleted",
            qeetId -> qeetId.users().listDeleted(DeletedUserListParams.builder().build()),
            "GET",
            "/v1/users/deleted",
            null,
            null,
            false,
            true),
        row(
            "Users.restore",
            qeetId -> qeetId.users().restore("p1"),
            "POST",
            "/v1/users/p1/restore",
            null,
            null,
            false,
            true),
        row(
            "Users.purge",
            qeetId -> qeetId.users().purge("p1"),
            "DELETE",
            "/v1/users/p1/purge",
            null,
            null,
            false,
            true),
        row(
            "Users.setPassword",
            qeetId -> qeetId.users().setPassword("p1", "v"),
            "POST",
            "/v1/users/p1/password",
            null,
            "{\"password\":\"v\"}",
            false,
            true),
        row(
            "Users.resetMfa",
            qeetId -> qeetId.users().resetMfa("p1"),
            "DELETE",
            "/v1/users/p1/mfa",
            null,
            null,
            false,
            true),
        row(
            "Users.setMfaRequired",
            qeetId -> qeetId.users().setMfaRequired("p1", true),
            "PUT",
            "/v1/users/p1/mfa-required",
            null,
            "{\"required\":true}",
            false,
            true),
        row(
            "Users.bulkCreate",
            qeetId ->
                qeetId
                    .users()
                    .bulkCreate(
                        UserBulkCreateParams.builder()
                            .users(List.of(BulkUserRow.builder().email("v").build()))
                            .build()),
            "POST",
            "/v1/users/bulk",
            null,
            "{\"users\":[{\"email\":\"v\"}]}",
            false,
            true),
        row(
            "Users.listSessions",
            qeetId -> qeetId.users().listSessions("p1"),
            "GET",
            "/v1/users/p1/sessions",
            null,
            null,
            false,
            true),
        row(
            "Users.revokeSession",
            qeetId -> qeetId.users().revokeSession("p1", "p2", "p3"),
            "DELETE",
            "/v1/tenants/p1/users/p2/sessions/p3",
            null,
            null,
            false,
            true),
        row(
            "Users.revokeAllSessions",
            qeetId -> qeetId.users().revokeAllSessions("p1"),
            "POST",
            "/v1/users/p1/sessions/revoke-all",
            null,
            null,
            false,
            true),
        row(
            "Users.getSecurity",
            qeetId -> qeetId.users().getSecurity("p1"),
            "GET",
            "/v1/users/p1/security",
            null,
            null,
            false,
            true),
        row(
            "Users.getAccess",
            qeetId -> qeetId.users().getAccess("p1"),
            "GET",
            "/v1/users/p1/access",
            null,
            null,
            false,
            true),
        row(
            "Users.removeMembership",
            qeetId -> qeetId.users().removeMembership("p1"),
            "DELETE",
            "/v1/users/p1/membership",
            null,
            null,
            false,
            true),
        row(
            "Users.listLinkedIdentities",
            qeetId -> qeetId.users().listLinkedIdentities("p1"),
            "GET",
            "/v1/users/p1/social/identities",
            null,
            null,
            false,
            true),
        row(
            "Analytics.getOverview without parameters",
            qeetId -> qeetId.analytics().getOverview("p1"),
            "GET",
            "/v1/tenants/p1/analytics/overview",
            null,
            null,
            false,
            true),
        row(
            "Analytics.getOverview",
            qeetId ->
                qeetId.analytics().getOverview("p1", AnalyticsOverviewParams.builder().build()),
            "GET",
            "/v1/tenants/p1/analytics/overview",
            null,
            null,
            false,
            true),
        row(
            "AuditAnomalies.list without parameters",
            qeetId -> qeetId.auditLogs().anomalies().list("p1"),
            "GET",
            "/v1/tenants/p1/audit/anomalies",
            null,
            null,
            false,
            true),
        row(
            "AuditAnomalies.list",
            qeetId ->
                qeetId.auditLogs().anomalies().list("p1", AuditAnomalyListParams.builder().build()),
            "GET",
            "/v1/tenants/p1/audit/anomalies",
            null,
            null,
            false,
            true),
        row(
            "AuditAnomalies.getSummary",
            qeetId -> qeetId.auditLogs().anomalies().getSummary("p1"),
            "GET",
            "/v1/tenants/p1/audit/anomalies/summary",
            null,
            null,
            false,
            true),
        row(
            "AuditAnomalies.resolve",
            qeetId -> qeetId.auditLogs().anomalies().resolve("p1", "p2"),
            "POST",
            "/v1/tenants/p1/audit/anomalies/p2/resolve",
            null,
            null,
            false,
            true),
        row(
            "AuditAnomalies.getSettings",
            qeetId -> qeetId.auditLogs().anomalies().getSettings("p1"),
            "GET",
            "/v1/tenants/p1/audit/anomaly-settings",
            null,
            null,
            false,
            true),
        row(
            "AuditAnomalies.updateSettings",
            qeetId ->
                qeetId
                    .auditLogs()
                    .anomalies()
                    .updateSettings("p1", new AuditAnomalySettings(true, 0.5, 1)),
            "PUT",
            "/v1/tenants/p1/audit/anomaly-settings",
            null,
            "{\"enabled\":true,\"score_threshold\":0.5,\"min_baseline_events\":1}",
            false,
            true),
        row(
            "AuditLogs.list without parameters",
            qeetId -> qeetId.auditLogs().list("p1"),
            "GET",
            "/v1/tenants/p1/audit",
            null,
            null,
            false,
            true),
        row(
            "AuditLogs.list",
            qeetId -> qeetId.auditLogs().list("p1", AuditLogListParams.builder().build()),
            "GET",
            "/v1/tenants/p1/audit",
            null,
            null,
            false,
            true),
        row(
            "AuditLogs.verify",
            qeetId -> qeetId.auditLogs().verify("p1"),
            "GET",
            "/v1/tenants/p1/audit/verify",
            null,
            null,
            false,
            true),
        row(
            "AuditLogs.getVerificationStatus",
            qeetId -> qeetId.auditLogs().getVerificationStatus("p1"),
            "GET",
            "/v1/tenants/p1/audit/verification",
            null,
            null,
            false,
            true),
        row(
            "Billing.getSubscription",
            qeetId -> qeetId.billing().getSubscription("p1"),
            "GET",
            "/v1/tenants/p1/billing/subscription",
            null,
            null,
            false,
            true),
        row(
            "Billing.updateSubscription",
            qeetId ->
                qeetId
                    .billing()
                    .updateSubscription(
                        "p1",
                        SubscriptionUpdateParams.builder().planCode("v").currency("v").build()),
            "PUT",
            "/v1/tenants/p1/billing/subscription",
            null,
            "{\"plan_code\":\"v\",\"currency\":\"v\"}",
            false,
            true),
        row(
            "Billing.cancelSubscription",
            qeetId -> qeetId.billing().cancelSubscription("p1"),
            "POST",
            "/v1/tenants/p1/billing/subscription/cancel",
            null,
            null,
            false,
            true),
        row(
            "Billing.checkout",
            qeetId ->
                qeetId
                    .billing()
                    .checkout(
                        "p1",
                        CheckoutParams.builder()
                            .planCode("v")
                            .currency("v")
                            .successUrl("v")
                            .cancelUrl("v")
                            .build()),
            "POST",
            "/v1/tenants/p1/billing/checkout",
            null,
            "{\"plan_code\":\"v\",\"currency\":\"v\",\"success_url\":\"v\",\"cancel_url\":\"v\"}",
            true,
            true),
        row(
            "Billing.listInvoices",
            qeetId -> qeetId.billing().listInvoices("p1"),
            "GET",
            "/v1/tenants/p1/billing/invoices",
            null,
            null,
            false,
            true),
        row(
            "Billing.payInvoice",
            qeetId ->
                qeetId
                    .billing()
                    .payInvoice(
                        "p1",
                        "p2",
                        InvoicePayParams.builder().successUrl("v").cancelUrl("v").build()),
            "POST",
            "/v1/tenants/p1/billing/invoices/p2/pay",
            null,
            "{\"success_url\":\"v\",\"cancel_url\":\"v\"}",
            true,
            true),
        row(
            "Billing.getProfile",
            qeetId -> qeetId.billing().getProfile("p1"),
            "GET",
            "/v1/tenants/p1/billing/profile",
            null,
            null,
            false,
            true),
        row(
            "Billing.updateProfile",
            qeetId ->
                qeetId.billing().updateProfile("p1", BillingProfileUpdateParams.builder().build()),
            "PUT",
            "/v1/tenants/p1/billing/profile",
            null,
            "{}",
            false,
            true),
        row(
            "Billing.getEntitlements",
            qeetId -> qeetId.billing().getEntitlements("p1"),
            "GET",
            "/v1/tenants/p1/entitlements",
            null,
            null,
            false,
            true),
        row(
            "EmailTemplates.list",
            qeetId -> qeetId.emailTemplates().list("p1"),
            "GET",
            "/v1/tenants/p1/email-templates",
            null,
            null,
            false,
            true),
        row(
            "EmailTemplates.get",
            qeetId -> qeetId.emailTemplates().get("p1", "p2"),
            "GET",
            "/v1/tenants/p1/email-templates/p2",
            null,
            null,
            false,
            true),
        row(
            "EmailTemplates.update",
            qeetId ->
                qeetId
                    .emailTemplates()
                    .update("p1", "p2", new EmailTemplateContent("v", "v", null)),
            "PUT",
            "/v1/tenants/p1/email-templates/p2",
            null,
            "{\"subject\":\"v\",\"body\":\"v\"}",
            false,
            true),
        row(
            "EmailTemplates.reset",
            qeetId -> qeetId.emailTemplates().reset("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/email-templates/p2",
            null,
            null,
            false,
            true),
        row(
            "EmailTemplates.preview without parameters",
            qeetId -> qeetId.emailTemplates().preview("p1", "p2"),
            "POST",
            "/v1/tenants/p1/email-templates/p2/preview",
            null,
            "{}",
            false,
            true),
        row(
            "EmailTemplates.preview",
            qeetId ->
                qeetId
                    .emailTemplates()
                    .preview("p1", "p2", EmailTemplatePreviewParams.builder().build()),
            "POST",
            "/v1/tenants/p1/email-templates/p2/preview",
            null,
            "{}",
            false,
            true),
        row(
            "EmailTemplates.sendTest",
            qeetId ->
                qeetId
                    .emailTemplates()
                    .sendTest(
                        "p1",
                        "p2",
                        EmailTemplateTestParams.builder()
                            .draft(new EmailTemplateContent("v", "v", null))
                            .build()),
            "POST",
            "/v1/tenants/p1/email-templates/p2/test",
            null,
            "{\"draft\":{\"subject\":\"v\",\"body\":\"v\"}}",
            false,
            true),
        row(
            "GdprErasures.create",
            qeetId ->
                qeetId
                    .gdpr()
                    .erasures()
                    .create(
                        ErasureCreateParams.builder()
                            .tenantId("v")
                            .userId("v")
                            .reason("v")
                            .build()),
            "POST",
            "/v1/gdpr/purge",
            null,
            "{\"tenant_id\":\"v\",\"user_id\":\"v\",\"reason\":\"v\"}",
            false,
            true),
        row(
            "GdprErasures.list",
            qeetId -> qeetId.gdpr().erasures().list("p1"),
            "GET",
            "/v1/tenants/p1/gdpr/purge",
            null,
            null,
            false,
            true),
        row(
            "GdprErasures.cancel",
            qeetId -> qeetId.gdpr().erasures().cancel("p1"),
            "DELETE",
            "/v1/gdpr/purge/p1",
            null,
            null,
            false,
            true),
        row(
            "GdprExports.create",
            qeetId ->
                qeetId
                    .gdpr()
                    .exports()
                    .create(ExportCreateParams.builder().tenantId("v").userId("v").build()),
            "POST",
            "/v1/gdpr/export",
            null,
            "{\"tenant_id\":\"v\",\"user_id\":\"v\"}",
            false,
            true),
        row(
            "GdprExports.get",
            qeetId -> qeetId.gdpr().exports().get("p1", "p2"),
            "GET",
            "/v1/tenants/p1/gdpr/export/p2",
            null,
            null,
            false,
            true),
        row(
            "GdprExports.list",
            qeetId -> qeetId.gdpr().exports().list("p1"),
            "GET",
            "/v1/tenants/p1/gdpr/export",
            null,
            null,
            false,
            true),
        row(
            "LogSinks.create",
            qeetId ->
                qeetId
                    .logSinks()
                    .create("p1", LogSinkCreateParams.builder().type("v").endpoint("v").build()),
            "POST",
            "/v1/tenants/p1/log-sinks",
            null,
            "{\"type\":\"v\",\"endpoint\":\"v\"}",
            false,
            true),
        row(
            "LogSinks.list",
            qeetId -> qeetId.logSinks().list("p1"),
            "GET",
            "/v1/tenants/p1/log-sinks",
            null,
            null,
            false,
            true),
        row(
            "LogSinks.setEnabled",
            qeetId -> qeetId.logSinks().setEnabled("p1", "p2", true),
            "PATCH",
            "/v1/tenants/p1/log-sinks/p2",
            null,
            "{\"enabled\":true}",
            false,
            true),
        row(
            "LogSinks.delete",
            qeetId -> qeetId.logSinks().delete("p1", "p2"),
            "DELETE",
            "/v1/tenants/p1/log-sinks/p2",
            null,
            null,
            false,
            true),
        row(
            "LogSinks.test",
            qeetId -> qeetId.logSinks().test("p1", "p2"),
            "POST",
            "/v1/tenants/p1/log-sinks/p2/test",
            null,
            null,
            false,
            true),
        row(
            "LogSinks.testDestination",
            qeetId ->
                qeetId
                    .logSinks()
                    .testDestination(
                        "p1", LogSinkCreateParams.builder().type("v").endpoint("v").build()),
            "POST",
            "/v1/tenants/p1/log-sinks/test",
            null,
            "{\"type\":\"v\",\"endpoint\":\"v\"}",
            false,
            true),
        row(
            "RateLimits.get",
            qeetId -> qeetId.rateLimits().get("p1"),
            "GET",
            "/v1/tenants/p1/rate-limits",
            null,
            null,
            false,
            true),
        row(
            "RateLimits.update",
            qeetId -> qeetId.rateLimits().update("p1", RateLimitsUpdateParams.builder().build()),
            "PUT",
            "/v1/tenants/p1/rate-limits",
            null,
            "{}",
            false,
            true),
        row(
            "RateLimits.reset",
            qeetId -> qeetId.rateLimits().reset("p1"),
            "DELETE",
            "/v1/tenants/p1/rate-limits",
            null,
            null,
            false,
            true),
        row(
            "Retention.get",
            qeetId -> qeetId.retention().get("p1"),
            "GET",
            "/v1/tenants/p1/retention",
            null,
            null,
            false,
            true),
        row(
            "Retention.update",
            qeetId ->
                qeetId
                    .retention()
                    .update(
                        "p1",
                        RetentionPolicyUpdateParams.builder()
                            .deletedUsersEnabled(true)
                            .deletedUsersDays(1)
                            .build()),
            "PUT",
            "/v1/tenants/p1/retention",
            null,
            "{\"deleted_users_enabled\":true,\"deleted_users_days\":1}",
            false,
            true),
        row(
            "Retention.preview",
            qeetId -> qeetId.retention().preview("p1"),
            "POST",
            "/v1/tenants/p1/retention/preview",
            null,
            null,
            false,
            true),
        row(
            "Retention.run",
            qeetId -> qeetId.retention().run("p1"),
            "POST",
            "/v1/tenants/p1/retention/run",
            null,
            null,
            false,
            true));
  }
}
