package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Reads and manages the API key's organisation. Listing and creating organisations are a signed-in
 * person's actions and aren't open to API keys.
 *
 * <p>Get it from {@code qeetId.tenants()}. Safe for concurrent use.
 */
public final class Tenants {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.tenants()}.
   *
   * @param http the client's transport
   */
  public Tenants(Transport http) {
    this.http = http;
  }

  /**
   * Fetches a tenant.
   *
   * @param tenantId the organisation's id
   * @return the tenant
   */
  public Tenant get(String tenantId) {
    return get(tenantId, RequestOptions.none());
  }

  /**
   * Fetches a tenant. With per-call settings: see {@link #get(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the tenant
   */
  public Tenant get(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}", tenantId)), options, Tenant.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the tenant
   */
  public Tenant update(String tenantId, TenantUpdateParams params) {
    return update(tenantId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String,
   * TenantUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the tenant
   */
  public Tenant update(String tenantId, TenantUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PATCH", Paths.of("/v1/tenants/{tenantId}", tenantId)).json(params),
        options,
        Tenant.class);
  }

  /**
   * Schedules the organisation's deletion seven days from now. An owner may call it off with {@code
   * cancelDeletion}.
   *
   * @param tenantId the organisation's id
   * @return the tenant deletion
   */
  public TenantDeletion delete(String tenantId) {
    return delete(tenantId, RequestOptions.none());
  }

  /**
   * Schedules the organisation's deletion seven days from now. With per-call settings: see {@link
   * #delete(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the tenant deletion
   */
  public TenantDeletion delete(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.of("DELETE", Paths.of("/v1/tenants/{tenantId}", tenantId)),
        options,
        TenantDeletion.class);
  }

  /**
   * Calls off a scheduled deletion.
   *
   * @param tenantId the organisation's id
   * @return the tenant
   */
  public Tenant cancelDeletion(String tenantId) {
    return cancelDeletion(tenantId, RequestOptions.none());
  }

  /**
   * Calls off a scheduled deletion. With per-call settings: see {@link #cancelDeletion(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the tenant
   */
  public Tenant cancelDeletion(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/deletion/cancel", tenantId)),
        options,
        Tenant.class);
  }
}
