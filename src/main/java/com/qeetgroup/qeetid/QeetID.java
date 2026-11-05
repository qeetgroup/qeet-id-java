package com.qeetgroup.qeetid;

import com.qeetgroup.qeetid.access.AuthPolicies;
import com.qeetgroup.qeetid.access.Decisions;
import com.qeetgroup.qeetid.access.IpRules;
import com.qeetgroup.qeetid.access.Permissions;
import com.qeetgroup.qeetid.access.Relationships;
import com.qeetgroup.qeetid.access.Roles;
import com.qeetgroup.qeetid.access.SecurityPolicies;
import com.qeetgroup.qeetid.access.ThreatDetection;
import com.qeetgroup.qeetid.developer.Agents;
import com.qeetgroup.qeetid.developer.ApiKeys;
import com.qeetgroup.qeetid.developer.AuthHooks;
import com.qeetgroup.qeetid.developer.Secrets;
import com.qeetgroup.qeetid.developer.TokenVault;
import com.qeetgroup.qeetid.developer.VerifiableCredentials;
import com.qeetgroup.qeetid.developer.Webhooks;
import com.qeetgroup.qeetid.federation.AdminPortalLinks;
import com.qeetgroup.qeetid.federation.Applications;
import com.qeetgroup.qeetid.federation.LdapConnections;
import com.qeetgroup.qeetid.federation.OAuth;
import com.qeetgroup.qeetid.federation.SamlConnections;
import com.qeetgroup.qeetid.federation.SamlServiceProviders;
import com.qeetgroup.qeetid.federation.Scim;
import com.qeetgroup.qeetid.federation.ServicePrincipals;
import com.qeetgroup.qeetid.federation.SocialProviders;
import com.qeetgroup.qeetid.identity.Branding;
import com.qeetgroup.qeetid.identity.Domains;
import com.qeetgroup.qeetid.identity.Groups;
import com.qeetgroup.qeetid.identity.Invitations;
import com.qeetgroup.qeetid.identity.Tenants;
import com.qeetgroup.qeetid.identity.Users;
import com.qeetgroup.qeetid.internal.Headers;
import com.qeetgroup.qeetid.internal.Transport;
import com.qeetgroup.qeetid.internal.UserAgent;
import com.qeetgroup.qeetid.operations.Analytics;
import com.qeetgroup.qeetid.operations.AuditLogs;
import com.qeetgroup.qeetid.operations.Billing;
import com.qeetgroup.qeetid.operations.EmailTemplates;
import com.qeetgroup.qeetid.operations.Gdpr;
import com.qeetgroup.qeetid.operations.LogSinks;
import com.qeetgroup.qeetid.operations.RateLimits;
import com.qeetgroup.qeetid.operations.Retention;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * The Qeet ID API client. Build one per process and share it: it is immutable, safe for concurrent
 * use, and reuses connections and cached tokens.
 *
 * <pre>{@code
 * QeetID qeetId = QeetID.builder().apiKey(System.getenv("QEET_ID_API_KEY")).build();
 * User user = qeetId.users().get(userId);
 * }</pre>
 *
 * <p>Each accessor groups the calls on one kind of resource. Every call is synchronous, throws a
 * {@link com.qeetgroup.qeetid.exception.QeetIDException} on failure, and has an overload taking
 * {@link RequestOptions} last.
 */
public final class QeetID {
  /** The production API. */
  public static final String DEFAULT_BASE_URL = "https://api.id.qeet.in";

  /** How long each attempt may take by default, response body included. */
  public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

  /** How many times a retry-safe request is retried by default. */
  public static final int DEFAULT_MAX_RETRIES = 2;

  private final Transport transport;
  private final String description;

  // Identity: the organisation, its people and groups.
  private final Tenants tenants;
  private final Domains domains;
  private final Users users;
  private final Groups groups;
  private final Invitations invitations;
  private final Branding branding;

  // Access: who may do what, and how people sign in.
  private final Roles roles;
  private final Permissions permissions;
  private final Relationships relationships;
  private final Decisions decisions;
  private final AuthPolicies authPolicies;
  private final SecurityPolicies securityPolicies;
  private final IpRules ipRules;
  private final ThreatDetection threatDetection;

  // Federation: OAuth/OpenID Connect, SAML, directories, SCIM, social sign-in.
  private final OAuth oauth;
  private final Applications applications;
  private final ServicePrincipals servicePrincipals;
  private final SamlConnections samlConnections;
  private final SamlServiceProviders samlServiceProviders;
  private final LdapConnections ldapConnections;
  private final Scim scim;
  private final SocialProviders socialProviders;
  private final AdminPortalLinks adminPortalLinks;

  // Developer: API keys, agents, credentials, hooks, webhooks, secrets.
  private final ApiKeys apiKeys;
  private final Agents agents;
  private final VerifiableCredentials verifiableCredentials;
  private final AuthHooks authHooks;
  private final Webhooks webhooks;
  private final Secrets secrets;
  private final TokenVault tokenVault;

  // Operations: audit, analytics, billing, compliance and limits.
  private final AuditLogs auditLogs;
  private final Analytics analytics;
  private final Billing billing;
  private final Gdpr gdpr;
  private final EmailTemplates emailTemplates;
  private final Retention retention;
  private final LogSinks logSinks;
  private final RateLimits rateLimits;

  private QeetID(Builder builder, String baseUrl) {
    Map<String, String> headers = Map.copyOf(builder.headers);
    HttpClient httpClient = builder.httpClient;
    if (httpClient == null) {
      httpClient =
          HttpClient.newBuilder()
              .connectTimeout(Duration.ofSeconds(10))
              .followRedirects(HttpClient.Redirect.NEVER)
              .build();
    }
    this.transport =
        new Transport(
            new Transport.Config(
                baseUrl,
                builder.apiKey,
                builder.clientId,
                builder.clientSecret,
                builder.timeout,
                builder.maxRetries,
                builder.initialRetryDelay,
                builder.maxRetryDelay,
                headers,
                UserAgent.build(builder.appName, builder.appVersion),
                builder.logger,
                httpClient));
    String credential =
        builder.apiKey != null
            ? "apiKey=[redacted]"
            : builder.clientId != null
                ? "clientId=" + builder.clientId + ", clientSecret=[redacted]"
                : "no credentials";
    this.description = "QeetID[baseUrl=" + baseUrl + ", " + credential + "]";
    this.tenants = new Tenants(transport);
    this.domains = new Domains(transport);
    this.users = new Users(transport);
    this.groups = new Groups(transport);
    this.invitations = new Invitations(transport);
    this.branding = new Branding(transport);
    this.roles = new Roles(transport);
    this.permissions = new Permissions(transport);
    this.relationships = new Relationships(transport);
    this.decisions = new Decisions(transport);
    this.authPolicies = new AuthPolicies(transport);
    this.securityPolicies = new SecurityPolicies(transport);
    this.ipRules = new IpRules(transport);
    this.threatDetection = new ThreatDetection(transport);
    this.oauth = new OAuth(transport);
    this.applications = new Applications(transport);
    this.servicePrincipals = new ServicePrincipals(transport);
    this.samlConnections = new SamlConnections(transport);
    this.samlServiceProviders = new SamlServiceProviders(transport);
    this.ldapConnections = new LdapConnections(transport);
    this.scim = new Scim(transport);
    this.socialProviders = new SocialProviders(transport);
    this.adminPortalLinks = new AdminPortalLinks(transport);
    this.apiKeys = new ApiKeys(transport);
    this.agents = new Agents(transport);
    this.verifiableCredentials = new VerifiableCredentials(transport);
    this.authHooks = new AuthHooks(transport);
    this.webhooks = new Webhooks(transport);
    this.secrets = new Secrets(transport);
    this.tokenVault = new TokenVault(transport);
    this.auditLogs = new AuditLogs(transport);
    this.analytics = new Analytics(transport);
    this.billing = new Billing(transport);
    this.gdpr = new Gdpr(transport);
    this.emailTemplates = new EmailTemplates(transport);
    this.retention = new Retention(transport);
    this.logSinks = new LogSinks(transport);
    this.rateLimits = new RateLimits(transport);
  }

  /**
   * Starts building a client.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * The base URL requests go to.
   *
   * @return the base URL, without a trailing slash
   */
  public String baseUrl() {
    return transport.baseUrl();
  }

  // Identity: the organisation, its people and groups.

  /**
   * Reads and manages the API key's organisation.
   *
   * @return the tenants
   */
  public Tenants tenants() {
    return tenants;
  }

  /**
   * Manages the organisation's domains: claim, verify by DNS, and route sign-in.
   *
   * @return the domains
   */
  public Domains domains() {
    return domains;
  }

  /**
   * Manages the organisation's users.
   *
   * @return the users
   */
  public Users users() {
    return users;
  }

  /**
   * Manages groups, their members and the roles they grant.
   *
   * @return the groups
   */
  public Groups groups() {
    return groups;
  }

  /**
   * Manages invitations.
   *
   * @return the invitations
   */
  public Invitations invitations() {
    return invitations;
  }

  /**
   * Manages the organisation's branding.
   *
   * @return the branding
   */
  public Branding branding() {
    return branding;
  }

  // Access: who may do what, and how people sign in.

  /**
   * Manages roles, the permissions they grant, and who holds them.
   *
   * @return the roles
   */
  public Roles roles() {
    return roles;
  }

  /**
   * Checks permissions and reads the permission catalogue.
   *
   * @return the permissions
   */
  public Permissions permissions() {
    return permissions;
  }

  /**
   * Manages relationship tuples (ReBAC) and answers relationship checks.
   *
   * @return the relationships
   */
  public Relationships relationships() {
    return relationships;
  }

  /**
   * OpenID AuthZEN evaluations over every authorization model, and the log of decisions made.
   *
   * @return the decisions
   */
  public Decisions decisions() {
    return decisions;
  }

  /**
   * Manages the organisation's sign-in policy: methods, password rules and lockout.
   *
   * @return the authPolicies
   */
  public AuthPolicies authPolicies() {
    return authPolicies;
  }

  /**
   * Manages the organisation's security policy.
   *
   * @return the securityPolicies
   */
  public SecurityPolicies securityPolicies() {
    return securityPolicies;
  }

  /**
   * Manages the organisation's address rules: which networks may sign in.
   *
   * @return the ipRules
   */
  public IpRules ipRules() {
    return ipRules;
  }

  /**
   * Bot detection, adaptive-risk settings and sign-in anomalies.
   *
   * @return the threatDetection
   */
  public ThreatDetection threatDetection() {
    return threatDetection;
  }

  // Federation: OAuth/OpenID Connect, SAML, directories, SCIM, social sign-in.

  /**
   * The OAuth 2.0 and OpenID endpoints, with Qeet ID as the authorization server: the token grants,
   * introspection, revocation, the device and backchannel (CIBA) flows, and the administration of
   * grants and devices.
   *
   * @return the oauth
   */
  public OAuth oauth() {
    return oauth;
  }

  /**
   * Manages applications (OAuth / OpenID Connect clients) and the resources they may ask tokens
   * for.
   *
   * @return the applications
   */
  public Applications applications() {
    return applications;
  }

  /**
   * Manages service principals: machine identities for machine-to-machine access.
   *
   * @return the servicePrincipals
   */
  public ServicePrincipals servicePrincipals() {
    return servicePrincipals;
  }

  /**
   * Manages inbound SAML connections (Qeet ID as the service provider).
   *
   * @return the samlConnections
   */
  public SamlConnections samlConnections() {
    return samlConnections;
  }

  /**
   * Manages the service providers that use Qeet ID as their SAML identity provider.
   *
   * @return the samlServiceProviders
   */
  public SamlServiceProviders samlServiceProviders() {
    return samlServiceProviders;
  }

  /**
   * Manages directory connections, and signs people in with their directory password.
   *
   * @return the ldapConnections
   */
  public LdapConnections ldapConnections() {
    return ldapConnections;
  }

  /**
   * Administers SCIM provisioning: tokens, and what was provisioned.
   *
   * @return the scim
   */
  public Scim scim() {
    return scim;
  }

  /**
   * Manages the API key's organisation's social sign-in providers, and linked social accounts.
   *
   * @return the socialProviders
   */
  public SocialProviders socialProviders() {
    return socialProviders;
  }

  /**
   * Manages admin portal links.
   *
   * @return the adminPortalLinks
   */
  public AdminPortalLinks adminPortalLinks() {
    return adminPortalLinks;
  }

  // Developer: API keys, agents, credentials, hooks, webhooks, secrets.

  /**
   * Manages API keys.
   *
   * @return the apiKeys
   */
  public ApiKeys apiKeys() {
    return apiKeys;
  }

  /**
   * Manages AI agent identities: their tokens, secrets and lifecycle.
   *
   * @return the agents
   */
  public Agents agents() {
    return agents;
  }

  /**
   * Issues, lists, revokes and verifies W3C verifiable credentials (JWT-VC).
   *
   * @return the verifiableCredentials
   */
  public VerifiableCredentials verifiableCredentials() {
    return verifiableCredentials;
  }

  /**
   * Manages authentication hooks.
   *
   * @return the authHooks
   */
  public AuthHooks authHooks() {
    return authHooks;
  }

  /**
   * Manages webhook subscriptions.
   *
   * @return the webhooks
   */
  public Webhooks webhooks() {
    return webhooks;
  }

  /**
   * The organisation's encrypted secrets store.
   *
   * @return the secrets
   */
  public Secrets secrets() {
    return secrets;
  }

  /**
   * Third-party OAuth tokens held for your users (Slack, GitHub, Google, or any OAuth 2.0 provider
   * an administrator registers).
   *
   * @return the tokenVault
   */
  public TokenVault tokenVault() {
    return tokenVault;
  }

  // Operations: audit, analytics, billing, compliance and limits.

  /**
   * Reads the hash-chained audit log and verifies its integrity.
   *
   * @return the auditLogs
   */
  public AuditLogs auditLogs() {
    return auditLogs;
  }

  /**
   * Sign-in analytics.
   *
   * @return the analytics
   */
  public Analytics analytics() {
    return analytics;
  }

  /**
   * The organisation's Qeet ID billing: subscription, invoices and plan.
   *
   * @return the billing
   */
  public Billing billing() {
    return billing;
  }

  /**
   * Data-subject requests: erasure and export.
   *
   * @return the gdpr
   */
  public Gdpr gdpr() {
    return gdpr;
  }

  /**
   * Manages the organisation's email templates.
   *
   * @return the emailTemplates
   */
  public EmailTemplates emailTemplates() {
    return emailTemplates;
  }

  /**
   * Manages the organisation's data-retention policy.
   *
   * @return the retention
   */
  public Retention retention() {
    return retention;
  }

  /**
   * Manages audit-log streaming to SIEMs.
   *
   * @return the logSinks
   */
  public LogSinks logSinks() {
    return logSinks;
  }

  /**
   * Manages the organisation's rate limits.
   *
   * @return the rateLimits
   */
  public RateLimits rateLimits() {
    return rateLimits;
  }

  /** The base URL and which credential the client uses; never the credential itself. */
  @Override
  public String toString() {
    return description;
  }

  /**
   * Builds a {@link QeetID} client. Give it one credential: an {@link #apiKey(String) API key}, or
   * a service principal's {@link #clientCredentials(String, String) client credentials}. A client
   * without one can still call the endpoints that authenticate otherwise: the OAuth endpoints,
   * directory sign-in, agent tokens and credential verification.
   */
  public static final class Builder {
    private String baseUrl = DEFAULT_BASE_URL;
    private @Nullable String apiKey;
    private @Nullable String clientId;
    private @Nullable String clientSecret;
    private Duration timeout = DEFAULT_TIMEOUT;
    private int maxRetries = DEFAULT_MAX_RETRIES;
    private Duration initialRetryDelay = Duration.ofMillis(500);
    private Duration maxRetryDelay = Duration.ofSeconds(8);
    private final Map<String, String> headers = new LinkedHashMap<>();
    private @Nullable String appName;
    private @Nullable String appVersion;
    private System.Logger logger = System.getLogger("com.qeetgroup.qeetid");
    private @Nullable HttpClient httpClient;

    private Builder() {}

    /**
     * Authenticates every call with a server-side API key ({@code qk_…}), sent as {@code
     * Authorization: ApiKey <key>}. Keep it out of browsers, source control and logs.
     *
     * @param apiKey the key
     * @return this builder
     */
    public Builder apiKey(String apiKey) {
      Objects.requireNonNull(apiKey, "apiKey");
      if (apiKey.isBlank()) {
        throw new IllegalArgumentException("apiKey must not be blank.");
      }
      this.apiKey = apiKey.trim();
      return this;
    }

    /**
     * Authenticates every call with an access token the client gets for a service principal with
     * the OAuth {@code client_credentials} grant ({@code POST /v1/oauth/token}). The token is
     * cached and replaced shortly before it expires. It carries every scope the principal holds.
     *
     * @param clientId the principal's client ID
     * @param clientSecret its secret
     * @return this builder
     */
    public Builder clientCredentials(String clientId, String clientSecret) {
      Objects.requireNonNull(clientId, "clientId");
      Objects.requireNonNull(clientSecret, "clientSecret");
      if (clientId.isBlank() || clientSecret.isBlank()) {
        throw new IllegalArgumentException("clientId and clientSecret must not be blank.");
      }
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      return this;
    }

    /**
     * The API's base URL. Default {@value QeetID#DEFAULT_BASE_URL}. It must use https, except for a
     * loopback host ({@code localhost}, {@code *.localhost}, {@code 127.0.0.1}, {@code [::1]}): a
     * credential must never cross a network in the clear.
     *
     * @param baseUrl an absolute URL without a query or fragment
     * @return this builder
     */
    public Builder baseUrl(String baseUrl) {
      this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
      return this;
    }

    /**
     * How long each attempt may take, response body included. Default 30 seconds.
     *
     * @param timeout more than zero
     * @return this builder
     */
    public Builder timeout(Duration timeout) {
      Objects.requireNonNull(timeout, "timeout");
      if (timeout.isZero() || timeout.isNegative()) {
        throw new IllegalArgumentException("timeout must be greater than zero.");
      }
      this.timeout = timeout;
      return this;
    }

    /**
     * How many times a retry-safe request is retried after a transient failure. Default 2; 0 turns
     * retries off.
     *
     * @param maxRetries 0 or more
     * @return this builder
     */
    public Builder maxRetries(int maxRetries) {
      if (maxRetries < 0) {
        throw new IllegalArgumentException("maxRetries must be 0 or more.");
      }
      this.maxRetries = maxRetries;
      return this;
    }

    /**
     * The bounds of the exponential backoff between retries, when the server sends no {@code
     * Retry-After}. Default 500 ms doubling to 8 s; each wait is between half and all of it.
     *
     * @param initial the first wait, more than zero
     * @param max the longest wait, at least {@code initial}
     * @return this builder
     */
    public Builder retryBackoff(Duration initial, Duration max) {
      Objects.requireNonNull(initial, "initial");
      Objects.requireNonNull(max, "max");
      if (initial.isZero() || initial.isNegative() || max.compareTo(initial) < 0) {
        throw new IllegalArgumentException("retryBackoff needs 0 < initial <= max.");
      }
      this.initialRetryDelay = initial;
      this.maxRetryDelay = max;
      return this;
    }

    /**
     * Adds a header to every request, such as a tracing header. It can't replace {@code
     * Authorization}, {@code Accept}, {@code Content-Type}, {@code User-Agent} or {@code
     * Idempotency-Key}.
     *
     * @param name the header name
     * @param value its value
     * @return this builder
     */
    public Builder header(String name, String value) {
      Headers.check(name, value);
      headers.put(name.trim(), value);
      return this;
    }

    /**
     * Names your application at the front of the {@code User-Agent}, such as {@code
     * brightleaf-onboarding/1.4.0}.
     *
     * @param name a short token: letters, digits and {@code . _ - @ / +}
     * @param version its version, or null
     * @return this builder
     */
    public Builder appInfo(String name, @Nullable String version) {
      this.appName = UserAgent.checkToken(name, "The application name");
      this.appVersion = version == null ? null : UserAgent.checkToken(version, "The version");
      return this;
    }

    /**
     * Receives the SDK's diagnostics, at {@code DEBUG}: each attempt's method, path, status,
     * duration and request id, and each retry. Never credentials, query strings, headers or bodies.
     * Default {@code System.getLogger("com.qeetgroup.qeetid")}, which SLF4J and Log4j can take over
     * through their {@code System.Logger} bridges.
     *
     * @param logger the logger
     * @return this builder
     */
    public Builder logger(System.Logger logger) {
      this.logger = Objects.requireNonNull(logger, "logger");
      return this;
    }

    /**
     * The HTTP client that sends the requests: for a proxy, custom TLS or an executor of your own.
     * By default one with a 10-second connect timeout that doesn't follow redirects.
     *
     * @param httpClient the client
     * @return this builder
     */
    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
      return this;
    }

    /**
     * Builds the client. Makes no network call.
     *
     * @return the client
     * @throws IllegalStateException when both an API key and client credentials were given
     * @throws IllegalArgumentException when the base URL is not allowed
     */
    public QeetID build() {
      if (apiKey != null && clientId != null) {
        throw new IllegalStateException("Give one credential: an API key or client credentials.");
      }
      return new QeetID(this, normalizeBaseUrl(baseUrl));
    }
  }

  /**
   * Checks and normalises a base URL: absolute, https unless the host is this machine, without user
   * info, query or fragment, and without a trailing slash.
   *
   * @param baseUrl the URL
   * @return the normalised URL
   * @throws IllegalArgumentException when it is not allowed
   */
  static String normalizeBaseUrl(String baseUrl) {
    URI uri;
    try {
      uri = new URI(baseUrl.trim());
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(
          "baseUrl must be an absolute URL, such as " + DEFAULT_BASE_URL + ".", e);
    }
    String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
    String host = uri.getHost();
    if (host == null || !(scheme.equals("https") || scheme.equals("http"))) {
      throw new IllegalArgumentException(
          "baseUrl must be an absolute http(s) URL, such as " + DEFAULT_BASE_URL + ".");
    }
    if (scheme.equals("http") && !isLoopback(host)) {
      throw new IllegalArgumentException(
          "baseUrl must use https (plain http is allowed only for localhost).");
    }
    if (uri.getRawUserInfo() != null || uri.getRawQuery() != null || uri.getRawFragment() != null) {
      throw new IllegalArgumentException(
          "baseUrl must not carry credentials, a query or a fragment.");
    }
    String text = uri.toString();
    while (text.endsWith("/")) {
      text = text.substring(0, text.length() - 1);
    }
    return text;
  }

  private static boolean isLoopback(String host) {
    String h = host.toLowerCase(Locale.ROOT);
    return h.equals("localhost")
        || h.endsWith(".localhost")
        || h.equals("[::1]")
        || h.equals("::1")
        || h.startsWith("127.");
  }
}
