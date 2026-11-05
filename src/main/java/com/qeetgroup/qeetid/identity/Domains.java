package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's domains: claim, verify by DNS, and route sign-in.
 *
 * <p>Get it from {@code qeetId.domains()}. Safe for concurrent use.
 */
public final class Domains {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.domains()}.
   *
   * @param http the client's transport
   */
  public Domains(Transport http) {
    this.http = http;
  }

  /**
   * Claims a domain and returns the TXT record to publish.
   *
   * @param tenantId the organisation's id
   * @param domain the domain
   * @return the domain
   */
  public Domain create(String tenantId, String domain) {
    return create(tenantId, domain, RequestOptions.none());
  }

  /**
   * Claims a domain and returns the TXT record to publish. With per-call settings: see {@link
   * #create(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param domain the domain
   * @param options per-call settings
   * @return the domain
   */
  public Domain create(String tenantId, String domain, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/domains", tenantId))
            .json(Bodies.of("domain", domain)),
        options,
        Domain.class);
  }

  /**
   * Lists the domains.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<Domain> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the domains. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Domain> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/domains", tenantId)),
        Domain.class,
        options);
  }

  /**
   * Looks the TXT record up now and, on a match, marks the domain verified.
   *
   * @param tenantId the organisation's id
   * @param domainId the domain's id
   * @return the domain
   */
  public Domain verify(String tenantId, String domainId) {
    return verify(tenantId, domainId, RequestOptions.none());
  }

  /**
   * Looks the TXT record up now and, on a match, marks the domain verified. With per-call settings:
   * see {@link #verify(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param domainId the domain's id
   * @param options per-call settings
   * @return the domain
   */
  public Domain verify(String tenantId, String domainId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of("/v1/tenants/{tenantId}/domains/{domainId}/verify", tenantId, domainId)),
        options,
        Domain.class);
  }

  /**
   * Turns SSO routing and just-in-time provisioning on or off.
   *
   * @param tenantId the organisation's id
   * @param domainId the domain's id
   * @param params the parameters
   * @return the domain
   */
  public Domain update(String tenantId, String domainId, DomainUpdateParams params) {
    return update(tenantId, domainId, params, RequestOptions.none());
  }

  /**
   * Turns SSO routing and just-in-time provisioning on or off. With per-call settings: see {@link
   * #update(String, String, DomainUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param domainId the domain's id
   * @param params the parameters
   * @param options per-call settings
   * @return the domain
   */
  public Domain update(
      String tenantId, String domainId, DomainUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH", Paths.of("/v1/tenants/{tenantId}/domains/{domainId}", tenantId, domainId))
            .json(params),
        options,
        Domain.class);
  }

  /**
   * Deletes a domain.
   *
   * @param tenantId the organisation's id
   * @param domainId the domain's id
   */
  public void delete(String tenantId, String domainId) {
    delete(tenantId, domainId, RequestOptions.none());
  }

  /**
   * Deletes a domain. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param domainId the domain's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String domainId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/domains/{domainId}", tenantId, domainId)),
        options);
  }
}
