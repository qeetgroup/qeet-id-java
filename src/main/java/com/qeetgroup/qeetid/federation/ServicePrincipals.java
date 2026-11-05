package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages service principals: machine identities for machine-to-machine access.
 *
 * <p>Get it from {@code qeetId.servicePrincipals()}. Safe for concurrent use.
 */
public final class ServicePrincipals {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.servicePrincipals()}.
   *
   * @param http the client's transport
   */
  public ServicePrincipals(Transport http) {
    this.http = http;
  }

  /**
   * Creates a service principal. The API makes it idempotent: an {@code Idempotency-Key} is sent
   * with it, generated per call unless you give one in {@link RequestOptions}, so a retry never
   * acts twice.
   *
   * @param params the parameters
   * @return the service principal created
   */
  public ServicePrincipalCreated create(ServicePrincipalCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /**
   * Creates a service principal. With per-call settings: see {@link
   * #create(ServicePrincipalCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the service principal created
   */
  public ServicePrincipalCreated create(
      ServicePrincipalCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/service-principals").json(params).idempotent(),
        options,
        ServicePrincipalCreated.class);
  }

  /**
   * Lists the service principals.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<ServicePrincipal> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the service principals. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<ServicePrincipal> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/service-principals", tenantId)),
        ServicePrincipal.class,
        options);
  }

  /**
   * Disables the principal: its secret gets no more tokens and the tokens it holds are refused at
   * once. Undo with {@code enable}.
   *
   * @param servicePrincipalId the service principal's id
   */
  public void disable(String servicePrincipalId) {
    disable(servicePrincipalId, RequestOptions.none());
  }

  /**
   * Disables the principal: its secret gets no more tokens and the tokens it holds are refused at
   * once. With per-call settings: see {@link #disable(String)}.
   *
   * @param servicePrincipalId the service principal's id
   * @param options per-call settings
   */
  public void disable(String servicePrincipalId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/service-principals/{servicePrincipalId}", servicePrincipalId)),
        options);
  }

  /**
   * Turns a disabled principal back on.
   *
   * @param tenantId the organisation's id
   * @param servicePrincipalId the service principal's id
   */
  public void enable(String tenantId, String servicePrincipalId) {
    enable(tenantId, servicePrincipalId, RequestOptions.none());
  }

  /**
   * Turns a disabled principal back on. With per-call settings: see {@link #enable(String,
   * String)}.
   *
   * @param tenantId the organisation's id
   * @param servicePrincipalId the service principal's id
   * @param options per-call settings
   */
  public void enable(String tenantId, String servicePrincipalId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/tenants/{tenantId}/service-principals/{servicePrincipalId}/enable",
                tenantId,
                servicePrincipalId)),
        options);
  }

  /**
   * Issues a new secret, shown once. The old one keeps getting tokens for {@code overlap_hours}
   * (default 24, at most 168; 0 stops it at once).
   *
   * @param tenantId the organisation's id
   * @param servicePrincipalId the service principal's id
   * @return the service principal secret
   */
  public ServicePrincipalSecret rotateSecret(String tenantId, String servicePrincipalId) {
    return rotateSecret(
        tenantId,
        servicePrincipalId,
        ServicePrincipalRotateSecretParams.builder().build(),
        RequestOptions.none());
  }

  /**
   * Issues a new secret, shown once. The old one keeps getting tokens for {@code overlap_hours}
   * (default 24, at most 168; 0 stops it at once).
   *
   * @param tenantId the organisation's id
   * @param servicePrincipalId the service principal's id
   * @param params the parameters
   * @return the service principal secret
   */
  public ServicePrincipalSecret rotateSecret(
      String tenantId, String servicePrincipalId, ServicePrincipalRotateSecretParams params) {
    return rotateSecret(tenantId, servicePrincipalId, params, RequestOptions.none());
  }

  /**
   * Issues a new secret, shown once. With per-call settings: see {@link #rotateSecret(String,
   * String, ServicePrincipalRotateSecretParams)}.
   *
   * @param tenantId the organisation's id
   * @param servicePrincipalId the service principal's id
   * @param params the parameters
   * @param options per-call settings
   * @return the service principal secret
   */
  public ServicePrincipalSecret rotateSecret(
      String tenantId,
      String servicePrincipalId,
      ServicePrincipalRotateSecretParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/service-principals/{servicePrincipalId}/rotate-secret",
                    tenantId,
                    servicePrincipalId))
            .json(params),
        options,
        ServicePrincipalSecret.class);
  }
}
