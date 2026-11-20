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
   * The organization's registered RFC 8707 resources.
   *
   * @return the resources
   */
  public OAuthResources resources() {
    return resources;
  }

  /**
   * Registers an application. A confidential one's secret is in the result, once.
   *
   * @param organizationId the organization's id
   * @param params the application
   * @return the application and, for a confidential one, its secret
   */
  public ApplicationCreated create(String organizationId, ApplicationCreateParams params) {
    return create(organizationId, params, RequestOptions.none());
  }

  /**
   * Registers an application, with per-call settings. See {@link #create(String,
   * ApplicationCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the application
   * @param options per-call settings
   * @return the application and, for a confidential one, its secret
   */
  public ApplicationCreated create(
      String organizationId, ApplicationCreateParams params, RequestOptions options) {
    Map<String, @Nullable Object> body = new LinkedHashMap<>(params.toMap());
    body.put("organization_id", organizationId);
    return http.send(
        "POST",
        Paths.of("/v1/organizations/{organizationId}/oidc/clients", organizationId),
        body,
        ApplicationCreated.class,
        options);
  }

  /**
   * Fetches an application.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @return the application
   */
  public Application get(String organizationId, String applicationId) {
    return get(organizationId, applicationId, RequestOptions.none());
  }

  /**
   * Fetches an application. With per-call settings: see {@link #get(String, String)}.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @param options per-call settings
   * @return the application
   */
  public Application get(String organizationId, String applicationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/organizations/{organizationId}/oidc/clients/{applicationId}",
                organizationId,
                applicationId)),
        options,
        Application.class);
  }

  /**
   * Lists the applications.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<Application> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the applications. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Application> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/oidc/clients", organizationId)),
        Application.class,
        options);
  }

  /**
   * Changes only the fields given.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @param params the parameters
   * @return the application
   */
  public Application update(
      String organizationId, String applicationId, ApplicationUpdateParams params) {
    return update(organizationId, applicationId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * ApplicationUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @param params the parameters
   * @param options per-call settings
   * @return the application
   */
  public Application update(
      String organizationId,
      String applicationId,
      ApplicationUpdateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/organizations/{organizationId}/oidc/clients/{applicationId}",
                    organizationId,
                    applicationId))
            .json(params),
        options,
        Application.class);
  }

  /**
   * Deletes the application; its tokens and grants stop being honoured.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   */
  public void delete(String organizationId, String applicationId) {
    delete(organizationId, applicationId, RequestOptions.none());
  }

  /**
   * Deletes the application; its tokens and grants stop being honoured. With per-call settings: see
   * {@link #delete(String, String)}.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @param options per-call settings
   */
  public void delete(String organizationId, String applicationId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/oidc/clients/{applicationId}",
                organizationId,
                applicationId)),
        options);
  }

  /**
   * Issues a new secret for a confidential application, shown once. The old one stops working at
   * once.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @return the application secret
   */
  public ApplicationSecret rotateSecret(String organizationId, String applicationId) {
    return rotateSecret(organizationId, applicationId, RequestOptions.none());
  }

  /**
   * Issues a new secret for a confidential application, shown once. With per-call settings: see
   * {@link #rotateSecret(String, String)}.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @param options per-call settings
   * @return the application secret
   */
  public ApplicationSecret rotateSecret(
      String organizationId, String applicationId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of(
                "/v1/organizations/{organizationId}/oidc/clients/{applicationId}/rotate-secret",
                organizationId,
                applicationId)),
        options,
        ApplicationSecret.class);
  }

  /**
   * The application's wrong-secret lockout and its last 20 failed authentications.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @return the application auth failures
   */
  public ApplicationAuthFailures getAuthFailures(String organizationId, String applicationId) {
    return getAuthFailures(organizationId, applicationId, RequestOptions.none());
  }

  /**
   * The application's wrong-secret lockout and its last 20 failed authentications. With per-call
   * settings: see {@link #getAuthFailures(String, String)}.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @param options per-call settings
   * @return the application auth failures
   */
  public ApplicationAuthFailures getAuthFailures(
      String organizationId, String applicationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/organizations/{organizationId}/oidc/clients/{applicationId}/auth-failures",
                organizationId,
                applicationId)),
        options,
        ApplicationAuthFailures.class);
  }

  /**
   * Applications holding a machine grant that nobody has reviewed.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<ShadowAiApplication> listShadowAi(String organizationId) {
    return listShadowAi(organizationId, RequestOptions.none());
  }

  /**
   * Applications holding a machine grant that nobody has reviewed. With per-call settings: see
   * {@link #listShadowAi(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<ShadowAiApplication> listShadowAi(
      String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/oidc/clients/shadow-ai", organizationId)),
        ShadowAiApplication.class,
        options);
  }

  /**
   * Marks the application reviewed: it leaves the shadow-AI list until its grant types change.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   */
  public void markReviewed(String organizationId, String applicationId) {
    markReviewed(organizationId, applicationId, RequestOptions.none());
  }

  /**
   * Marks the application reviewed: it leaves the shadow-AI list until its grant types change. With
   * per-call settings: see {@link #markReviewed(String, String)}.
   *
   * @param organizationId the organization's id
   * @param applicationId the application's id
   * @param options per-call settings
   */
  public void markReviewed(String organizationId, String applicationId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/organizations/{organizationId}/oidc/clients/{applicationId}/review",
                organizationId,
                applicationId)),
        options);
  }
}
