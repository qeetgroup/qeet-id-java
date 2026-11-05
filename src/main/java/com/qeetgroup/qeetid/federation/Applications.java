package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Manages applications (OAuth / OpenID Connect clients) and the resources they may ask tokens for.
 *
 * <p>Get it from {@code qeetId.applications()}. Safe for concurrent use.
 */
public final class Applications {
  private final Transport http;
  private final OAuthResources resources;

  /**
   * Used by the client; get this from {@code qeetId.applications()}.
   *
   * @param http the client's transport
   */
  public Applications(Transport http) {
    this.http = http;
    this.resources = new OAuthResources(http);
  }

  /**
   * The organisation's registered RFC 8707 resources.
   *
   * @return the resources
   */
  public OAuthResources resources() {
    return resources;
  }

  /**
   * Registers an application. A confidential one's secret is in the result, once.
   *
   * @param tenantId the organisation's id
   * @param params the application
   * @return the application and, for a confidential one, its secret
   */
  public ApplicationCreated create(String tenantId, ApplicationCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Registers an application, with per-call settings. See {@link #create(String,
   * ApplicationCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the application
   * @param options per-call settings
   * @return the application and, for a confidential one, its secret
   */
  public ApplicationCreated create(
      String tenantId, ApplicationCreateParams params, RequestOptions options) {
    Map<String, @Nullable Object> body = new LinkedHashMap<>(params.toMap());
    body.put("tenant_id", tenantId);
    return http.send(
        "POST",
        Paths.of("/v1/tenants/{tenantId}/oidc/clients", tenantId),
        body,
        ApplicationCreated.class,
        options);
  }

  /**
   * Fetches an application.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @return the application
   */
  public Application get(String tenantId, String applicationId) {
    return get(tenantId, applicationId, RequestOptions.none());
  }

  /**
   * Fetches an application. With per-call settings: see {@link #get(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @param options per-call settings
   * @return the application
   */
  public Application get(String tenantId, String applicationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/tenants/{tenantId}/oidc/clients/{applicationId}", tenantId, applicationId)),
        options,
        Application.class);
  }

  /**
   * Lists the applications.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<Application> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the applications. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Application> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/oidc/clients", tenantId)),
        Application.class,
        options);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @param params the parameters
   * @return the application
   */
  public Application update(String tenantId, String applicationId, ApplicationUpdateParams params) {
    return update(tenantId, applicationId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * ApplicationUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @param params the parameters
   * @param options per-call settings
   * @return the application
   */
  public Application update(
      String tenantId,
      String applicationId,
      ApplicationUpdateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/tenants/{tenantId}/oidc/clients/{applicationId}", tenantId, applicationId))
            .json(params),
        options,
        Application.class);
  }

  /**
   * Deletes the application; its tokens and grants stop being honoured.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   */
  public void delete(String tenantId, String applicationId) {
    delete(tenantId, applicationId, RequestOptions.none());
  }

  /**
   * Deletes the application; its tokens and grants stop being honoured. With per-call settings: see
   * {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String applicationId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/oidc/clients/{applicationId}", tenantId, applicationId)),
        options);
  }

  /**
   * Issues a new secret for a confidential application, shown once. The old one stops working at
   * once.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @return the application secret
   */
  public ApplicationSecret rotateSecret(String tenantId, String applicationId) {
    return rotateSecret(tenantId, applicationId, RequestOptions.none());
  }

  /**
   * Issues a new secret for a confidential application, shown once. With per-call settings: see
   * {@link #rotateSecret(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @param options per-call settings
   * @return the application secret
   */
  public ApplicationSecret rotateSecret(
      String tenantId, String applicationId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of(
                "/v1/tenants/{tenantId}/oidc/clients/{applicationId}/rotate-secret",
                tenantId,
                applicationId)),
        options,
        ApplicationSecret.class);
  }

  /**
   * The application's wrong-secret lockout and its last 20 failed authentications.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @return the application auth failures
   */
  public ApplicationAuthFailures getAuthFailures(String tenantId, String applicationId) {
    return getAuthFailures(tenantId, applicationId, RequestOptions.none());
  }

  /**
   * The application's wrong-secret lockout and its last 20 failed authentications. With per-call
   * settings: see {@link #getAuthFailures(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @param options per-call settings
   * @return the application auth failures
   */
  public ApplicationAuthFailures getAuthFailures(
      String tenantId, String applicationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/tenants/{tenantId}/oidc/clients/{applicationId}/auth-failures",
                tenantId,
                applicationId)),
        options,
        ApplicationAuthFailures.class);
  }

  /**
   * Applications holding a machine grant that nobody has reviewed.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<ShadowAiApplication> listShadowAi(String tenantId) {
    return listShadowAi(tenantId, RequestOptions.none());
  }

  /**
   * Applications holding a machine grant that nobody has reviewed. With per-call settings: see
   * {@link #listShadowAi(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<ShadowAiApplication> listShadowAi(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/oidc/clients/shadow-ai", tenantId)),
        ShadowAiApplication.class,
        options);
  }

  /**
   * Marks the application reviewed: it leaves the shadow-AI list until its grant types change.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   */
  public void markReviewed(String tenantId, String applicationId) {
    markReviewed(tenantId, applicationId, RequestOptions.none());
  }

  /**
   * Marks the application reviewed: it leaves the shadow-AI list until its grant types change. With
   * per-call settings: see {@link #markReviewed(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param applicationId the application's id
   * @param options per-call settings
   */
  public void markReviewed(String tenantId, String applicationId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/tenants/{tenantId}/oidc/clients/{applicationId}/review",
                tenantId,
                applicationId)),
        options);
  }
}
