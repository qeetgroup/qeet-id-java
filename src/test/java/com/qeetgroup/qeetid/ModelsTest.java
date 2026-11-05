package com.qeetgroup.qeetid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qeetgroup.qeetid.access.Anomaly;
import com.qeetgroup.qeetid.access.AnomalyListParams;
import com.qeetgroup.qeetid.access.AnomalySummary;
import com.qeetgroup.qeetid.access.AuthPolicy;
import com.qeetgroup.qeetid.access.AuthPolicyUpdateParams;
import com.qeetgroup.qeetid.access.AuthPolicyUsage;
import com.qeetgroup.qeetid.access.AuthzenAction;
import com.qeetgroup.qeetid.access.AuthzenEntity;
import com.qeetgroup.qeetid.access.BotActivity;
import com.qeetgroup.qeetid.access.BotActivityParams;
import com.qeetgroup.qeetid.access.BotEvent;
import com.qeetgroup.qeetid.access.BotSettings;
import com.qeetgroup.qeetid.access.DecidedBy;
import com.qeetgroup.qeetid.access.DecisionListParams;
import com.qeetgroup.qeetid.access.DecisionLogEntry;
import com.qeetgroup.qeetid.access.EffectivePermissions;
import com.qeetgroup.qeetid.access.Evaluation;
import com.qeetgroup.qeetid.access.EvaluationParams;
import com.qeetgroup.qeetid.access.Evaluations;
import com.qeetgroup.qeetid.access.EvaluationsParams;
import com.qeetgroup.qeetid.access.GrantStep;
import com.qeetgroup.qeetid.access.IpRule;
import com.qeetgroup.qeetid.access.IpRuleCheck;
import com.qeetgroup.qeetid.access.IpRuleCreateParams;
import com.qeetgroup.qeetid.access.LockoutSettings;
import com.qeetgroup.qeetid.access.Permission;
import com.qeetgroup.qeetid.access.PermissionCheckParams;
import com.qeetgroup.qeetid.access.PermissionDecision;
import com.qeetgroup.qeetid.access.RelationCheckParams;
import com.qeetgroup.qeetid.access.RelationDecision;
import com.qeetgroup.qeetid.access.RelationGraph;
import com.qeetgroup.qeetid.access.RelationGraphParams;
import com.qeetgroup.qeetid.access.RelationObject;
import com.qeetgroup.qeetid.access.RelationObjectListParams;
import com.qeetgroup.qeetid.access.RelationObjectType;
import com.qeetgroup.qeetid.access.RelationTuple;
import com.qeetgroup.qeetid.access.RelationTupleCreateParams;
import com.qeetgroup.qeetid.access.RiskSettings;
import com.qeetgroup.qeetid.access.RiskSettingsUpdateParams;
import com.qeetgroup.qeetid.access.Role;
import com.qeetgroup.qeetid.access.RoleCreateParams;
import com.qeetgroup.qeetid.access.RoleHolders;
import com.qeetgroup.qeetid.access.RoleHoldersParams;
import com.qeetgroup.qeetid.access.RoleUpdateParams;
import com.qeetgroup.qeetid.access.SecurityPolicy;
import com.qeetgroup.qeetid.access.SecurityPolicyUpdateParams;
import com.qeetgroup.qeetid.developer.Agent;
import com.qeetgroup.qeetid.developer.AgentCreateParams;
import com.qeetgroup.qeetid.developer.AgentRotateSecretParams;
import com.qeetgroup.qeetid.developer.AgentSecret;
import com.qeetgroup.qeetid.developer.AgentToken;
import com.qeetgroup.qeetid.developer.ApiKey;
import com.qeetgroup.qeetid.developer.ApiKeyCreateParams;
import com.qeetgroup.qeetid.developer.ApiKeyCreated;
import com.qeetgroup.qeetid.developer.ApiKeyRotateParams;
import com.qeetgroup.qeetid.developer.AuthHook;
import com.qeetgroup.qeetid.developer.AuthHookCreateParams;
import com.qeetgroup.qeetid.developer.AuthHookState;
import com.qeetgroup.qeetid.developer.AuthHookTestResult;
import com.qeetgroup.qeetid.developer.AuthHookUpdateParams;
import com.qeetgroup.qeetid.developer.CredentialVerification;
import com.qeetgroup.qeetid.developer.IssuedCredential;
import com.qeetgroup.qeetid.developer.Secret;
import com.qeetgroup.qeetid.developer.SecretCreateParams;
import com.qeetgroup.qeetid.developer.SecretUpdateParams;
import com.qeetgroup.qeetid.developer.SecretValue;
import com.qeetgroup.qeetid.developer.TokenVaultProvider;
import com.qeetgroup.qeetid.developer.TokenVaultProviderCreateParams;
import com.qeetgroup.qeetid.developer.VerifiableCredential;
import com.qeetgroup.qeetid.developer.VerifiableCredentialIssueParams;
import com.qeetgroup.qeetid.developer.WebhookCreateParams;
import com.qeetgroup.qeetid.developer.WebhookDelivery;
import com.qeetgroup.qeetid.developer.WebhookDeliveryAttempt;
import com.qeetgroup.qeetid.developer.WebhookDeliveryListParams;
import com.qeetgroup.qeetid.developer.WebhookEventType;
import com.qeetgroup.qeetid.developer.WebhookSecret;
import com.qeetgroup.qeetid.developer.WebhookSubscription;
import com.qeetgroup.qeetid.developer.WebhookTestResult;
import com.qeetgroup.qeetid.developer.WebhookUpdateParams;
import com.qeetgroup.qeetid.federation.AdminPortalLink;
import com.qeetgroup.qeetid.federation.AdminPortalLinkCreateParams;
import com.qeetgroup.qeetid.federation.AdminPortalLinkCreated;
import com.qeetgroup.qeetid.federation.Application;
import com.qeetgroup.qeetid.federation.ApplicationAuthFailures;
import com.qeetgroup.qeetid.federation.ApplicationCreateParams;
import com.qeetgroup.qeetid.federation.ApplicationCreated;
import com.qeetgroup.qeetid.federation.ApplicationSecret;
import com.qeetgroup.qeetid.federation.ApplicationUpdateParams;
import com.qeetgroup.qeetid.federation.AuthorizationCodeGrantParams;
import com.qeetgroup.qeetid.federation.BackchannelAuthentication;
import com.qeetgroup.qeetid.federation.BackchannelAuthenticationParams;
import com.qeetgroup.qeetid.federation.CibaGrantParams;
import com.qeetgroup.qeetid.federation.ClientCredentialsToken;
import com.qeetgroup.qeetid.federation.ConnectionTestResult;
import com.qeetgroup.qeetid.federation.DeviceAuthorization;
import com.qeetgroup.qeetid.federation.DeviceAuthorizationParams;
import com.qeetgroup.qeetid.federation.DeviceCodeGrantParams;
import com.qeetgroup.qeetid.federation.LdapConnection;
import com.qeetgroup.qeetid.federation.LdapConnectionCreateParams;
import com.qeetgroup.qeetid.federation.LdapConnectionUpdateParams;
import com.qeetgroup.qeetid.federation.MfaChallenge;
import com.qeetgroup.qeetid.federation.MfaFactor;
import com.qeetgroup.qeetid.federation.OAuthDeviceAuthorization;
import com.qeetgroup.qeetid.federation.OAuthGrant;
import com.qeetgroup.qeetid.federation.OAuthGrantListParams;
import com.qeetgroup.qeetid.federation.OAuthResource;
import com.qeetgroup.qeetid.federation.OAuthResourceCreateParams;
import com.qeetgroup.qeetid.federation.RefreshTokenGrantParams;
import com.qeetgroup.qeetid.federation.SamlAttributeMapping;
import com.qeetgroup.qeetid.federation.SamlCertificate;
import com.qeetgroup.qeetid.federation.SamlConnection;
import com.qeetgroup.qeetid.federation.SamlConnectionCreateParams;
import com.qeetgroup.qeetid.federation.SamlConnectionImportParams;
import com.qeetgroup.qeetid.federation.SamlConnectionUpdateParams;
import com.qeetgroup.qeetid.federation.SamlMetadataParams;
import com.qeetgroup.qeetid.federation.SamlServiceProvider;
import com.qeetgroup.qeetid.federation.SamlServiceProviderAssignments;
import com.qeetgroup.qeetid.federation.SamlServiceProviderCreateParams;
import com.qeetgroup.qeetid.federation.SamlServiceProviderUpdateParams;
import com.qeetgroup.qeetid.federation.ScimConfig;
import com.qeetgroup.qeetid.federation.ScimGroup;
import com.qeetgroup.qeetid.federation.ScimToken;
import com.qeetgroup.qeetid.federation.ScimTokenCreateParams;
import com.qeetgroup.qeetid.federation.ScimTokenCreated;
import com.qeetgroup.qeetid.federation.ScimTokenRotateParams;
import com.qeetgroup.qeetid.federation.ScimTokenRotated;
import com.qeetgroup.qeetid.federation.ScimUser;
import com.qeetgroup.qeetid.federation.ServicePrincipal;
import com.qeetgroup.qeetid.federation.ServicePrincipalCreateParams;
import com.qeetgroup.qeetid.federation.ServicePrincipalCreated;
import com.qeetgroup.qeetid.federation.ServicePrincipalRotateSecretParams;
import com.qeetgroup.qeetid.federation.ServicePrincipalSecret;
import com.qeetgroup.qeetid.federation.ShadowAiApplication;
import com.qeetgroup.qeetid.federation.SigningKey;
import com.qeetgroup.qeetid.federation.SocialProvider;
import com.qeetgroup.qeetid.federation.SocialProviderUpdateParams;
import com.qeetgroup.qeetid.federation.SocialProviderUpsertParams;
import com.qeetgroup.qeetid.federation.SsoGroupMapping;
import com.qeetgroup.qeetid.federation.SsoGroupMappingCreateParams;
import com.qeetgroup.qeetid.federation.TokenExchangeGrantParams;
import com.qeetgroup.qeetid.federation.TokenPair;
import com.qeetgroup.qeetid.federation.TokenResponse;
import com.qeetgroup.qeetid.identity.BrandingUpdateParams;
import com.qeetgroup.qeetid.identity.BulkImportResult;
import com.qeetgroup.qeetid.identity.BulkImportRowError;
import com.qeetgroup.qeetid.identity.BulkUserRow;
import com.qeetgroup.qeetid.identity.DeletedUser;
import com.qeetgroup.qeetid.identity.DeletedUserListParams;
import com.qeetgroup.qeetid.identity.Domain;
import com.qeetgroup.qeetid.identity.DomainUpdateParams;
import com.qeetgroup.qeetid.identity.Group;
import com.qeetgroup.qeetid.identity.GroupCreateParams;
import com.qeetgroup.qeetid.identity.GroupListParams;
import com.qeetgroup.qeetid.identity.GroupMember;
import com.qeetgroup.qeetid.identity.GroupRole;
import com.qeetgroup.qeetid.identity.GroupUpdateParams;
import com.qeetgroup.qeetid.identity.Invitation;
import com.qeetgroup.qeetid.identity.InvitationCreateParams;
import com.qeetgroup.qeetid.identity.InvitationDelivery;
import com.qeetgroup.qeetid.identity.InvitationResult;
import com.qeetgroup.qeetid.identity.LinkedIdentity;
import com.qeetgroup.qeetid.identity.MembershipRemoval;
import com.qeetgroup.qeetid.identity.Tenant;
import com.qeetgroup.qeetid.identity.TenantBranding;
import com.qeetgroup.qeetid.identity.TenantDeletion;
import com.qeetgroup.qeetid.identity.TenantUpdateParams;
import com.qeetgroup.qeetid.identity.User;
import com.qeetgroup.qeetid.identity.UserAccess;
import com.qeetgroup.qeetid.identity.UserBulkCreateParams;
import com.qeetgroup.qeetid.identity.UserCreateParams;
import com.qeetgroup.qeetid.identity.UserDeletion;
import com.qeetgroup.qeetid.identity.UserListParams;
import com.qeetgroup.qeetid.identity.UserSecurity;
import com.qeetgroup.qeetid.identity.UserSession;
import com.qeetgroup.qeetid.identity.UserSessionsRevocation;
import com.qeetgroup.qeetid.identity.UserSuspension;
import com.qeetgroup.qeetid.identity.UserUpdateParams;
import com.qeetgroup.qeetid.internal.Json;
import com.qeetgroup.qeetid.operations.AnalyticsActivityPoint;
import com.qeetgroup.qeetid.operations.AnalyticsMetric;
import com.qeetgroup.qeetid.operations.AnalyticsOverview;
import com.qeetgroup.qeetid.operations.AnalyticsOverviewParams;
import com.qeetgroup.qeetid.operations.AnalyticsTrendPoint;
import com.qeetgroup.qeetid.operations.AuditAnomaly;
import com.qeetgroup.qeetid.operations.AuditAnomalyListParams;
import com.qeetgroup.qeetid.operations.AuditAnomalySettings;
import com.qeetgroup.qeetid.operations.AuditAnomalySummary;
import com.qeetgroup.qeetid.operations.AuditChainVerification;
import com.qeetgroup.qeetid.operations.AuditLogEntry;
import com.qeetgroup.qeetid.operations.AuditLogListParams;
import com.qeetgroup.qeetid.operations.AuditVerificationStatus;
import com.qeetgroup.qeetid.operations.BillingProfile;
import com.qeetgroup.qeetid.operations.BillingProfileUpdateParams;
import com.qeetgroup.qeetid.operations.CheckoutParams;
import com.qeetgroup.qeetid.operations.CheckoutResult;
import com.qeetgroup.qeetid.operations.EmailLocalizedContent;
import com.qeetgroup.qeetid.operations.EmailTemplate;
import com.qeetgroup.qeetid.operations.EmailTemplateContent;
import com.qeetgroup.qeetid.operations.EmailTemplateOptions;
import com.qeetgroup.qeetid.operations.EmailTemplatePreview;
import com.qeetgroup.qeetid.operations.EmailTemplatePreviewParams;
import com.qeetgroup.qeetid.operations.EmailTemplateTestParams;
import com.qeetgroup.qeetid.operations.Entitlements;
import com.qeetgroup.qeetid.operations.ErasureCreateParams;
import com.qeetgroup.qeetid.operations.ErasureRequest;
import com.qeetgroup.qeetid.operations.ExportCreateParams;
import com.qeetgroup.qeetid.operations.ExportRequest;
import com.qeetgroup.qeetid.operations.Invoice;
import com.qeetgroup.qeetid.operations.InvoicePayParams;
import com.qeetgroup.qeetid.operations.LogSink;
import com.qeetgroup.qeetid.operations.LogSinkCreateParams;
import com.qeetgroup.qeetid.operations.LogSinkTestResult;
import com.qeetgroup.qeetid.operations.PaymentMethod;
import com.qeetgroup.qeetid.operations.RateLimitBucket;
import com.qeetgroup.qeetid.operations.RateLimitsUpdateParams;
import com.qeetgroup.qeetid.operations.RetentionPolicy;
import com.qeetgroup.qeetid.operations.RetentionPolicyUpdateParams;
import com.qeetgroup.qeetid.operations.RetentionPreview;
import com.qeetgroup.qeetid.operations.Subscription;
import com.qeetgroup.qeetid.operations.SubscriptionUpdateParams;
import com.qeetgroup.qeetid.operations.TenantRateLimits;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Every record reads every field the API sends and writes it back under the same name, and every
 * builder sends each field under the API's name. Secrets never appear in {@code toString}.
 */
class ModelsTest {
  private static final ObjectMapper PLAIN = new ObjectMapper();

  record RecordRow(Class<?> type, String json, List<String> secrets) {
    @Override
    public String toString() {
      return type.getSimpleName();
    }
  }

  record ParamsRow(String name, Supplier<Params> build, String json, List<String> secrets) {
    @Override
    public String toString() {
      return name;
    }
  }

  private static RecordRow record(Class<?> type, String json, List<String> secrets) {
    return new RecordRow(type, json, secrets);
  }

  private static ParamsRow params(
      String name, Supplier<Params> build, String json, List<String> secrets) {
    return new ParamsRow(name, build, json, secrets);
  }

  @ParameterizedTest
  @MethodSource("records")
  void readsAndWritesEveryField(RecordRow row) throws Exception {
    Object value = Json.read(row.json().getBytes(StandardCharsets.UTF_8), Json.type(row.type()));
    JsonNode written = PLAIN.readTree(Json.write(value));
    assertEquals(PLAIN.readTree(row.json()), written);
    for (String secret : row.secrets()) {
      assertFalse(
          value.toString().contains(secret), row.type().getSimpleName() + " prints a secret");
    }
  }

  @ParameterizedTest
  @MethodSource("builders")
  void sendsEveryFieldUnderTheApisName(ParamsRow row) throws Exception {
    Params params = row.build().get();
    assertEquals(PLAIN.readTree(row.json()), PLAIN.readTree(Json.write(params)));
    for (String secret : row.secrets()) {
      assertFalse(params.toString().contains(secret), row.name() + " prints a secret");
    }
  }

  static Stream<RecordRow> records() {
    return Stream.of(
        record(
            AdminPortalLink.class,
            "{\"id\": \"s_id_1\", \"tenant_id\": \"s_tenant_id_2\", \"capabilities\": [\"s_capabilities_3\"], \"created_by\": \"s_created_by_4\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"revoked_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            AdminPortalLinkCreated.class,
            "{\"link\": {\"id\": \"s_id_6\", \"tenant_id\": \"s_tenant_id_7\", \"capabilities\": [\"s_capabilities_8\"], \"created_by\": \"s_created_by_9\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"revoked_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\"}, \"token\": \"s_token_10\", \"url\": \"s_url_11\"}",
            List.of("s_token_10", "s_url_11")),
        record(
            Agent.class,
            "{\"id\": \"s_id_12\", \"name\": \"s_name_13\", \"scopes\": [\"s_scopes_14\"], \"token_ttl_seconds\": 7, \"status\": \"s_status_15\", \"disabled\": true, \"sponsor_user_id\": \"s_sponsor_user_id_16\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"previous_secret_expires_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\", \"secret\": \"s_secret_17\"}",
            List.of("s_secret_17")),
        record(
            AgentSecret.class,
            "{\"id\": \"s_id_21\", \"secret\": \"s_secret_22\", \"previous_secret_expires_at\": \"2026-11-05T10:00:00Z\"}",
            List.of("s_secret_22")),
        record(
            AgentToken.class,
            "{\"access_token\": \"s_access_token_23\", \"token_type\": \"s_token_type_24\", \"expires_in\": 7, \"scope\": \"s_scope_25\"}",
            List.of("s_access_token_23")),
        record(
            AnalyticsActivityPoint.class,
            "{\"date\": \"s_date_26\", \"password\": 7, \"passkey\": 7, \"social\": 7, \"saml\": 7, \"oidc\": 7, \"ldap\": 7, \"magic_link\": 7}",
            List.of()),
        record(AnalyticsMetric.class, "{\"value\": 0.5, \"delta_pct\": 0.5}", List.of()),
        record(
            AnalyticsOverview.class,
            "{\"generated_at\": \"2026-11-05T10:00:00Z\", \"kpis\": {\"mau\": {\"value\": 0.5, \"delta_pct\": 0.5}, \"dau\": {\"value\": 0.5, \"delta_pct\": 0.5}, \"logins_today\": {\"value\": 0.5, \"delta_pct\": 0.5}, \"failed_logins_24h\": {\"value\": 0.5, \"delta_pct\": 0.5}, \"mfa_adoption_pct\": {\"value\": 0.5, \"delta_pct\": 0.5}, \"total_users\": {\"value\": 0.5, \"delta_pct\": 0.5}, \"avg_sessions_per_user\": {\"value\": 0.5, \"delta_pct\": 0.5}, \"stickiness_pct\": {\"value\": 0.5, \"delta_pct\": 0.5}}, \"weekly_activity_8w\": [{\"week\": \"s_week_27\", \"wau\": 7, \"dau\": 7}], \"user_trend_14d\": [{\"date\": \"s_date_28\", \"value\": 0.5}], \"login_trend_14d\": [{\"date\": \"s_date_29\", \"value\": 0.5}], \"mfa_trend_14d\": [{\"date\": \"s_date_30\", \"value\": 0.5}], \"failed_trend_14d\": [{\"date\": \"s_date_31\", \"value\": 0.5}], \"login_activity_14d\": [{\"date\": \"s_date_32\", \"password\": 7, \"passkey\": 7, \"social\": 7, \"saml\": 7, \"oidc\": 7, \"ldap\": 7, \"magic_link\": 7}], \"login_methods_mix\": [{\"method\": \"s_method_33\", \"value\": 0.5}], \"mfa_methods_adoption\": [{\"method\": \"s_method_34\", \"users\": 7}], \"failed_logins_hourly_24h\": [{\"hour\": \"s_hour_35\", \"attempts\": 7}], \"range\": {\"from\": \"s_from_36\", \"to\": \"s_to_37\", \"method\": \"s_method_38\", \"days\": [{\"date\": \"s_date_39\", \"sign_ins\": 7, \"people\": 7, \"failed\": 7}]}}",
            List.of()),
        record(AnalyticsTrendPoint.class, "{\"date\": \"s_date_43\", \"value\": 0.5}", List.of()),
        record(
            Anomaly.class,
            "{\"id\": \"s_id_44\", \"type\": \"s_type_45\", \"severity\": \"s_severity_46\", \"detail\": \"s_detail_47\", \"status\": \"s_status_48\", \"user_id\": \"s_user_id_49\", \"user_email\": \"s_user_email_50\", \"ip\": \"s_ip_51\", \"metadata\": {\"k\": \"v\"}, \"created_at\": \"2026-11-05T10:00:00Z\", \"resolved_at\": \"2026-11-05T10:00:00Z\", \"resolved_by\": \"s_resolved_by_52\", \"resolved_by_email\": \"s_resolved_by_email_53\"}",
            List.of()),
        record(
            AnomalySummary.class,
            "{\"open\": 7, \"resolved_24h\": 7, \"affected_accounts\": 7, \"high_severity_24h\": 7}",
            List.of()),
        record(
            ApiKey.class,
            "{\"id\": \"s_id_56\", \"tenant_id\": \"s_tenant_id_57\", \"user_id\": \"s_user_id_58\", \"name\": \"s_name_59\", \"prefix\": \"s_prefix_60\", \"scopes\": [\"s_scopes_61\"], \"expires_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"revoked_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\", \"created_by\": \"s_created_by_62\", \"rotated_from\": \"s_rotated_from_63\"}",
            List.of()),
        record(
            ApiKeyCreated.class,
            "{\"api_key\": {\"id\": \"s_id_68\", \"tenant_id\": \"s_tenant_id_69\", \"user_id\": \"s_user_id_70\", \"name\": \"s_name_71\", \"prefix\": \"s_prefix_72\", \"scopes\": [\"s_scopes_73\"], \"expires_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"revoked_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\", \"created_by\": \"s_created_by_74\", \"rotated_from\": \"s_rotated_from_75\"}, \"secret\": \"s_secret_76\", \"warning\": \"s_warning_77\"}",
            List.of("s_secret_76")),
        record(
            Application.class,
            "{\"id\": \"s_id_78\", \"tenant_id\": \"s_tenant_id_79\", \"client_id\": \"s_client_id_80\", \"type\": \"s_type_81\", \"name\": \"s_name_82\", \"redirect_uris\": [\"s_redirect_uris_83\"], \"post_logout_uris\": [\"s_post_logout_uris_84\"], \"grant_types\": [\"s_grant_types_85\"], \"scopes\": [\"s_scopes_86\"], \"created_at\": \"2026-11-05T10:00:00Z\", \"pkce_exempt_until\": \"2026-11-05T10:00:00Z\", \"backchannel_logout_uri\": \"s_backchannel_logout_uri_87\", \"frontchannel_logout_uri\": \"s_frontchannel_logout_uri_88\", \"logo_uri\": \"s_logo_uri_89\", \"client_uri\": \"s_client_uri_90\", \"web_origins\": [\"s_web_origins_91\"]}",
            List.of()),
        record(
            ApplicationAuthFailures.class,
            "{\"locked_until\": \"2026-11-05T10:00:00Z\", \"items\": [{\"at\": \"2026-11-05T10:00:00Z\", \"ip\": \"s_ip_92\", \"reason\": \"s_reason_93\"}]}",
            List.of()),
        record(
            ApplicationCreated.class,
            "{\"client\": {\"id\": \"s_id_105\", \"tenant_id\": \"s_tenant_id_106\", \"client_id\": \"s_client_id_107\", \"type\": \"s_type_108\", \"name\": \"s_name_109\", \"redirect_uris\": [\"s_redirect_uris_110\"], \"post_logout_uris\": [\"s_post_logout_uris_111\"], \"grant_types\": [\"s_grant_types_112\"], \"scopes\": [\"s_scopes_113\"], \"created_at\": \"2026-11-05T10:00:00Z\", \"pkce_exempt_until\": \"2026-11-05T10:00:00Z\", \"backchannel_logout_uri\": \"s_backchannel_logout_uri_114\", \"frontchannel_logout_uri\": \"s_frontchannel_logout_uri_115\", \"logo_uri\": \"s_logo_uri_116\", \"client_uri\": \"s_client_uri_117\", \"web_origins\": [\"s_web_origins_118\"]}, \"client_secret\": \"s_client_secret_119\", \"warning\": \"s_warning_120\"}",
            List.of("s_client_secret_119")),
        record(
            ApplicationSecret.class,
            "{\"client_secret\": \"s_client_secret_121\", \"warning\": \"s_warning_122\"}",
            List.of("s_client_secret_121")),
        record(
            AuditAnomaly.class,
            "{\"id\": \"s_id_133\", \"tenant_id\": \"s_tenant_id_134\", \"event_id\": \"s_event_id_135\", \"actor_user_id\": \"s_actor_user_id_136\", \"actor_email\": \"s_actor_email_137\", \"score\": 0.5, \"reasons\": [\"s_reasons_138\"], \"status\": \"s_status_139\", \"resolved_at\": \"2026-11-05T10:00:00Z\", \"resolved_by\": \"s_resolved_by_140\", \"action\": \"s_action_141\", \"resource_type\": \"s_resource_type_142\", \"ip\": \"s_ip_143\", \"event_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            AuditAnomalySettings.class,
            "{\"enabled\": true, \"score_threshold\": 0.5, \"min_baseline_events\": 7}",
            List.of()),
        record(
            AuditAnomalySummary.class,
            "{\"open\": 7, \"high_score_open\": 7, \"resolved_7d\": 7}",
            List.of()),
        record(
            AuditChainVerification.class,
            "{\"ok\": true, \"rows_checked\": 7, \"last_verified_id\": \"s_last_verified_id_145\", \"broken_at_id\": \"s_broken_at_id_146\", \"broken_reason\": \"s_broken_reason_147\", \"reason_code\": \"s_reason_code_148\"}",
            List.of()),
        record(
            AuditLogEntry.class,
            "{\"id\": \"s_id_149\", \"tenant_id\": \"s_tenant_id_150\", \"action\": \"s_action_151\", \"actor_type\": \"s_actor_type_152\", \"actor_user_id\": \"s_actor_user_id_153\", \"resource_type\": \"s_resource_type_154\", \"resource_id\": \"s_resource_id_155\", \"ip\": \"s_ip_156\", \"user_agent\": \"s_user_agent_157\", \"request_id\": \"s_request_id_158\", \"metadata\": {\"k\": \"v\"}, \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            AuditVerificationStatus.class,
            "{\"verified\": true, \"intact\": true, \"last_verified_at\": \"2026-11-05T10:00:00Z\", \"broken_at\": \"2026-11-05T10:00:00Z\", \"last_verified_event_id\": \"s_last_verified_event_id_168\", \"last_run_at\": \"2026-11-05T10:00:00Z\", \"last_run_scope\": \"s_last_run_scope_169\"}",
            List.of()),
        record(
            AuthHook.class,
            "{\"id\": \"s_id_170\", \"trigger\": \"s_trigger_171\", \"url\": \"s_url_172\", \"enabled\": true, \"fail_open\": true, \"created_at\": \"2026-11-05T10:00:00Z\", \"last_called_at\": \"2026-11-05T10:00:00Z\", \"last_outcome\": \"s_last_outcome_173\", \"last_status_code\": 7, \"calls\": 7, \"skips\": 7, \"denies\": 7}",
            List.of()),
        record(AuthHookState.class, "{\"enabled\": true, \"fail_open\": true}", List.of()),
        record(
            AuthHookTestResult.class,
            "{\"status_code\": 7, \"duration_ms\": 7, \"decision\": \"s_decision_176\", \"message\": \"s_message_177\", \"claims\": {\"k\": \"v\"}, \"error\": \"s_error_178\"}",
            List.of()),
        record(
            AuthPolicy.class,
            "{\"password_enabled\": true, \"password_min_length\": 7, \"password_require_uppercase\": true, \"password_require_number\": true, \"password_require_symbol\": true, \"magic_link_enabled\": true, \"magic_link_ttl_minutes\": 7, \"passkey_enabled\": true, \"otp_email_enabled\": true, \"otp_sms_enabled\": true, \"self_registration_enabled\": true, \"remember_device_enabled\": true, \"remember_device_days\": 7, \"lockout_threshold\": 7, \"lockout_window_minutes\": 7, \"lockout_duration_minutes\": 7, \"lockout_limits\": {\"defaults\": {\"threshold\": 0.5, \"window_minutes\": 7, \"duration_minutes\": 7}, \"min\": {\"threshold\": 0.5, \"window_minutes\": 7, \"duration_minutes\": 7}, \"max\": {\"threshold\": 0.5, \"window_minutes\": 7, \"duration_minutes\": 7}}}",
            List.of()),
        record(
            AuthPolicyUsage.class,
            "{\"people\": 7, \"password\": {\"people\": 7, \"only\": 7}, \"passkey\": {\"people\": 7, \"only\": 7}, \"social\": [{\"provider\": \"s_provider_181\", \"linked\": 7, \"only\": 7}], \"magic_link_enabled\": true}",
            List.of()),
        record(
            AuthzenAction.class,
            "{\"name\": \"s_name_188\", \"properties\": {\"k\": \"v\"}}",
            List.of()),
        record(
            AuthzenEntity.class,
            "{\"type\": \"s_type_189\", \"id\": \"s_id_190\", \"properties\": {\"k\": \"v\"}}",
            List.of()),
        record(
            BackchannelAuthentication.class,
            "{\"auth_req_id\": \"s_auth_req_id_191\", \"expires_in\": 7, \"interval\": 7}",
            List.of("s_auth_req_id_191")),
        record(
            BillingProfile.class,
            "{\"legal_name\": \"s_legal_name_197\", \"billing_email\": \"s_billing_email_198\", \"address_line1\": \"s_address_line1_199\", \"address_line2\": \"s_address_line2_200\", \"city\": \"s_city_201\", \"state\": \"s_state_202\", \"postal_code\": \"s_postal_code_203\", \"country\": \"s_country_204\", \"tax_id\": \"s_tax_id_205\", \"tax_id_type\": \"s_tax_id_type_206\"}",
            List.of()),
        record(
            BotActivity.class,
            "{\"recent\": [{\"id\": \"s_id_217\", \"ip\": \"s_ip_218\", \"user_agent\": \"s_user_agent_219\", \"score\": 0.5, \"verdict\": \"s_verdict_220\", \"outcome\": \"s_outcome_221\", \"user_id\": \"s_user_id_222\", \"user_email\": \"s_user_email_223\", \"created_at\": \"2026-11-05T10:00:00Z\"}], \"next_cursor\": \"s_next_cursor_224\", \"total\": 7, \"stats\": {\"blocked_24h\": 7, \"challenged_24h\": 7, \"threshold\": 0.5}}",
            List.of()),
        record(
            BotEvent.class,
            "{\"id\": \"s_id_227\", \"ip\": \"s_ip_228\", \"user_agent\": \"s_user_agent_229\", \"score\": 0.5, \"verdict\": \"s_verdict_230\", \"outcome\": \"s_outcome_231\", \"user_id\": \"s_user_id_232\", \"user_email\": \"s_user_email_233\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            BotSettings.class,
            "{\"ua_check\": true, \"honeypot\": true, \"captcha\": true, \"signature\": true, \"score_threshold\": 0.5}",
            List.of()),
        record(
            BulkImportResult.class,
            "{\"succeeded\": 7, \"failed\": 7, \"created\": [{\"line\": 7, \"email\": \"s_email_240\", \"id\": \"s_id_241\"}], \"errors\": [{\"line\": 7, \"email\": \"s_email_242\", \"code\": \"s_code_243\", \"field\": \"s_field_244\", \"message\": \"s_message_245\"}]}",
            List.of()),
        record(
            BulkImportRowError.class,
            "{\"line\": 7, \"email\": \"s_email_246\", \"code\": \"s_code_247\", \"field\": \"s_field_248\", \"message\": \"s_message_249\"}",
            List.of("s_code_247")),
        record(
            CheckoutResult.class,
            "{\"status\": \"s_status_259\", \"checkout_url\": \"s_checkout_url_260\", \"provider\": \"s_provider_261\"}",
            List.of()),
        record(
            ClientCredentialsToken.class,
            "{\"access_token\": \"s_access_token_265\", \"token_type\": \"s_token_type_266\", \"expires_in\": 7, \"expires_at\": \"2026-11-05T10:00:00Z\", \"scope\": \"s_scope_267\"}",
            List.of("s_access_token_265")),
        record(
            ConnectionTestResult.class,
            "{\"ok\": true, \"checks\": [{\"name\": \"s_name_268\", \"ok\": true, \"detail\": \"s_detail_269\"}]}",
            List.of()),
        record(
            CredentialVerification.class,
            "{\"valid\": true, \"reason\": \"s_reason_270\", \"reason_code\": \"s_reason_code_271\", \"subject\": \"s_subject_272\", \"issuer\": \"s_issuer_273\", \"vc\": {\"k\": \"v\"}}",
            List.of()),
        record(
            DecidedBy.class,
            "{\"model\": \"s_model_274\", \"rule\": \"s_rule_275\", \"reason_code\": \"s_reason_code_276\", \"reason_params\": {\"k\": \"v\"}, \"policy_id\": \"s_policy_id_277\"}",
            List.of()),
        record(
            DecisionLogEntry.class,
            "{\"id\": \"s_id_281\", \"decided_at\": \"2026-11-05T10:00:00Z\", \"source\": \"s_source_282\", \"caller_type\": \"s_caller_type_283\", \"caller_id\": \"s_caller_id_284\", \"caller_client\": \"s_caller_client_285\", \"subject\": \"s_subject_286\", \"resource\": \"s_resource_287\", \"action\": \"s_action_288\", \"allowed\": true, \"model\": \"s_model_289\", \"rule\": \"s_rule_290\", \"reason_code\": \"s_reason_code_291\"}",
            List.of()),
        record(
            DeletedUser.class,
            "{\"id\": \"s_id_292\", \"email\": \"s_email_293\", \"display_name\": \"s_display_name_294\", \"deleted_at\": \"2026-11-05T10:00:00Z\", \"deleted_by\": \"s_deleted_by_295\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            DeviceAuthorization.class,
            "{\"device_code\": \"s_device_code_296\", \"user_code\": \"s_user_code_297\", \"verification_uri\": \"s_verification_uri_298\", \"verification_uri_complete\": \"s_verification_uri_complete_299\", \"expires_in\": 7, \"interval\": 7}",
            List.of("s_device_code_296", "s_user_code_297", "s_verification_uri_complete_299")),
        record(
            Domain.class,
            "{\"id\": \"s_id_306\", \"domain\": \"s_domain_307\", \"status\": \"s_status_308\", \"verification_token\": \"s_verification_token_309\", \"dns_record_name\": \"s_dns_record_name_310\", \"dns_record_type\": \"s_dns_record_type_311\", \"dns_record_value\": \"s_dns_record_value_312\", \"verified_at\": \"2026-11-05T10:00:00Z\", \"last_checked_at\": \"2026-11-05T10:00:00Z\", \"last_error\": \"s_last_error_313\", \"sso_enabled\": true, \"jit_enabled\": true, \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            EffectivePermissions.class,
            "{\"permissions\": [\"s_permissions_314\"], \"grants\": [{\"permission\": \"s_permission_315\", \"granted_by\": \"s_granted_by_316\", \"via\": \"s_via_317\", \"group_id\": \"s_group_id_318\", \"group_name\": \"s_group_name_319\", \"role_id\": \"s_role_id_320\", \"role_name\": \"s_role_name_321\"}]}",
            List.of()),
        record(
            EmailLocalizedContent.class,
            "{\"subject\": \"s_subject_322\", \"body\": \"s_body_323\", \"preheader\": \"s_preheader_324\", \"body_html\": \"s_body_html_325\"}",
            List.of()),
        record(
            EmailTemplate.class,
            "{\"subject\": \"s_subject_326\", \"body\": \"s_body_327\", \"options\": {\"preheader\": \"s_preheader_328\", \"body_html\": \"s_body_html_329\", \"default_locale\": \"s_default_locale_330\", \"show_logo\": true, \"alignment\": \"s_alignment_331\", \"translations\": {\"k\": {\"subject\": \"s_subject_332\", \"body\": \"s_body_333\", \"preheader\": \"s_preheader_334\", \"body_html\": \"s_body_html_335\"}}}, \"key\": \"s_key_336\", \"name\": \"s_name_337\", \"description\": \"s_description_338\", \"variables\": [\"s_variables_339\"], \"custom\": true, \"delivery_scope\": \"s_delivery_scope_340\"}",
            List.of()),
        record(
            EmailTemplateContent.class,
            "{\"subject\": \"s_subject_341\", \"body\": \"s_body_342\", \"options\": {\"preheader\": \"s_preheader_343\", \"body_html\": \"s_body_html_344\", \"default_locale\": \"s_default_locale_345\", \"show_logo\": true, \"alignment\": \"s_alignment_346\", \"translations\": {\"k\": {\"subject\": \"s_subject_347\", \"body\": \"s_body_348\", \"preheader\": \"s_preheader_349\", \"body_html\": \"s_body_html_350\"}}}}",
            List.of()),
        record(
            EmailTemplateOptions.class,
            "{\"preheader\": \"s_preheader_351\", \"body_html\": \"s_body_html_352\", \"default_locale\": \"s_default_locale_353\", \"show_logo\": true, \"alignment\": \"s_alignment_354\", \"translations\": {\"k\": {\"subject\": \"s_subject_355\", \"body\": \"s_body_356\", \"preheader\": \"s_preheader_357\", \"body_html\": \"s_body_html_358\"}}}",
            List.of()),
        record(
            EmailTemplatePreview.class,
            "{\"subject\": \"s_subject_359\", \"body\": \"s_body_360\", \"html\": \"s_html_361\", \"locale\": \"s_locale_362\"}",
            List.of()),
        record(
            Entitlements.class,
            "{\"plan\": \"s_plan_385\", \"features\": {\"k\": true}, \"limits\": {\"k\": 7}}",
            List.of()),
        record(
            ErasureRequest.class,
            "{\"id\": \"s_id_389\", \"tenant_id\": \"s_tenant_id_390\", \"user_id\": \"s_user_id_391\", \"requested_by\": \"s_requested_by_392\", \"reason\": \"s_reason_393\", \"status\": \"s_status_394\", \"grace_until\": \"2026-11-05T10:00:00Z\", \"grace_period_days\": 7, \"completed_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\", \"subject_email\": \"s_subject_email_395\", \"subject_name\": \"s_subject_name_396\", \"subject_redacted\": true}",
            List.of()),
        record(Evaluation.class, "{\"decision\": true, \"context\": {\"k\": \"v\"}}", List.of()),
        record(
            Evaluations.class,
            "{\"evaluations\": [{\"decision\": true, \"context\": {\"k\": \"v\"}}]}",
            List.of()),
        record(
            ExportRequest.class,
            "{\"id\": \"s_id_414\", \"tenant_id\": \"s_tenant_id_415\", \"user_id\": \"s_user_id_416\", \"requested_by\": \"s_requested_by_417\", \"status\": \"s_status_418\", \"payload\": {\"k\": \"v\"}, \"error\": \"s_error_419\", \"completed_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            GrantStep.class,
            "{\"permission\": \"s_permission_420\", \"granted_by\": \"s_granted_by_421\", \"via\": \"s_via_422\", \"group_id\": \"s_group_id_423\", \"group_name\": \"s_group_name_424\", \"role_id\": \"s_role_id_425\", \"role_name\": \"s_role_name_426\"}",
            List.of()),
        record(
            Group.class,
            "{\"id\": \"s_id_427\", \"tenant_id\": \"s_tenant_id_428\", \"parent_id\": \"s_parent_id_429\", \"name\": \"s_name_430\", \"description\": \"s_description_431\", \"member_count\": 7, \"created_at\": \"2026-11-05T10:00:00Z\", \"roles\": [{\"role_id\": \"s_role_id_432\", \"name\": \"s_name_433\"}]}",
            List.of()),
        record(
            GroupMember.class,
            "{\"user_id\": \"s_user_id_440\", \"email\": \"s_email_441\", \"display_name\": \"s_display_name_442\"}",
            List.of()),
        record(
            GroupRole.class,
            "{\"role_id\": \"s_role_id_443\", \"name\": \"s_name_444\", \"granted_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            Invitation.class,
            "{\"id\": \"s_id_448\", \"tenant_id\": \"s_tenant_id_449\", \"email\": \"s_email_450\", \"role_id\": \"s_role_id_451\", \"status\": \"s_status_452\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"accepted_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\", \"delivery\": {\"status\": \"s_status_453\", \"code\": \"s_code_454\", \"message\": \"s_message_455\"}}",
            List.of()),
        record(
            InvitationDelivery.class,
            "{\"status\": \"s_status_459\", \"code\": \"s_code_460\", \"message\": \"s_message_461\"}",
            List.of("s_code_460")),
        record(
            InvitationResult.class,
            "{\"invite\": {\"id\": \"s_id_462\", \"tenant_id\": \"s_tenant_id_463\", \"email\": \"s_email_464\", \"role_id\": \"s_role_id_465\", \"status\": \"s_status_466\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"accepted_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\", \"delivery\": {\"status\": \"s_status_467\", \"code\": \"s_code_468\", \"message\": \"s_message_469\"}}}",
            List.of()),
        record(
            Invoice.class,
            "{\"id\": \"s_id_470\", \"invoice_number\": \"s_invoice_number_471\", \"plan_code\": \"s_plan_code_472\", \"kind\": \"s_kind_473\", \"status\": \"s_status_474\", \"currency\": \"s_currency_475\", \"amount_minor\": 7, \"taxable_amount_minor\": 7, \"tax_amount_minor\": 7, \"tax_rate_bps\": 7, \"tax_type\": \"s_tax_type_476\", \"place_of_supply\": \"s_place_of_supply_477\", \"supplier_gstin\": \"s_supplier_gstin_478\", \"sac\": \"s_sac_479\", \"billed_to\": {\"legal_name\": \"s_legal_name_480\", \"billing_email\": \"s_billing_email_481\", \"address_line1\": \"s_address_line1_482\", \"address_line2\": \"s_address_line2_483\", \"city\": \"s_city_484\", \"state\": \"s_state_485\", \"postal_code\": \"s_postal_code_486\", \"country\": \"s_country_487\", \"tax_id\": \"s_tax_id_488\", \"tax_id_type\": \"s_tax_id_type_489\"}, \"period_start\": \"2026-11-05T10:00:00Z\", \"period_end\": \"2026-11-05T10:00:00Z\", \"issued_at\": \"2026-11-05T10:00:00Z\", \"due_at\": \"2026-11-05T10:00:00Z\", \"paid_at\": \"2026-11-05T10:00:00Z\", \"emailed_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            IpRule.class,
            "{\"id\": \"s_id_492\", \"tenant_id\": \"s_tenant_id_493\", \"action\": \"s_action_494\", \"cidr\": \"s_cidr_495\", \"label\": \"s_label_496\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            IpRuleCheck.class,
            "{\"enabled\": true, \"allowed\": true, \"reason\": \"s_reason_497\", \"reason_code\": \"s_reason_code_498\", \"reason_params\": {\"k\": \"v\"}, \"rules\": {\"allowed\": true, \"reason\": \"s_reason_499\", \"reason_code\": \"s_reason_code_500\", \"reason_params\": {\"k\": \"v\"}}}",
            List.of()),
        record(
            IssuedCredential.class,
            "{\"credential_id\": \"s_credential_id_504\", \"jwt\": \"s_jwt_505\", \"expires_at\": \"2026-11-05T10:00:00Z\"}",
            List.of("s_jwt_505")),
        record(
            LdapConnection.class,
            "{\"id\": \"s_id_506\", \"tenant_id\": \"s_tenant_id_507\", \"name\": \"s_name_508\", \"server_url\": \"s_server_url_509\", \"start_tls\": true, \"skip_tls_verify\": true, \"bind_dn\": \"s_bind_dn_510\", \"base_dn\": \"s_base_dn_511\", \"user_filter\": \"s_user_filter_512\", \"email_attribute\": \"s_email_attribute_513\", \"name_attribute\": \"s_name_attribute_514\", \"status\": \"s_status_515\", \"created_at\": \"2026-11-05T10:00:00Z\", \"updated_at\": \"2026-11-05T10:00:00Z\", \"last_login_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            LinkedIdentity.class,
            "{\"id\": \"s_id_534\", \"user_id\": \"s_user_id_535\", \"tenant_id\": \"s_tenant_id_536\", \"provider\": \"s_provider_537\", \"subject\": \"s_subject_538\", \"email\": \"s_email_539\", \"linked_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            LockoutSettings.class,
            "{\"threshold\": 0.5, \"window_minutes\": 7, \"duration_minutes\": 7}",
            List.of()),
        record(
            LogSink.class,
            "{\"id\": \"s_id_540\", \"type\": \"s_type_541\", \"endpoint\": \"s_endpoint_542\", \"enabled\": true, \"last_forwarded_at\": \"2026-11-05T10:00:00Z\", \"last_error\": \"s_last_error_543\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            LogSinkTestResult.class,
            "{\"delivered\": true, \"status\": 7, \"error\": \"s_error_547\", \"duration_ms\": 7}",
            List.of()),
        record(
            MembershipRemoval.class,
            "{\"id\": \"s_id_548\", \"roles_removed\": 7, \"sessions_ended\": 7}",
            List.of()),
        record(
            MfaChallenge.class,
            "{\"mfa_required\": true, \"mfa_token\": \"s_mfa_token_549\", \"methods\": [\"s_methods_550\"], \"factors\": [{\"type\": \"s_type_551\", \"id\": \"s_id_552\", \"destination\": \"s_destination_553\"}], \"enrollment_required\": true, \"enrollment_due_at\": \"2026-11-05T10:00:00Z\"}",
            List.of("s_mfa_token_549")),
        record(
            MfaFactor.class,
            "{\"type\": \"s_type_554\", \"id\": \"s_id_555\", \"destination\": \"s_destination_556\"}",
            List.of()),
        record(
            OAuthDeviceAuthorization.class,
            "{\"id\": \"s_id_557\", \"client_id\": \"s_client_id_558\", \"client_name\": \"s_client_name_559\", \"user_code\": \"s_user_code_560\", \"status\": \"s_status_561\", \"user_id\": \"s_user_id_562\", \"user_email\": \"s_user_email_563\", \"scopes\": [\"s_scopes_564\"], \"created_at\": \"2026-11-05T10:00:00Z\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"last_polled_at\": \"2026-11-05T10:00:00Z\", \"consumed_at\": \"2026-11-05T10:00:00Z\"}",
            List.of("s_user_code_560")),
        record(
            OAuthGrant.class,
            "{\"id\": \"s_id_565\", \"client_id\": \"s_client_id_566\", \"client_name\": \"s_client_name_567\", \"user_id\": \"s_user_id_568\", \"user_email\": \"s_user_email_569\", \"user_name\": \"s_user_name_570\", \"scopes\": [\"s_scopes_571\"], \"authorized_at\": \"2026-11-05T10:00:00Z\", \"consented_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"issued_at\": \"2026-11-05T10:00:00Z\", \"expires_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            OAuthResource.class,
            "{\"id\": \"s_id_574\", \"tenant_id\": \"s_tenant_id_575\", \"identifier\": \"s_identifier_576\", \"name\": \"s_name_577\", \"server_client_id\": \"s_server_client_id_578\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            PaymentMethod.class,
            "{\"provider\": \"s_provider_582\", \"type\": \"s_type_583\", \"brand\": \"s_brand_584\", \"last4\": \"s_last4_585\", \"exp_month\": 7, \"exp_year\": 7, \"last_used_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            Permission.class,
            "{\"id\": \"s_id_586\", \"key\": \"s_key_587\", \"description\": \"s_description_588\"}",
            List.of()),
        record(
            PermissionDecision.class,
            "{\"allowed\": true, \"decided_by\": {\"model\": \"s_model_592\", \"rule\": \"s_rule_593\", \"reason_code\": \"s_reason_code_594\", \"reason_params\": {\"k\": \"v\"}, \"policy_id\": \"s_policy_id_595\"}, \"paths\": [{\"permission\": \"s_permission_596\", \"granted_by\": \"s_granted_by_597\", \"via\": \"s_via_598\", \"group_id\": \"s_group_id_599\", \"group_name\": \"s_group_name_600\", \"role_id\": \"s_role_id_601\", \"role_name\": \"s_role_name_602\"}], \"reason\": \"s_reason_603\", \"reason_code\": \"s_reason_code_604\", \"reason_params\": {\"k\": \"v\"}, \"trace\": [\"s_trace_605\"]}",
            List.of()),
        record(RateLimitBucket.class, "{\"rate\": 0.5, \"capacity\": 7}", List.of()),
        record(
            RelationDecision.class,
            "{\"allowed\": true, \"decided_by\": {\"model\": \"s_model_614\", \"rule\": \"s_rule_615\", \"reason_code\": \"s_reason_code_616\", \"reason_params\": {\"k\": \"v\"}, \"policy_id\": \"s_policy_id_617\"}, \"trace\": [\"s_trace_618\"], \"path\": [{\"object\": \"s_object_619\", \"relation\": \"s_relation_620\", \"subject\": \"s_subject_621\", \"depth\": 7}]}",
            List.of()),
        record(
            RelationGraph.class,
            "{\"nodes\": [{\"id\": \"s_id_622\", \"type\": \"s_type_623\", \"label\": \"s_label_624\", \"name\": \"s_name_625\"}], \"edges\": [{\"from\": \"s_from_626\", \"to\": \"s_to_627\", \"relation\": \"s_relation_628\"}]}",
            List.of()),
        record(
            RelationObject.class,
            "{\"object\": \"s_object_631\", \"id\": \"s_id_632\", \"tuples\": 7}",
            List.of()),
        record(
            RelationObjectType.class,
            "{\"type\": \"s_type_635\", \"objects\": 7, \"tuples\": 7}",
            List.of()),
        record(
            RelationTuple.class,
            "{\"id\": \"s_id_636\", \"object\": \"s_object_637\", \"relation\": \"s_relation_638\", \"subject\": \"s_subject_639\", \"subject_name\": \"s_subject_name_640\"}",
            List.of()),
        record(
            RetentionPolicy.class,
            "{\"deleted_users_enabled\": true, \"deleted_users_days\": 7, \"updated_at\": \"2026-11-05T10:00:00Z\", \"updated_by\": \"s_updated_by_644\", \"updated_by_email\": \"s_updated_by_email_645\"}",
            List.of()),
        record(
            RetentionPreview.class,
            "{\"ripe_deleted_users\": 7, \"deleted_users_days\": 7}",
            List.of()),
        record(
            RiskSettings.class,
            "{\"medium_threshold\": 0.5, \"high_threshold\": 0.5, \"force_mfa_at_level\": \"s_force_mfa_at_level_646\", \"impossible_travel_enabled\": true, \"min_travel_hours\": 0.5, \"device_reputation_enabled\": true, \"country_header_configured\": true, \"country_last_seen_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            Role.class,
            "{\"id\": \"s_id_648\", \"tenant_id\": \"s_tenant_id_649\", \"name\": \"s_name_650\", \"description\": \"s_description_651\", \"is_system\": true, \"created_at\": \"2026-11-05T10:00:00Z\", \"holders\": {\"people_direct\": 7, \"people_through_groups\": 7, \"groups\": 7}}",
            List.of()),
        record(
            RoleHolders.class,
            "{\"people\": [{\"user_id\": \"s_user_id_654\", \"email\": \"s_email_655\", \"display_name\": \"s_display_name_656\", \"direct\": true, \"via_groups\": [{\"id\": \"s_id_657\", \"name\": \"s_name_658\"}]}], \"groups\": [{\"id\": \"s_id_659\", \"name\": \"s_name_660\"}], \"next_cursor\": \"s_next_cursor_661\"}",
            List.of()),
        record(
            SamlAttributeMapping.class,
            "{\"name\": \"s_name_665\", \"source\": \"s_source_666\", \"name_format\": \"s_name_format_667\"}",
            List.of()),
        record(
            SamlCertificate.class,
            "{\"id\": \"s_id_668\", \"certificate\": \"s_certificate_669\", \"subject\": \"s_subject_670\", \"fingerprint_sha256\": \"s_fingerprint_sha256_671\", \"not_before\": \"2026-11-05T10:00:00Z\", \"not_after\": \"2026-11-05T10:00:00Z\", \"source\": \"s_source_672\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            SamlConnection.class,
            "{\"id\": \"s_id_673\", \"tenant_id\": \"s_tenant_id_674\", \"name\": \"s_name_675\", \"sp_entity_id\": \"s_sp_entity_id_676\", \"acs_url\": \"s_acs_url_677\", \"sp_metadata_url\": \"s_sp_metadata_url_678\", \"idp_entity_id\": \"s_idp_entity_id_679\", \"idp_sso_url\": \"s_idp_sso_url_680\", \"idp_certificate\": \"s_idp_certificate_681\", \"certificates\": [{\"id\": \"s_id_682\", \"certificate\": \"s_certificate_683\", \"subject\": \"s_subject_684\", \"fingerprint_sha256\": \"s_fingerprint_sha256_685\", \"not_before\": \"2026-11-05T10:00:00Z\", \"not_after\": \"2026-11-05T10:00:00Z\", \"source\": \"s_source_686\", \"created_at\": \"2026-11-05T10:00:00Z\"}], \"metadata_url\": \"s_metadata_url_687\", \"metadata_refreshed_at\": \"2026-11-05T10:00:00Z\", \"metadata_error\": \"s_metadata_error_688\", \"email_attribute\": \"s_email_attribute_689\", \"name_attribute\": \"s_name_attribute_690\", \"groups_attribute\": \"s_groups_attribute_691\", \"status\": \"s_status_692\", \"allow_idp_initiated\": true, \"created_at\": \"2026-11-05T10:00:00Z\", \"updated_at\": \"2026-11-05T10:00:00Z\", \"last_login_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            SamlServiceProvider.class,
            "{\"id\": \"s_id_719\", \"tenant_id\": \"s_tenant_id_720\", \"name\": \"s_name_721\", \"entity_id\": \"s_entity_id_722\", \"acs_url\": \"s_acs_url_723\", \"name_id_format\": \"s_name_id_format_724\", \"name_id_attribute\": \"s_name_id_attribute_725\", \"certificate\": \"s_certificate_726\", \"require_signed_requests\": true, \"encrypt_assertions\": true, \"certificate_details\": {\"subject\": \"s_subject_727\", \"fingerprint_sha256\": \"s_fingerprint_sha256_728\", \"not_before\": \"2026-11-05T10:00:00Z\", \"not_after\": \"2026-11-05T10:00:00Z\"}, \"status\": \"s_status_729\", \"attributes\": [{\"name\": \"s_name_730\", \"source\": \"s_source_731\", \"name_format\": \"s_name_format_732\"}], \"assignment_required\": true, \"created_at\": \"2026-11-05T10:00:00Z\", \"updated_at\": \"2026-11-05T10:00:00Z\", \"last_login_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            SamlServiceProviderAssignments.class,
            "{\"users\": [{\"user_id\": \"s_user_id_733\", \"email\": \"s_email_734\", \"display_name\": \"s_display_name_735\", \"assigned_at\": \"2026-11-05T10:00:00Z\", \"assigned_by\": \"s_assigned_by_736\"}], \"groups\": [{\"group_id\": \"s_group_id_737\", \"name\": \"s_name_738\", \"assigned_at\": \"2026-11-05T10:00:00Z\", \"assigned_by\": \"s_assigned_by_739\"}]}",
            List.of()),
        record(
            ScimConfig.class,
            "{\"token_set\": true, \"token_count\": 7, \"token_prefix\": \"s_token_prefix_758\", \"created_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"provisioned_count\": 7, \"last_request\": {\"at\": \"2026-11-05T10:00:00Z\", \"operation\": \"s_operation_759\", \"resource\": \"s_resource_760\", \"http_status\": 7, \"status\": \"s_status_761\", \"error_code\": \"s_error_code_762\", \"reason\": \"s_reason_763\"}}",
            List.of()),
        record(
            ScimGroup.class,
            "{\"id\": \"s_id_764\", \"name\": \"s_name_765\", \"external_id\": \"s_external_id_766\", \"members\": 7, \"created_at\": \"2026-11-05T10:00:00Z\", \"updated_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            ScimToken.class,
            "{\"id\": \"s_id_767\", \"name\": \"s_name_768\", \"token_prefix\": \"s_token_prefix_769\", \"status\": \"s_status_770\", \"created_at\": \"2026-11-05T10:00:00Z\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"revoked_at\": \"2026-11-05T10:00:00Z\", \"replaced_by\": \"s_replaced_by_771\"}",
            List.of()),
        record(
            ScimTokenCreated.class,
            "{\"scim_token\": {\"id\": \"s_id_773\", \"name\": \"s_name_774\", \"token_prefix\": \"s_token_prefix_775\", \"status\": \"s_status_776\", \"created_at\": \"2026-11-05T10:00:00Z\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"revoked_at\": \"2026-11-05T10:00:00Z\", \"replaced_by\": \"s_replaced_by_777\"}, \"token\": \"s_token_778\"}",
            List.of("s_token_778")),
        record(
            ScimTokenRotated.class,
            "{\"scim_token\": {\"id\": \"s_id_779\", \"name\": \"s_name_780\", \"token_prefix\": \"s_token_prefix_781\", \"status\": \"s_status_782\", \"created_at\": \"2026-11-05T10:00:00Z\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"revoked_at\": \"2026-11-05T10:00:00Z\", \"replaced_by\": \"s_replaced_by_783\"}, \"token\": \"s_token_784\", \"replaced\": {\"id\": \"s_id_785\", \"name\": \"s_name_786\", \"token_prefix\": \"s_token_prefix_787\", \"status\": \"s_status_788\", \"created_at\": \"2026-11-05T10:00:00Z\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"revoked_at\": \"2026-11-05T10:00:00Z\", \"replaced_by\": \"s_replaced_by_789\"}}",
            List.of("s_token_784")),
        record(
            ScimUser.class,
            "{\"id\": \"s_id_790\", \"email\": \"s_email_791\", \"display_name\": \"s_display_name_792\", \"external_id\": \"s_external_id_793\", \"status\": \"s_status_794\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            Secret.class,
            "{\"id\": \"s_id_795\", \"name\": \"s_name_796\", \"scope\": \"s_scope_797\", \"last4\": \"s_last4_798\", \"created_at\": \"2026-11-05T10:00:00Z\", \"updated_at\": \"2026-11-05T10:00:00Z\", \"value_changed_at\": \"2026-11-05T10:00:00Z\", \"last_revealed_at\": \"2026-11-05T10:00:00Z\", \"last_revealed_by\": {\"id\": \"s_id_799\", \"email\": \"s_email_800\", \"display_name\": \"s_display_name_801\"}, \"last_revealed_credential\": \"s_last_revealed_credential_802\", \"rotation_interval_days\": 7, \"rotation_due_at\": \"2026-11-05T10:00:00Z\", \"rotation_overdue\": true}",
            List.of()),
        record(
            SecretValue.class,
            "{\"name\": \"s_name_808\", \"value\": \"s_value_809\"}",
            List.of("s_value_809")),
        record(
            SecurityPolicy.class,
            "{\"tenant_id\": \"s_tenant_id_810\", \"password_min_length\": 7, \"password_complexity\": \"s_password_complexity_811\", \"session_max_age\": \"s_session_max_age_812\", \"session_idle_timeout\": \"s_session_idle_timeout_813\", \"max_sessions\": 7, \"session_limit_action\": \"s_session_limit_action_814\", \"mfa_enforcement\": \"s_mfa_enforcement_815\", \"mfa_grace_days\": 7, \"mfa_enforced_since\": \"2026-11-05T10:00:00Z\", \"step_up_window\": \"s_step_up_window_816\", \"step_up_methods\": [\"s_step_up_methods_817\"], \"settings\": {\"k\": \"v\"}}",
            List.of()),
        record(
            ServicePrincipal.class,
            "{\"id\": \"s_id_825\", \"tenant_id\": \"s_tenant_id_826\", \"name\": \"s_name_827\", \"description\": \"s_description_828\", \"scopes\": [\"s_scopes_829\"], \"disabled_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"previous_secret_expires_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            ServicePrincipalCreated.class,
            "{\"service_principal\": {\"id\": \"s_id_834\", \"tenant_id\": \"s_tenant_id_835\", \"name\": \"s_name_836\", \"description\": \"s_description_837\", \"scopes\": [\"s_scopes_838\"], \"disabled_at\": \"2026-11-05T10:00:00Z\", \"last_used_at\": \"2026-11-05T10:00:00Z\", \"previous_secret_expires_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\"}, \"client_id\": \"s_client_id_839\", \"client_secret\": \"s_client_secret_840\", \"warning\": \"s_warning_841\"}",
            List.of("s_client_secret_840")),
        record(
            ServicePrincipalSecret.class,
            "{\"client_id\": \"s_client_id_842\", \"client_secret\": \"s_client_secret_843\", \"previous_secret_expires_at\": \"2026-11-05T10:00:00Z\", \"warning\": \"s_warning_844\"}",
            List.of("s_client_secret_843")),
        record(
            ShadowAiApplication.class,
            "{\"id\": \"s_id_845\", \"client_id\": \"s_client_id_846\", \"name\": \"s_name_847\", \"grant_types\": [\"s_grant_types_848\"], \"live_grants\": 7, \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            SigningKey.class,
            "{\"kid\": \"s_kid_849\", \"alg\": \"s_alg_850\", \"use\": \"s_use_851\", \"status\": \"s_status_852\", \"retire_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\", \"activated_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            SocialProvider.class,
            "{\"id\": \"s_id_853\", \"tenant_id\": \"s_tenant_id_854\", \"provider\": \"s_provider_855\", \"kind\": \"s_kind_856\", \"client_id\": \"s_client_id_857\", \"discovery_url\": \"s_discovery_url_858\", \"enabled\": true, \"apple_team_id\": \"s_apple_team_id_859\", \"apple_key_id\": \"s_apple_key_id_860\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            SsoGroupMapping.class,
            "{\"id\": \"s_id_872\", \"idp_group\": \"s_idp_group_873\", \"group_id\": \"s_group_id_874\", \"group_name\": \"s_group_name_875\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            Subscription.class,
            "{\"status\": \"s_status_878\", \"plan_code\": \"s_plan_code_879\", \"plan_name\": \"s_plan_name_880\", \"currency\": \"s_currency_881\", \"amount_minor\": 7, \"interval\": \"s_interval_882\", \"current_period_start\": \"2026-11-05T10:00:00Z\", \"current_period_end\": \"2026-11-05T10:00:00Z\", \"trial_end\": \"2026-11-05T10:00:00Z\", \"cancel_at_period_end\": true, \"ended_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            Tenant.class,
            "{\"id\": \"s_id_885\", \"slug\": \"s_slug_886\", \"name\": \"s_name_887\", \"status\": \"s_status_888\", \"plan\": \"s_plan_889\", \"region\": \"s_region_890\", \"metadata\": {\"k\": \"v\"}, \"created_at\": \"2026-11-05T10:00:00Z\", \"updated_at\": \"2026-11-05T10:00:00Z\", \"deletion_due_at\": \"2026-11-05T10:00:00Z\", \"member_count\": 7, \"mfa_enabled_count\": 7}",
            List.of()),
        record(
            TenantBranding.class,
            "{\"tenant_id\": \"s_tenant_id_891\", \"logo_url\": \"s_logo_url_892\", \"primary_color\": \"s_primary_color_893\", \"secondary_color\": \"s_secondary_color_894\", \"custom_domain\": \"s_custom_domain_895\", \"email_from_name\": \"s_email_from_name_896\", \"email_from_address\": \"s_email_from_address_897\", \"settings\": {\"k\": \"v\"}}",
            List.of()),
        record(
            TenantDeletion.class,
            "{\"tenant_id\": \"s_tenant_id_898\", \"deletion_due_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            TenantRateLimits.class,
            "{\"tenant\": {\"rate\": 0.5, \"capacity\": 7}, \"user\": {\"rate\": 0.5, \"capacity\": 7}, \"api_key\": {\"rate\": 0.5, \"capacity\": 7}, \"defaults\": {\"tenant\": {\"rate\": 0.5, \"capacity\": 7}, \"user\": {\"rate\": 0.5, \"capacity\": 7}, \"api_key\": {\"rate\": 0.5, \"capacity\": 7}}, \"ceiling\": {\"tenant\": {\"rate\": 0.5, \"capacity\": 7}, \"user\": {\"rate\": 0.5, \"capacity\": 7}, \"api_key\": {\"rate\": 0.5, \"capacity\": 7}}, \"min_rate\": 0.5, \"built_in\": [{\"name\": \"s_name_899\", \"per\": \"s_per_900\", \"applies\": \"s_applies_901\", \"rate\": 0.5, \"capacity\": 7}]}",
            List.of()),
        record(
            TokenPair.class,
            "{\"access_token\": \"s_access_token_914\", \"token_type\": \"s_token_type_915\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"refresh_token\": \"s_refresh_token_916\", \"session_expires_at\": \"2026-11-05T10:00:00Z\", \"idle_expires_at\": \"2026-11-05T10:00:00Z\", \"session_id\": \"s_session_id_917\", \"user_id\": \"s_user_id_918\", \"tenant_id\": \"s_tenant_id_919\"}",
            List.of("s_access_token_914", "s_refresh_token_916")),
        record(
            TokenResponse.class,
            "{\"access_token\": \"s_access_token_920\", \"token_type\": \"s_token_type_921\", \"expires_in\": 7, \"scope\": \"s_scope_922\", \"id_token\": \"s_id_token_923\", \"refresh_token\": \"s_refresh_token_924\", \"issued_token_type\": \"s_issued_token_type_925\"}",
            List.of("s_access_token_920", "s_id_token_923", "s_refresh_token_924")),
        record(
            TokenVaultProvider.class,
            "{\"id\": \"s_id_926\", \"provider\": \"s_provider_927\", \"client_id\": \"s_client_id_928\", \"authorize_url\": \"s_authorize_url_929\", \"token_url\": \"s_token_url_930\", \"scopes\": \"s_scopes_931\", \"created_at\": \"2026-11-05T10:00:00Z\", \"updated_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            User.class,
            "{\"id\": \"s_id_938\", \"tenant_id\": \"s_tenant_id_939\", \"email\": \"s_email_940\", \"email_verified_at\": \"2026-11-05T10:00:00Z\", \"phone\": \"s_phone_941\", \"phone_verified_at\": \"2026-11-05T10:00:00Z\", \"display_name\": \"s_display_name_942\", \"avatar_url\": \"s_avatar_url_943\", \"status\": \"s_status_944\", \"metadata\": {\"k\": \"v\"}, \"created_at\": \"2026-11-05T10:00:00Z\", \"updated_at\": \"2026-11-05T10:00:00Z\", \"roles\": [\"s_roles_945\"], \"assigned_roles\": [{\"id\": \"s_id_946\", \"name\": \"s_name_947\"}], \"mfa_enabled\": true, \"groups_count\": 7, \"last_seen_at\": \"2026-11-05T10:00:00Z\", \"last_seen_ip\": \"s_last_seen_ip_948\", \"home_tenant_name\": \"s_home_tenant_name_949\", \"suspension\": {\"reason\": \"s_reason_950\", \"source\": \"s_source_951\", \"suspended_at\": \"2026-11-05T10:00:00Z\", \"suspended_by\": \"s_suspended_by_952\", \"suspended_by_email\": \"s_suspended_by_email_953\"}}",
            List.of()),
        record(
            UserAccess.class,
            "{\"organization\": {\"id\": \"s_id_954\", \"name\": \"s_name_955\", \"slug\": \"s_slug_956\"}, \"roles\": [{\"id\": \"s_id_957\", \"name\": \"s_name_958\", \"description\": \"s_description_959\"}], \"groups\": [{\"id\": \"s_id_960\", \"name\": \"s_name_961\", \"description\": \"s_description_962\"}], \"applications_count\": 7, \"policies_count\": 7, \"permissions_direct\": 7, \"permissions_inherited\": 7, \"permissions_total\": 7}",
            List.of()),
        record(
            UserDeletion.class,
            "{\"id\": \"s_id_974\", \"deleted_at\": \"2026-11-05T10:00:00Z\", \"deleted_by\": \"s_deleted_by_975\"}",
            List.of()),
        record(
            UserSecurity.class,
            "{\"mfa_required\": true, \"mfa_enabled\": true, \"totp_enabled\": true, \"otp_factors\": 7, \"push_devices\": 7, \"passkeys\": 7, \"passkey_last_used_at\": \"2026-11-05T10:00:00Z\", \"recovery_codes_remaining\": 7, \"password_set\": true, \"password_changed_at\": \"2026-11-05T10:00:00Z\", \"active_sessions\": 7, \"distinct_devices\": 7}",
            List.of()),
        record(
            UserSession.class,
            "{\"id\": \"s_id_982\", \"ip\": \"s_ip_983\", \"user_agent\": \"s_user_agent_984\", \"created_at\": \"2026-11-05T10:00:00Z\", \"last_seen_at\": \"2026-11-05T10:00:00Z\", \"device\": \"s_device_985\", \"device_kind\": \"s_device_kind_986\", \"country\": \"s_country_987\", \"city\": \"s_city_988\"}",
            List.of()),
        record(
            UserSessionsRevocation.class,
            "{\"revoked\": 7, \"sessions\": 7, \"hosted_login_sessions\": 7, \"app_grants\": 7, \"trusted_devices\": 7, \"pending_authorizations\": 7}",
            List.of()),
        record(
            UserSuspension.class,
            "{\"reason\": \"s_reason_989\", \"source\": \"s_source_990\", \"suspended_at\": \"2026-11-05T10:00:00Z\", \"suspended_by\": \"s_suspended_by_991\", \"suspended_by_email\": \"s_suspended_by_email_992\"}",
            List.of()),
        record(
            VerifiableCredential.class,
            "{\"id\": \"s_id_997\", \"subject\": \"s_subject_998\", \"type\": \"s_type_999\", \"issued_at\": \"2026-11-05T10:00:00Z\", \"expires_at\": \"2026-11-05T10:00:00Z\", \"revoked\": true, \"user\": {\"id\": \"s_id_1000\", \"email\": \"s_email_1001\", \"display_name\": \"s_display_name_1002\"}}",
            List.of()),
        record(
            WebhookDelivery.class,
            "{\"id\": \"s_id_1009\", \"event_type\": \"s_event_type_1010\", \"attempt\": 7, \"status_code\": 7, \"error\": \"s_error_1011\", \"payload\": \"s_payload_1012\", \"response_body\": \"s_response_body_1013\", \"delivered_at\": \"2026-11-05T10:00:00Z\", \"next_attempt_at\": \"2026-11-05T10:00:00Z\", \"dead_at\": \"2026-11-05T10:00:00Z\", \"created_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        record(
            WebhookDeliveryAttempt.class,
            "{\"attempted_at\": \"2026-11-05T10:00:00Z\", \"status_code\": 7, \"duration_ms\": 7, \"error\": \"s_error_1014\"}",
            List.of()),
        record(
            WebhookEventType.class,
            "{\"name\": \"s_name_1017\", \"description\": \"s_description_1018\"}",
            List.of()),
        record(
            WebhookSecret.class,
            "{\"secret\": \"s_secret_1019\", \"previous_secret_expires_at\": \"2026-11-05T10:00:00Z\"}",
            List.of("s_secret_1019")),
        record(
            WebhookSubscription.class,
            "{\"id\": \"s_id_1020\", \"tenant_id\": \"s_tenant_id_1021\", \"url\": \"s_url_1022\", \"events\": [\"s_events_1023\"], \"disabled_at\": \"2026-11-05T10:00:00Z\", \"disabled_reason\": \"s_disabled_reason_1024\", \"disabled_by\": \"s_disabled_by_1025\", \"failing_since\": \"2026-11-05T10:00:00Z\", \"previous_secret_expires_at\": \"2026-11-05T10:00:00Z\", \"last_delivered_at\": \"2026-11-05T10:00:00Z\", \"last_status_code\": 7, \"counts\": {\"pending\": 7, \"retrying\": 7, \"gave_up\": 7}, \"created_at\": \"2026-11-05T10:00:00Z\", \"secret\": \"s_secret_1026\"}",
            List.of("s_secret_1026")),
        record(
            WebhookTestResult.class,
            "{\"queued\": true, \"subscription\": \"s_subscription_1027\"}",
            List.of()));
  }

  static Stream<ParamsRow> builders() {
    return Stream.of(
        params(
            "AdminPortalLinkCreateParams",
            () ->
                AdminPortalLinkCreateParams.builder()
                    .capabilities(List.of("s_capabilities_5"))
                    .ttlSeconds(7)
                    .build(),
            "{\"capabilities\": [\"s_capabilities_5\"], \"ttl_seconds\": 7}",
            List.of()),
        params(
            "AgentCreateParams",
            () ->
                AgentCreateParams.builder()
                    .name("s_name_18")
                    .sponsorUserId("s_sponsor_user_id_19")
                    .scopes(List.of("s_scopes_20"))
                    .tokenTtlSeconds(7)
                    .build(),
            "{\"name\": \"s_name_18\", \"sponsor_user_id\": \"s_sponsor_user_id_19\", \"scopes\": [\"s_scopes_20\"], \"token_ttl_seconds\": 7}",
            List.of()),
        params(
            "AgentRotateSecretParams",
            () -> AgentRotateSecretParams.builder().overlapHours(7).build(),
            "{\"overlap_hours\": 7}",
            List.of()),
        params(
            "AnalyticsOverviewParams",
            () ->
                AnalyticsOverviewParams.builder()
                    .from("s_from_40")
                    .to("s_to_41")
                    .days(7)
                    .method("s_method_42")
                    .build(),
            "{\"from\": \"s_from_40\", \"to\": \"s_to_41\", \"days\": 7, \"method\": \"s_method_42\"}",
            List.of()),
        params(
            "AnomalyListParams",
            () ->
                AnomalyListParams.builder()
                    .limit(7)
                    .cursor("s_cursor_54")
                    .status("s_status_55")
                    .build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_54\", \"status\": \"s_status_55\"}",
            List.of()),
        params(
            "ApiKeyCreateParams",
            () ->
                ApiKeyCreateParams.builder()
                    .tenantId("s_tenant_id_64")
                    .name("s_name_65")
                    .userId("s_user_id_66")
                    .scopes(List.of("s_scopes_67"))
                    .expiresAt(Instant.parse("2026-11-05T10:00:00Z"))
                    .build(),
            "{\"tenant_id\": \"s_tenant_id_64\", \"name\": \"s_name_65\", \"user_id\": \"s_user_id_66\", \"scopes\": [\"s_scopes_67\"], \"expires_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        params(
            "ApiKeyRotateParams",
            () -> ApiKeyRotateParams.builder().overlapHours(7).build(),
            "{\"overlap_hours\": 7}",
            List.of()),
        params(
            "ApplicationCreateParams",
            () ->
                ApplicationCreateParams.builder()
                    .name("s_name_94")
                    .type("s_type_95")
                    .redirectUris(List.of("s_redirect_uris_96"))
                    .postLogoutUris(List.of("s_post_logout_uris_97"))
                    .grantTypes(List.of("s_grant_types_98"))
                    .scopes(List.of("s_scopes_99"))
                    .backchannelLogoutUri("s_backchannel_logout_uri_100")
                    .frontchannelLogoutUri("s_frontchannel_logout_uri_101")
                    .logoUri("s_logo_uri_102")
                    .clientUri("s_client_uri_103")
                    .webOrigins(List.of("s_web_origins_104"))
                    .build(),
            "{\"name\": \"s_name_94\", \"type\": \"s_type_95\", \"redirect_uris\": [\"s_redirect_uris_96\"], \"post_logout_uris\": [\"s_post_logout_uris_97\"], \"grant_types\": [\"s_grant_types_98\"], \"scopes\": [\"s_scopes_99\"], \"backchannel_logout_uri\": \"s_backchannel_logout_uri_100\", \"frontchannel_logout_uri\": \"s_frontchannel_logout_uri_101\", \"logo_uri\": \"s_logo_uri_102\", \"client_uri\": \"s_client_uri_103\", \"web_origins\": [\"s_web_origins_104\"]}",
            List.of()),
        params(
            "ApplicationUpdateParams",
            () ->
                ApplicationUpdateParams.builder()
                    .name("s_name_123")
                    .redirectUris(List.of("s_redirect_uris_124"))
                    .postLogoutUris(List.of("s_post_logout_uris_125"))
                    .grantTypes(List.of("s_grant_types_126"))
                    .scopes(List.of("s_scopes_127"))
                    .pkceExemptUntil(Instant.parse("2026-11-05T10:00:00Z"))
                    .backchannelLogoutUri("s_backchannel_logout_uri_128")
                    .frontchannelLogoutUri("s_frontchannel_logout_uri_129")
                    .logoUri("s_logo_uri_130")
                    .clientUri("s_client_uri_131")
                    .webOrigins(List.of("s_web_origins_132"))
                    .build(),
            "{\"name\": \"s_name_123\", \"redirect_uris\": [\"s_redirect_uris_124\"], \"post_logout_uris\": [\"s_post_logout_uris_125\"], \"grant_types\": [\"s_grant_types_126\"], \"scopes\": [\"s_scopes_127\"], \"pkce_exempt_until\": \"2026-11-05T10:00:00Z\", \"backchannel_logout_uri\": \"s_backchannel_logout_uri_128\", \"frontchannel_logout_uri\": \"s_frontchannel_logout_uri_129\", \"logo_uri\": \"s_logo_uri_130\", \"client_uri\": \"s_client_uri_131\", \"web_origins\": [\"s_web_origins_132\"]}",
            List.of()),
        params(
            "AuditAnomalyListParams",
            () -> AuditAnomalyListParams.builder().status("s_status_144").limit(7).build(),
            "{\"status\": \"s_status_144\", \"limit\": 7}",
            List.of()),
        params(
            "AuditLogListParams",
            () ->
                AuditLogListParams.builder()
                    .limit(7)
                    .cursor("s_cursor_159")
                    .q("s_q_160")
                    .action("s_action_161")
                    .resourceType("s_resource_type_162")
                    .resourceId("s_resource_id_163")
                    .actorUserId("s_actor_user_id_164")
                    .ip("s_ip_165")
                    .from("s_from_166")
                    .to("s_to_167")
                    .build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_159\", \"q\": \"s_q_160\", \"action\": \"s_action_161\", \"resource_type\": \"s_resource_type_162\", \"resource_id\": \"s_resource_id_163\", \"actor_user_id\": \"s_actor_user_id_164\", \"ip\": \"s_ip_165\", \"from\": \"s_from_166\", \"to\": \"s_to_167\"}",
            List.of()),
        params(
            "AuthHookCreateParams",
            () ->
                AuthHookCreateParams.builder()
                    .url("s_url_174")
                    .secret("s_secret_175")
                    .failOpen(true)
                    .build(),
            "{\"url\": \"s_url_174\", \"secret\": \"s_secret_175\", \"fail_open\": true}",
            List.of("s_secret_175")),
        params(
            "AuthHookUpdateParams",
            () ->
                AuthHookUpdateParams.builder()
                    .url("s_url_179")
                    .secret("s_secret_180")
                    .enabled(true)
                    .failOpen(true)
                    .build(),
            "{\"url\": \"s_url_179\", \"secret\": \"s_secret_180\", \"enabled\": true, \"fail_open\": true}",
            List.of("s_secret_180")),
        params(
            "AuthPolicyUpdateParams",
            () ->
                AuthPolicyUpdateParams.builder()
                    .passwordEnabled(true)
                    .passwordMinLength(7)
                    .passwordRequireUppercase(true)
                    .passwordRequireNumber(true)
                    .passwordRequireSymbol(true)
                    .magicLinkEnabled(true)
                    .magicLinkTtlMinutes(7)
                    .passkeyEnabled(true)
                    .otpEmailEnabled(true)
                    .otpSmsEnabled(true)
                    .selfRegistrationEnabled(true)
                    .rememberDeviceEnabled(true)
                    .rememberDeviceDays(7)
                    .lockoutThreshold(7)
                    .lockoutWindowMinutes(7)
                    .lockoutDurationMinutes(7)
                    .build(),
            "{\"password_enabled\": true, \"password_min_length\": 7, \"password_require_uppercase\": true, \"password_require_number\": true, \"password_require_symbol\": true, \"magic_link_enabled\": true, \"magic_link_ttl_minutes\": 7, \"passkey_enabled\": true, \"otp_email_enabled\": true, \"otp_sms_enabled\": true, \"self_registration_enabled\": true, \"remember_device_enabled\": true, \"remember_device_days\": 7, \"lockout_threshold\": 7, \"lockout_window_minutes\": 7, \"lockout_duration_minutes\": 7}",
            List.of()),
        params(
            "AuthorizationCodeGrantParams",
            () ->
                AuthorizationCodeGrantParams.builder()
                    .clientId("s_client_id_182")
                    .clientSecret("s_client_secret_183")
                    .code("s_code_184")
                    .redirectUri("s_redirect_uri_185")
                    .codeVerifier("s_code_verifier_186")
                    .resource("s_resource_187")
                    .build(),
            "{\"client_id\": \"s_client_id_182\", \"client_secret\": \"s_client_secret_183\", \"code\": \"s_code_184\", \"redirect_uri\": \"s_redirect_uri_185\", \"code_verifier\": \"s_code_verifier_186\", \"resource\": \"s_resource_187\"}",
            List.of("s_client_secret_183", "s_code_184", "s_code_verifier_186")),
        params(
            "BackchannelAuthenticationParams",
            () ->
                BackchannelAuthenticationParams.builder()
                    .clientId("s_client_id_192")
                    .clientSecret("s_client_secret_193")
                    .loginHint("s_login_hint_194")
                    .scope("s_scope_195")
                    .bindingMessage("s_binding_message_196")
                    .build(),
            "{\"client_id\": \"s_client_id_192\", \"client_secret\": \"s_client_secret_193\", \"login_hint\": \"s_login_hint_194\", \"scope\": \"s_scope_195\", \"binding_message\": \"s_binding_message_196\"}",
            List.of("s_client_secret_193")),
        params(
            "BillingProfileUpdateParams",
            () ->
                BillingProfileUpdateParams.builder()
                    .legalName("s_legal_name_207")
                    .billingEmail("s_billing_email_208")
                    .addressLine1("s_address_line1_209")
                    .addressLine2("s_address_line2_210")
                    .city("s_city_211")
                    .state("s_state_212")
                    .postalCode("s_postal_code_213")
                    .country("s_country_214")
                    .taxId("s_tax_id_215")
                    .taxIdType("s_tax_id_type_216")
                    .build(),
            "{\"legal_name\": \"s_legal_name_207\", \"billing_email\": \"s_billing_email_208\", \"address_line1\": \"s_address_line1_209\", \"address_line2\": \"s_address_line2_210\", \"city\": \"s_city_211\", \"state\": \"s_state_212\", \"postal_code\": \"s_postal_code_213\", \"country\": \"s_country_214\", \"tax_id\": \"s_tax_id_215\", \"tax_id_type\": \"s_tax_id_type_216\"}",
            List.of()),
        params(
            "BotActivityParams",
            () ->
                BotActivityParams.builder()
                    .limit(7)
                    .cursor("s_cursor_225")
                    .verdict("s_verdict_226")
                    .build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_225\", \"verdict\": \"s_verdict_226\"}",
            List.of()),
        params(
            "BrandingUpdateParams",
            () ->
                BrandingUpdateParams.builder()
                    .logoUrl("s_logo_url_234")
                    .primaryColor("s_primary_color_235")
                    .secondaryColor("s_secondary_color_236")
                    .customDomain("s_custom_domain_237")
                    .emailFromName("s_email_from_name_238")
                    .emailFromAddress("s_email_from_address_239")
                    .settings(Map.of("k", "v"))
                    .build(),
            "{\"logo_url\": \"s_logo_url_234\", \"primary_color\": \"s_primary_color_235\", \"secondary_color\": \"s_secondary_color_236\", \"custom_domain\": \"s_custom_domain_237\", \"email_from_name\": \"s_email_from_name_238\", \"email_from_address\": \"s_email_from_address_239\", \"settings\": {\"k\": \"v\"}}",
            List.of()),
        params(
            "BulkUserRow",
            () ->
                BulkUserRow.builder()
                    .email("s_email_250")
                    .password("s_password_251")
                    .displayName("s_display_name_252")
                    .phone("s_phone_253")
                    .build(),
            "{\"email\": \"s_email_250\", \"password\": \"s_password_251\", \"display_name\": \"s_display_name_252\", \"phone\": \"s_phone_253\"}",
            List.of("s_password_251")),
        params(
            "CheckoutParams",
            () ->
                CheckoutParams.builder()
                    .planCode("s_plan_code_254")
                    .currency("s_currency_255")
                    .country("s_country_256")
                    .successUrl("s_success_url_257")
                    .cancelUrl("s_cancel_url_258")
                    .build(),
            "{\"plan_code\": \"s_plan_code_254\", \"currency\": \"s_currency_255\", \"country\": \"s_country_256\", \"success_url\": \"s_success_url_257\", \"cancel_url\": \"s_cancel_url_258\"}",
            List.of()),
        params(
            "CibaGrantParams",
            () ->
                CibaGrantParams.builder()
                    .clientId("s_client_id_262")
                    .clientSecret("s_client_secret_263")
                    .authReqId("s_auth_req_id_264")
                    .build(),
            "{\"client_id\": \"s_client_id_262\", \"client_secret\": \"s_client_secret_263\", \"auth_req_id\": \"s_auth_req_id_264\"}",
            List.of("s_client_secret_263", "s_auth_req_id_264")),
        params(
            "DecisionListParams",
            () ->
                DecisionListParams.builder()
                    .limit(7)
                    .cursor("s_cursor_278")
                    .subject("s_subject_279")
                    .source("s_source_280")
                    .allowed(true)
                    .days(7)
                    .build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_278\", \"subject\": \"s_subject_279\", \"source\": \"s_source_280\", \"allowed\": true, \"days\": 7}",
            List.of()),
        params(
            "DeletedUserListParams",
            () -> DeletedUserListParams.builder().limit(7).build(),
            "{\"limit\": 7}",
            List.of()),
        params(
            "DeviceAuthorizationParams",
            () ->
                DeviceAuthorizationParams.builder()
                    .clientId("s_client_id_300")
                    .clientSecret("s_client_secret_301")
                    .scope("s_scope_302")
                    .build(),
            "{\"client_id\": \"s_client_id_300\", \"client_secret\": \"s_client_secret_301\", \"scope\": \"s_scope_302\"}",
            List.of("s_client_secret_301")),
        params(
            "DeviceCodeGrantParams",
            () ->
                DeviceCodeGrantParams.builder()
                    .clientId("s_client_id_303")
                    .clientSecret("s_client_secret_304")
                    .deviceCode("s_device_code_305")
                    .build(),
            "{\"client_id\": \"s_client_id_303\", \"client_secret\": \"s_client_secret_304\", \"device_code\": \"s_device_code_305\"}",
            List.of("s_client_secret_304", "s_device_code_305")),
        params(
            "DomainUpdateParams",
            () -> DomainUpdateParams.builder().ssoEnabled(true).jitEnabled(true).build(),
            "{\"sso_enabled\": true, \"jit_enabled\": true}",
            List.of()),
        params(
            "EmailTemplatePreviewParams",
            () ->
                EmailTemplatePreviewParams.builder()
                    .vars(Map.of("k", "v"))
                    .locale("s_locale_363")
                    .draft(
                        new EmailTemplateContent(
                            "s_subject_364",
                            "s_body_365",
                            new EmailTemplateOptions(
                                "s_preheader_366",
                                "s_body_html_367",
                                "s_default_locale_368",
                                true,
                                "s_alignment_369",
                                Map.of(
                                    "k",
                                    new EmailLocalizedContent(
                                        "s_subject_370",
                                        "s_body_371",
                                        "s_preheader_372",
                                        "s_body_html_373")))))
                    .build(),
            "{\"vars\": {\"k\": \"v\"}, \"locale\": \"s_locale_363\", \"draft\": {\"subject\": \"s_subject_364\", \"body\": \"s_body_365\", \"options\": {\"preheader\": \"s_preheader_366\", \"body_html\": \"s_body_html_367\", \"default_locale\": \"s_default_locale_368\", \"show_logo\": true, \"alignment\": \"s_alignment_369\", \"translations\": {\"k\": {\"subject\": \"s_subject_370\", \"body\": \"s_body_371\", \"preheader\": \"s_preheader_372\", \"body_html\": \"s_body_html_373\"}}}}}",
            List.of()),
        params(
            "EmailTemplateTestParams",
            () ->
                EmailTemplateTestParams.builder()
                    .draft(
                        new EmailTemplateContent(
                            "s_subject_374",
                            "s_body_375",
                            new EmailTemplateOptions(
                                "s_preheader_376",
                                "s_body_html_377",
                                "s_default_locale_378",
                                true,
                                "s_alignment_379",
                                Map.of(
                                    "k",
                                    new EmailLocalizedContent(
                                        "s_subject_380",
                                        "s_body_381",
                                        "s_preheader_382",
                                        "s_body_html_383")))))
                    .locale("s_locale_384")
                    .build(),
            "{\"draft\": {\"subject\": \"s_subject_374\", \"body\": \"s_body_375\", \"options\": {\"preheader\": \"s_preheader_376\", \"body_html\": \"s_body_html_377\", \"default_locale\": \"s_default_locale_378\", \"show_logo\": true, \"alignment\": \"s_alignment_379\", \"translations\": {\"k\": {\"subject\": \"s_subject_380\", \"body\": \"s_body_381\", \"preheader\": \"s_preheader_382\", \"body_html\": \"s_body_html_383\"}}}}, \"locale\": \"s_locale_384\"}",
            List.of()),
        params(
            "ErasureCreateParams",
            () ->
                ErasureCreateParams.builder()
                    .tenantId("s_tenant_id_386")
                    .userId("s_user_id_387")
                    .reason("s_reason_388")
                    .build(),
            "{\"tenant_id\": \"s_tenant_id_386\", \"user_id\": \"s_user_id_387\", \"reason\": \"s_reason_388\"}",
            List.of()),
        params(
            "EvaluationParams",
            () ->
                EvaluationParams.builder()
                    .subject(new AuthzenEntity("s_type_397", "s_id_398", Map.of("k", "v")))
                    .resource(new AuthzenEntity("s_type_399", "s_id_400", Map.of("k", "v")))
                    .action(new AuthzenAction("s_name_401", Map.of("k", "v")))
                    .context(Map.of("k", "v"))
                    .build(),
            "{\"subject\": {\"type\": \"s_type_397\", \"id\": \"s_id_398\", \"properties\": {\"k\": \"v\"}}, \"resource\": {\"type\": \"s_type_399\", \"id\": \"s_id_400\", \"properties\": {\"k\": \"v\"}}, \"action\": {\"name\": \"s_name_401\", \"properties\": {\"k\": \"v\"}}, \"context\": {\"k\": \"v\"}}",
            List.of()),
        params(
            "EvaluationsParams",
            () ->
                EvaluationsParams.builder()
                    .subject(new AuthzenEntity("s_type_402", "s_id_403", Map.of("k", "v")))
                    .resource(new AuthzenEntity("s_type_404", "s_id_405", Map.of("k", "v")))
                    .action(new AuthzenAction("s_name_406", Map.of("k", "v")))
                    .context(Map.of("k", "v"))
                    .evaluations(
                        List.of(
                            EvaluationParams.builder()
                                .subject(
                                    new AuthzenEntity("s_type_407", "s_id_408", Map.of("k", "v")))
                                .resource(
                                    new AuthzenEntity("s_type_409", "s_id_410", Map.of("k", "v")))
                                .action(new AuthzenAction("s_name_411", Map.of("k", "v")))
                                .context(Map.of("k", "v"))
                                .build()))
                    .options(Map.of("k", "v"))
                    .build(),
            "{\"subject\": {\"type\": \"s_type_402\", \"id\": \"s_id_403\", \"properties\": {\"k\": \"v\"}}, \"resource\": {\"type\": \"s_type_404\", \"id\": \"s_id_405\", \"properties\": {\"k\": \"v\"}}, \"action\": {\"name\": \"s_name_406\", \"properties\": {\"k\": \"v\"}}, \"context\": {\"k\": \"v\"}, \"evaluations\": [{\"subject\": {\"type\": \"s_type_407\", \"id\": \"s_id_408\", \"properties\": {\"k\": \"v\"}}, \"resource\": {\"type\": \"s_type_409\", \"id\": \"s_id_410\", \"properties\": {\"k\": \"v\"}}, \"action\": {\"name\": \"s_name_411\", \"properties\": {\"k\": \"v\"}}, \"context\": {\"k\": \"v\"}}], \"options\": {\"k\": \"v\"}}",
            List.of()),
        params(
            "ExportCreateParams",
            () ->
                ExportCreateParams.builder()
                    .tenantId("s_tenant_id_412")
                    .userId("s_user_id_413")
                    .build(),
            "{\"tenant_id\": \"s_tenant_id_412\", \"user_id\": \"s_user_id_413\"}",
            List.of()),
        params(
            "GroupCreateParams",
            () ->
                GroupCreateParams.builder()
                    .tenantId("s_tenant_id_434")
                    .name("s_name_435")
                    .description("s_description_436")
                    .parentId("s_parent_id_437")
                    .build(),
            "{\"tenant_id\": \"s_tenant_id_434\", \"name\": \"s_name_435\", \"description\": \"s_description_436\", \"parent_id\": \"s_parent_id_437\"}",
            List.of()),
        params(
            "GroupListParams",
            () -> GroupListParams.builder().role("s_role_438").grants("s_grants_439").build(),
            "{\"role\": \"s_role_438\", \"grants\": \"s_grants_439\"}",
            List.of()),
        params(
            "GroupUpdateParams",
            () ->
                GroupUpdateParams.builder()
                    .name("s_name_445")
                    .description("s_description_446")
                    .parentId("s_parent_id_447")
                    .build(),
            "{\"name\": \"s_name_445\", \"description\": \"s_description_446\", \"parent_id\": \"s_parent_id_447\"}",
            List.of()),
        params(
            "InvitationCreateParams",
            () ->
                InvitationCreateParams.builder()
                    .tenantId("s_tenant_id_456")
                    .email("s_email_457")
                    .roleId("s_role_id_458")
                    .build(),
            "{\"tenant_id\": \"s_tenant_id_456\", \"email\": \"s_email_457\", \"role_id\": \"s_role_id_458\"}",
            List.of()),
        params(
            "InvoicePayParams",
            () ->
                InvoicePayParams.builder()
                    .successUrl("s_success_url_490")
                    .cancelUrl("s_cancel_url_491")
                    .build(),
            "{\"success_url\": \"s_success_url_490\", \"cancel_url\": \"s_cancel_url_491\"}",
            List.of()),
        params(
            "IpRuleCreateParams",
            () ->
                IpRuleCreateParams.builder()
                    .action("s_action_501")
                    .cidr("s_cidr_502")
                    .label("s_label_503")
                    .build(),
            "{\"action\": \"s_action_501\", \"cidr\": \"s_cidr_502\", \"label\": \"s_label_503\"}",
            List.of()),
        params(
            "LdapConnectionCreateParams",
            () ->
                LdapConnectionCreateParams.builder()
                    .name("s_name_516")
                    .serverUrl("s_server_url_517")
                    .bindDn("s_bind_dn_518")
                    .bindPassword("s_bind_password_519")
                    .baseDn("s_base_dn_520")
                    .startTls(true)
                    .skipTlsVerify(true)
                    .userFilter("s_user_filter_521")
                    .emailAttribute("s_email_attribute_522")
                    .nameAttribute("s_name_attribute_523")
                    .status("s_status_524")
                    .build(),
            "{\"name\": \"s_name_516\", \"server_url\": \"s_server_url_517\", \"bind_dn\": \"s_bind_dn_518\", \"bind_password\": \"s_bind_password_519\", \"base_dn\": \"s_base_dn_520\", \"start_tls\": true, \"skip_tls_verify\": true, \"user_filter\": \"s_user_filter_521\", \"email_attribute\": \"s_email_attribute_522\", \"name_attribute\": \"s_name_attribute_523\", \"status\": \"s_status_524\"}",
            List.of("s_bind_password_519")),
        params(
            "LdapConnectionUpdateParams",
            () ->
                LdapConnectionUpdateParams.builder()
                    .name("s_name_525")
                    .serverUrl("s_server_url_526")
                    .bindDn("s_bind_dn_527")
                    .bindPassword("s_bind_password_528")
                    .baseDn("s_base_dn_529")
                    .startTls(true)
                    .skipTlsVerify(true)
                    .userFilter("s_user_filter_530")
                    .emailAttribute("s_email_attribute_531")
                    .nameAttribute("s_name_attribute_532")
                    .status("s_status_533")
                    .build(),
            "{\"name\": \"s_name_525\", \"server_url\": \"s_server_url_526\", \"bind_dn\": \"s_bind_dn_527\", \"bind_password\": \"s_bind_password_528\", \"base_dn\": \"s_base_dn_529\", \"start_tls\": true, \"skip_tls_verify\": true, \"user_filter\": \"s_user_filter_530\", \"email_attribute\": \"s_email_attribute_531\", \"name_attribute\": \"s_name_attribute_532\", \"status\": \"s_status_533\"}",
            List.of("s_bind_password_528")),
        params(
            "LogSinkCreateParams",
            () ->
                LogSinkCreateParams.builder()
                    .type("s_type_544")
                    .endpoint("s_endpoint_545")
                    .token("s_token_546")
                    .build(),
            "{\"type\": \"s_type_544\", \"endpoint\": \"s_endpoint_545\", \"token\": \"s_token_546\"}",
            List.of("s_token_546")),
        params(
            "OAuthGrantListParams",
            () ->
                OAuthGrantListParams.builder()
                    .limit(7)
                    .cursor("s_cursor_572")
                    .clientId("s_client_id_573")
                    .build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_572\", \"client_id\": \"s_client_id_573\"}",
            List.of()),
        params(
            "OAuthResourceCreateParams",
            () ->
                OAuthResourceCreateParams.builder()
                    .identifier("s_identifier_579")
                    .name("s_name_580")
                    .serverClientId("s_server_client_id_581")
                    .build(),
            "{\"identifier\": \"s_identifier_579\", \"name\": \"s_name_580\", \"server_client_id\": \"s_server_client_id_581\"}",
            List.of()),
        params(
            "PermissionCheckParams",
            () ->
                PermissionCheckParams.builder()
                    .userId("s_user_id_589")
                    .tenantId("s_tenant_id_590")
                    .permission("s_permission_591")
                    .build(),
            "{\"user_id\": \"s_user_id_589\", \"tenant_id\": \"s_tenant_id_590\", \"permission\": \"s_permission_591\"}",
            List.of()),
        params(
            "RateLimitsUpdateParams",
            () ->
                RateLimitsUpdateParams.builder()
                    .tenant(new RateLimitBucket(0.5, 7))
                    .user(new RateLimitBucket(0.5, 7))
                    .apiKey(new RateLimitBucket(0.5, 7))
                    .build(),
            "{\"tenant\": {\"rate\": 0.5, \"capacity\": 7}, \"user\": {\"rate\": 0.5, \"capacity\": 7}, \"api_key\": {\"rate\": 0.5, \"capacity\": 7}}",
            List.of()),
        params(
            "RefreshTokenGrantParams",
            () ->
                RefreshTokenGrantParams.builder()
                    .clientId("s_client_id_606")
                    .clientSecret("s_client_secret_607")
                    .refreshToken("s_refresh_token_608")
                    .scope("s_scope_609")
                    .resource("s_resource_610")
                    .build(),
            "{\"client_id\": \"s_client_id_606\", \"client_secret\": \"s_client_secret_607\", \"refresh_token\": \"s_refresh_token_608\", \"scope\": \"s_scope_609\", \"resource\": \"s_resource_610\"}",
            List.of("s_client_secret_607", "s_refresh_token_608")),
        params(
            "RelationCheckParams",
            () ->
                RelationCheckParams.builder()
                    .object("s_object_611")
                    .relation("s_relation_612")
                    .userId("s_user_id_613")
                    .build(),
            "{\"object\": \"s_object_611\", \"relation\": \"s_relation_612\", \"user_id\": \"s_user_id_613\"}",
            List.of()),
        params(
            "RelationGraphParams",
            () ->
                RelationGraphParams.builder()
                    .object("s_object_629")
                    .relation("s_relation_630")
                    .depth(7)
                    .build(),
            "{\"object\": \"s_object_629\", \"relation\": \"s_relation_630\", \"depth\": 7}",
            List.of()),
        params(
            "RelationObjectListParams",
            () ->
                RelationObjectListParams.builder()
                    .limit(7)
                    .cursor("s_cursor_633")
                    .type("s_type_634")
                    .build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_633\", \"type\": \"s_type_634\"}",
            List.of()),
        params(
            "RelationTupleCreateParams",
            () ->
                RelationTupleCreateParams.builder()
                    .object("s_object_641")
                    .relation("s_relation_642")
                    .subject("s_subject_643")
                    .build(),
            "{\"object\": \"s_object_641\", \"relation\": \"s_relation_642\", \"subject\": \"s_subject_643\"}",
            List.of()),
        params(
            "RetentionPolicyUpdateParams",
            () ->
                RetentionPolicyUpdateParams.builder()
                    .deletedUsersEnabled(true)
                    .deletedUsersDays(7)
                    .build(),
            "{\"deleted_users_enabled\": true, \"deleted_users_days\": 7}",
            List.of()),
        params(
            "RiskSettingsUpdateParams",
            () ->
                RiskSettingsUpdateParams.builder()
                    .mediumThreshold(0.5)
                    .highThreshold(0.5)
                    .forceMfaAtLevel("s_force_mfa_at_level_647")
                    .impossibleTravelEnabled(true)
                    .minTravelHours(0.5)
                    .deviceReputationEnabled(true)
                    .build(),
            "{\"medium_threshold\": 0.5, \"high_threshold\": 0.5, \"force_mfa_at_level\": \"s_force_mfa_at_level_647\", \"impossible_travel_enabled\": true, \"min_travel_hours\": 0.5, \"device_reputation_enabled\": true}",
            List.of()),
        params(
            "RoleCreateParams",
            () ->
                RoleCreateParams.builder()
                    .name("s_name_652")
                    .description("s_description_653")
                    .build(),
            "{\"name\": \"s_name_652\", \"description\": \"s_description_653\"}",
            List.of()),
        params(
            "RoleHoldersParams",
            () -> RoleHoldersParams.builder().limit(7).cursor("s_cursor_662").build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_662\"}",
            List.of()),
        params(
            "RoleUpdateParams",
            () ->
                RoleUpdateParams.builder()
                    .name("s_name_663")
                    .description("s_description_664")
                    .build(),
            "{\"name\": \"s_name_663\", \"description\": \"s_description_664\"}",
            List.of()),
        params(
            "SamlConnectionCreateParams",
            () ->
                SamlConnectionCreateParams.builder()
                    .name("s_name_693")
                    .idpEntityId("s_idp_entity_id_694")
                    .idpSsoUrl("s_idp_sso_url_695")
                    .idpCertificate("s_idp_certificate_696")
                    .emailAttribute("s_email_attribute_697")
                    .nameAttribute("s_name_attribute_698")
                    .groupsAttribute("s_groups_attribute_699")
                    .status("s_status_700")
                    .build(),
            "{\"name\": \"s_name_693\", \"idp_entity_id\": \"s_idp_entity_id_694\", \"idp_sso_url\": \"s_idp_sso_url_695\", \"idp_certificate\": \"s_idp_certificate_696\", \"email_attribute\": \"s_email_attribute_697\", \"name_attribute\": \"s_name_attribute_698\", \"groups_attribute\": \"s_groups_attribute_699\", \"status\": \"s_status_700\"}",
            List.of()),
        params(
            "SamlConnectionImportParams",
            () ->
                SamlConnectionImportParams.builder()
                    .name("s_name_701")
                    .metadataUrl("s_metadata_url_702")
                    .metadataXml("s_metadata_xml_703")
                    .idpEntityId("s_idp_entity_id_704")
                    .emailAttribute("s_email_attribute_705")
                    .nameAttribute("s_name_attribute_706")
                    .groupsAttribute("s_groups_attribute_707")
                    .status("s_status_708")
                    .build(),
            "{\"name\": \"s_name_701\", \"metadata_url\": \"s_metadata_url_702\", \"metadata_xml\": \"s_metadata_xml_703\", \"idp_entity_id\": \"s_idp_entity_id_704\", \"email_attribute\": \"s_email_attribute_705\", \"name_attribute\": \"s_name_attribute_706\", \"groups_attribute\": \"s_groups_attribute_707\", \"status\": \"s_status_708\"}",
            List.of()),
        params(
            "SamlConnectionUpdateParams",
            () ->
                SamlConnectionUpdateParams.builder()
                    .name("s_name_709")
                    .idpEntityId("s_idp_entity_id_710")
                    .idpSsoUrl("s_idp_sso_url_711")
                    .idpCertificate("s_idp_certificate_712")
                    .emailAttribute("s_email_attribute_713")
                    .nameAttribute("s_name_attribute_714")
                    .groupsAttribute("s_groups_attribute_715")
                    .status("s_status_716")
                    .build(),
            "{\"name\": \"s_name_709\", \"idp_entity_id\": \"s_idp_entity_id_710\", \"idp_sso_url\": \"s_idp_sso_url_711\", \"idp_certificate\": \"s_idp_certificate_712\", \"email_attribute\": \"s_email_attribute_713\", \"name_attribute\": \"s_name_attribute_714\", \"groups_attribute\": \"s_groups_attribute_715\", \"status\": \"s_status_716\"}",
            List.of()),
        params(
            "SamlMetadataParams",
            () ->
                SamlMetadataParams.builder()
                    .metadataUrl("s_metadata_url_717")
                    .metadataXml("s_metadata_xml_718")
                    .build(),
            "{\"metadata_url\": \"s_metadata_url_717\", \"metadata_xml\": \"s_metadata_xml_718\"}",
            List.of()),
        params(
            "SamlServiceProviderCreateParams",
            () ->
                SamlServiceProviderCreateParams.builder()
                    .name("s_name_740")
                    .entityId("s_entity_id_741")
                    .acsUrl("s_acs_url_742")
                    .nameIdFormat("s_name_id_format_743")
                    .nameIdAttribute("s_name_id_attribute_744")
                    .certificate("s_certificate_745")
                    .requireSignedRequests(true)
                    .encryptAssertions(true)
                    .attributes(
                        List.of(
                            new SamlAttributeMapping(
                                "s_name_746", "s_source_747", "s_name_format_748")))
                    .assignmentRequired(true)
                    .build(),
            "{\"name\": \"s_name_740\", \"entity_id\": \"s_entity_id_741\", \"acs_url\": \"s_acs_url_742\", \"name_id_format\": \"s_name_id_format_743\", \"name_id_attribute\": \"s_name_id_attribute_744\", \"certificate\": \"s_certificate_745\", \"require_signed_requests\": true, \"encrypt_assertions\": true, \"attributes\": [{\"name\": \"s_name_746\", \"source\": \"s_source_747\", \"name_format\": \"s_name_format_748\"}], \"assignment_required\": true}",
            List.of()),
        params(
            "SamlServiceProviderUpdateParams",
            () ->
                SamlServiceProviderUpdateParams.builder()
                    .name("s_name_749")
                    .entityId("s_entity_id_750")
                    .acsUrl("s_acs_url_751")
                    .nameIdFormat("s_name_id_format_752")
                    .nameIdAttribute("s_name_id_attribute_753")
                    .certificate("s_certificate_754")
                    .requireSignedRequests(true)
                    .encryptAssertions(true)
                    .attributes(
                        List.of(
                            new SamlAttributeMapping(
                                "s_name_755", "s_source_756", "s_name_format_757")))
                    .assignmentRequired(true)
                    .build(),
            "{\"name\": \"s_name_749\", \"entity_id\": \"s_entity_id_750\", \"acs_url\": \"s_acs_url_751\", \"name_id_format\": \"s_name_id_format_752\", \"name_id_attribute\": \"s_name_id_attribute_753\", \"certificate\": \"s_certificate_754\", \"require_signed_requests\": true, \"encrypt_assertions\": true, \"attributes\": [{\"name\": \"s_name_755\", \"source\": \"s_source_756\", \"name_format\": \"s_name_format_757\"}], \"assignment_required\": true}",
            List.of()),
        params(
            "ScimTokenCreateParams",
            () ->
                ScimTokenCreateParams.builder()
                    .name("s_name_772")
                    .expiresAt(Instant.parse("2026-11-05T10:00:00Z"))
                    .build(),
            "{\"name\": \"s_name_772\", \"expires_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        params(
            "ScimTokenRotateParams",
            () ->
                ScimTokenRotateParams.builder()
                    .overlapHours(7)
                    .expiresAt(Instant.parse("2026-11-05T10:00:00Z"))
                    .build(),
            "{\"overlap_hours\": 7, \"expires_at\": \"2026-11-05T10:00:00Z\"}",
            List.of()),
        params(
            "SecretCreateParams",
            () ->
                SecretCreateParams.builder()
                    .name("s_name_803")
                    .scope("s_scope_804")
                    .value("s_value_805")
                    .rotationIntervalDays(7)
                    .build(),
            "{\"name\": \"s_name_803\", \"scope\": \"s_scope_804\", \"value\": \"s_value_805\", \"rotation_interval_days\": 7}",
            List.of("s_value_805")),
        params(
            "SecretUpdateParams",
            () ->
                SecretUpdateParams.builder()
                    .scope("s_scope_806")
                    .value("s_value_807")
                    .rotationIntervalDays(7)
                    .build(),
            "{\"scope\": \"s_scope_806\", \"value\": \"s_value_807\", \"rotation_interval_days\": 7}",
            List.of("s_value_807")),
        params(
            "SecurityPolicyUpdateParams",
            () ->
                SecurityPolicyUpdateParams.builder()
                    .passwordMinLength(7)
                    .passwordComplexity("s_password_complexity_818")
                    .sessionMaxAge("s_session_max_age_819")
                    .sessionIdleTimeout("s_session_idle_timeout_820")
                    .maxSessions(7)
                    .sessionLimitAction("s_session_limit_action_821")
                    .mfaEnforcement("s_mfa_enforcement_822")
                    .mfaGraceDays(7)
                    .mfaEnforcedSince(Instant.parse("2026-11-05T10:00:00Z"))
                    .stepUpWindow("s_step_up_window_823")
                    .stepUpMethods(List.of("s_step_up_methods_824"))
                    .settings(Map.of("k", "v"))
                    .build(),
            "{\"password_min_length\": 7, \"password_complexity\": \"s_password_complexity_818\", \"session_max_age\": \"s_session_max_age_819\", \"session_idle_timeout\": \"s_session_idle_timeout_820\", \"max_sessions\": 7, \"session_limit_action\": \"s_session_limit_action_821\", \"mfa_enforcement\": \"s_mfa_enforcement_822\", \"mfa_grace_days\": 7, \"mfa_enforced_since\": \"2026-11-05T10:00:00Z\", \"step_up_window\": \"s_step_up_window_823\", \"step_up_methods\": [\"s_step_up_methods_824\"], \"settings\": {\"k\": \"v\"}}",
            List.of()),
        params(
            "ServicePrincipalCreateParams",
            () ->
                ServicePrincipalCreateParams.builder()
                    .tenantId("s_tenant_id_830")
                    .name("s_name_831")
                    .description("s_description_832")
                    .scopes(List.of("s_scopes_833"))
                    .build(),
            "{\"tenant_id\": \"s_tenant_id_830\", \"name\": \"s_name_831\", \"description\": \"s_description_832\", \"scopes\": [\"s_scopes_833\"]}",
            List.of()),
        params(
            "ServicePrincipalRotateSecretParams",
            () -> ServicePrincipalRotateSecretParams.builder().overlapHours(7).build(),
            "{\"overlap_hours\": 7}",
            List.of()),
        params(
            "SocialProviderUpdateParams",
            () ->
                SocialProviderUpdateParams.builder()
                    .enabled(true)
                    .clientId("s_client_id_861")
                    .clientSecret("s_client_secret_862")
                    .discoveryUrl("s_discovery_url_863")
                    .appleTeamId("s_apple_team_id_864")
                    .appleKeyId("s_apple_key_id_865")
                    .build(),
            "{\"enabled\": true, \"client_id\": \"s_client_id_861\", \"client_secret\": \"s_client_secret_862\", \"discovery_url\": \"s_discovery_url_863\", \"apple_team_id\": \"s_apple_team_id_864\", \"apple_key_id\": \"s_apple_key_id_865\"}",
            List.of("s_client_secret_862")),
        params(
            "SocialProviderUpsertParams",
            () ->
                SocialProviderUpsertParams.builder()
                    .provider("s_provider_866")
                    .clientId("s_client_id_867")
                    .clientSecret("s_client_secret_868")
                    .discoveryUrl("s_discovery_url_869")
                    .appleTeamId("s_apple_team_id_870")
                    .appleKeyId("s_apple_key_id_871")
                    .build(),
            "{\"provider\": \"s_provider_866\", \"client_id\": \"s_client_id_867\", \"client_secret\": \"s_client_secret_868\", \"discovery_url\": \"s_discovery_url_869\", \"apple_team_id\": \"s_apple_team_id_870\", \"apple_key_id\": \"s_apple_key_id_871\"}",
            List.of("s_client_secret_868")),
        params(
            "SsoGroupMappingCreateParams",
            () ->
                SsoGroupMappingCreateParams.builder()
                    .idpGroup("s_idp_group_876")
                    .groupId("s_group_id_877")
                    .build(),
            "{\"idp_group\": \"s_idp_group_876\", \"group_id\": \"s_group_id_877\"}",
            List.of()),
        params(
            "SubscriptionUpdateParams",
            () ->
                SubscriptionUpdateParams.builder()
                    .planCode("s_plan_code_883")
                    .currency("s_currency_884")
                    .build(),
            "{\"plan_code\": \"s_plan_code_883\", \"currency\": \"s_currency_884\"}",
            List.of()),
        params(
            "TenantUpdateParams",
            () ->
                TenantUpdateParams.builder()
                    .name("s_name_902")
                    .status("s_status_903")
                    .plan("s_plan_904")
                    .metadata(Map.of("k", "v"))
                    .build(),
            "{\"name\": \"s_name_902\", \"status\": \"s_status_903\", \"plan\": \"s_plan_904\", \"metadata\": {\"k\": \"v\"}}",
            List.of()),
        params(
            "TokenExchangeGrantParams",
            () ->
                TokenExchangeGrantParams.builder()
                    .clientId("s_client_id_905")
                    .clientSecret("s_client_secret_906")
                    .subjectToken("s_subject_token_907")
                    .subjectTokenType("s_subject_token_type_908")
                    .actorToken("s_actor_token_909")
                    .actorTokenType("s_actor_token_type_910")
                    .requestedTokenType("s_requested_token_type_911")
                    .scope("s_scope_912")
                    .resource("s_resource_913")
                    .build(),
            "{\"client_id\": \"s_client_id_905\", \"client_secret\": \"s_client_secret_906\", \"subject_token\": \"s_subject_token_907\", \"subject_token_type\": \"s_subject_token_type_908\", \"actor_token\": \"s_actor_token_909\", \"actor_token_type\": \"s_actor_token_type_910\", \"requested_token_type\": \"s_requested_token_type_911\", \"scope\": \"s_scope_912\", \"resource\": \"s_resource_913\"}",
            List.of("s_client_secret_906", "s_subject_token_907", "s_actor_token_909")),
        params(
            "TokenVaultProviderCreateParams",
            () ->
                TokenVaultProviderCreateParams.builder()
                    .provider("s_provider_932")
                    .clientId("s_client_id_933")
                    .clientSecret("s_client_secret_934")
                    .authorizeUrl("s_authorize_url_935")
                    .tokenUrl("s_token_url_936")
                    .scopes("s_scopes_937")
                    .build(),
            "{\"provider\": \"s_provider_932\", \"client_id\": \"s_client_id_933\", \"client_secret\": \"s_client_secret_934\", \"authorize_url\": \"s_authorize_url_935\", \"token_url\": \"s_token_url_936\", \"scopes\": \"s_scopes_937\"}",
            List.of("s_client_secret_934")),
        params(
            "UserBulkCreateParams",
            () ->
                UserBulkCreateParams.builder()
                    .tenantId("s_tenant_id_963")
                    .roleId("s_role_id_964")
                    .users(
                        List.of(
                            BulkUserRow.builder()
                                .email("s_email_965")
                                .password("s_password_966")
                                .displayName("s_display_name_967")
                                .phone("s_phone_968")
                                .build()))
                    .build(),
            "{\"tenant_id\": \"s_tenant_id_963\", \"role_id\": \"s_role_id_964\", \"users\": [{\"email\": \"s_email_965\", \"password\": \"s_password_966\", \"display_name\": \"s_display_name_967\", \"phone\": \"s_phone_968\"}]}",
            List.of()),
        params(
            "UserCreateParams",
            () ->
                UserCreateParams.builder()
                    .tenantId("s_tenant_id_969")
                    .email("s_email_970")
                    .password("s_password_971")
                    .displayName("s_display_name_972")
                    .phone("s_phone_973")
                    .metadata(Map.of("k", "v"))
                    .build(),
            "{\"tenant_id\": \"s_tenant_id_969\", \"email\": \"s_email_970\", \"password\": \"s_password_971\", \"display_name\": \"s_display_name_972\", \"phone\": \"s_phone_973\", \"metadata\": {\"k\": \"v\"}}",
            List.of("s_password_971")),
        params(
            "UserListParams",
            () ->
                UserListParams.builder()
                    .limit(7)
                    .cursor("s_cursor_976")
                    .q("s_q_977")
                    .role("s_role_978")
                    .department("s_department_979")
                    .mfa(true)
                    .emailVerified(true)
                    .status("s_status_980")
                    .lastSeen("s_last_seen_981")
                    .build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_976\", \"q\": \"s_q_977\", \"role\": \"s_role_978\", \"department\": \"s_department_979\", \"mfa\": true, \"email_verified\": true, \"status\": \"s_status_980\", \"last_seen\": \"s_last_seen_981\"}",
            List.of()),
        params(
            "UserUpdateParams",
            () ->
                UserUpdateParams.builder()
                    .displayName("s_display_name_993")
                    .avatarUrl("s_avatar_url_994")
                    .phone("s_phone_995")
                    .status("s_status_996")
                    .metadata(Map.of("k", "v"))
                    .build(),
            "{\"display_name\": \"s_display_name_993\", \"avatar_url\": \"s_avatar_url_994\", \"phone\": \"s_phone_995\", \"status\": \"s_status_996\", \"metadata\": {\"k\": \"v\"}}",
            List.of()),
        params(
            "VerifiableCredentialIssueParams",
            () ->
                VerifiableCredentialIssueParams.builder()
                    .subject("s_subject_1003")
                    .type("s_type_1004")
                    .claims(Map.of("k", "v"))
                    .ttlSeconds(7)
                    .userId("s_user_id_1005")
                    .build(),
            "{\"subject\": \"s_subject_1003\", \"type\": \"s_type_1004\", \"claims\": {\"k\": \"v\"}, \"ttl_seconds\": 7, \"user_id\": \"s_user_id_1005\"}",
            List.of()),
        params(
            "WebhookCreateParams",
            () ->
                WebhookCreateParams.builder()
                    .url("s_url_1006")
                    .events(List.of("s_events_1007"))
                    .tenantId("s_tenant_id_1008")
                    .build(),
            "{\"url\": \"s_url_1006\", \"events\": [\"s_events_1007\"], \"tenant_id\": \"s_tenant_id_1008\"}",
            List.of()),
        params(
            "WebhookDeliveryListParams",
            () ->
                WebhookDeliveryListParams.builder()
                    .limit(7)
                    .cursor("s_cursor_1015")
                    .status("s_status_1016")
                    .build(),
            "{\"limit\": 7, \"cursor\": \"s_cursor_1015\", \"status\": \"s_status_1016\"}",
            List.of()),
        params(
            "WebhookUpdateParams",
            () ->
                WebhookUpdateParams.builder()
                    .url("s_url_1028")
                    .events(List.of("s_events_1029"))
                    .build(),
            "{\"url\": \"s_url_1028\", \"events\": [\"s_events_1029\"]}",
            List.of()));
  }
}
