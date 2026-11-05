package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's security policy. Which addresses may sign in is {@code ipRules()}.
 *
 * <p>Get it from {@code qeetId.securityPolicies()}. Safe for concurrent use.
 */
public final class SecurityPolicies {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.securityPolicies()}.
   *
   * @param http the client's transport
   */
  public SecurityPolicies(Transport http) {
    this.http = http;
  }

  /**
   * The policy, or the platform defaults when none is saved.
   *
   * @param tenantId the organisation's id
   * @return the security policy
   */
  public SecurityPolicy get(String tenantId) {
    return get(tenantId, RequestOptions.none());
  }

  /**
   * The policy, or the platform defaults when none is saved. With per-call settings: see {@link
   * #get(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the security policy
   */
  public SecurityPolicy get(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/policy", tenantId)),
        options,
        SecurityPolicy.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the security policy
   */
  public SecurityPolicy update(String tenantId, SecurityPolicyUpdateParams params) {
    return update(tenantId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String,
   * SecurityPolicyUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the security policy
   */
  public SecurityPolicy update(
      String tenantId, SecurityPolicyUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PATCH", Paths.of("/v1/tenants/{tenantId}/policy", tenantId)).json(params),
        options,
        SecurityPolicy.class);
  }

  /**
   * Replaces the whole policy.
   *
   * @param tenantId the organisation's id
   * @param policy the policy
   * @return the security policy
   */
  public SecurityPolicy replace(String tenantId, SecurityPolicyUpdateParams policy) {
    return replace(tenantId, policy, RequestOptions.none());
  }

  /**
   * Replaces the whole policy. With per-call settings: see {@link #replace(String,
   * SecurityPolicyUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param policy the policy
   * @param options per-call settings
   * @return the security policy
   */
  public SecurityPolicy replace(
      String tenantId, SecurityPolicyUpdateParams policy, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/policy", tenantId)).json(policy),
        options,
        SecurityPolicy.class);
  }
}
