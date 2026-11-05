package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's sign-in policy: methods, password rules and lockout.
 *
 * <p>Get it from {@code qeetId.authPolicies()}. Safe for concurrent use.
 */
public final class AuthPolicies {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.authPolicies()}.
   *
   * @param http the client's transport
   */
  public AuthPolicies(Transport http) {
    this.http = http;
  }

  /**
   * Fetches an auth policie.
   *
   * @param tenantId the organisation's id
   * @return the auth policy
   */
  public AuthPolicy get(String tenantId) {
    return get(tenantId, RequestOptions.none());
  }

  /**
   * Fetches an auth policie. With per-call settings: see {@link #get(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the auth policy
   */
  public AuthPolicy get(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/auth-policy", tenantId)),
        options,
        AuthPolicy.class);
  }

  /**
   * Replaces the policy. Read it first and send it back whole.
   *
   * @param tenantId the organisation's id
   * @param policy the policy
   * @return the auth policy
   */
  public AuthPolicy update(String tenantId, AuthPolicyUpdateParams policy) {
    return update(tenantId, policy, RequestOptions.none());
  }

  /**
   * Replaces the policy. With per-call settings: see {@link #update(String,
   * AuthPolicyUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param policy the policy
   * @param options per-call settings
   * @return the auth policy
   */
  public AuthPolicy update(String tenantId, AuthPolicyUpdateParams policy, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/auth-policy", tenantId)).json(policy),
        options,
        AuthPolicy.class);
  }

  /**
   * How the organisation's people sign in: who would be locked out by turning a method off.
   *
   * @param tenantId the organisation's id
   * @return the auth policy usage
   */
  public AuthPolicyUsage getUsage(String tenantId) {
    return getUsage(tenantId, RequestOptions.none());
  }

  /**
   * How the organisation's people sign in: who would be locked out by turning a method off. With
   * per-call settings: see {@link #getUsage(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the auth policy usage
   */
  public AuthPolicyUsage getUsage(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/auth-policy/usage", tenantId)),
        options,
        AuthPolicyUsage.class);
  }
}
