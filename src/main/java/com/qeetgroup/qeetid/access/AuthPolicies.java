package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organization's sign-in policy: methods, password rules and lockout.
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
   * @param organizationId the organization's id
   * @return the auth policy
   */
  public AuthPolicy get(String organizationId) {
    return get(organizationId, RequestOptions.none());
  }

  /**
   * Fetches an auth policie. With per-call settings: see {@link #get(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the auth policy
   */
  public AuthPolicy get(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/auth-policy", organizationId)),
        options,
        AuthPolicy.class);
  }

  /**
   * Replaces the policy. Read it first and send it back whole.
   *
   * @param organizationId the organization's id
   * @param policy the policy
   * @return the auth policy
   */
  public AuthPolicy update(String organizationId, AuthPolicyUpdateParams policy) {
    return update(organizationId, policy, RequestOptions.none());
  }

  /**
   * Replaces the policy. With per-call settings: see {@link #update(String,
   * AuthPolicyUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param policy the policy
   * @param options per-call settings
   * @return the auth policy
   */
  public AuthPolicy update(
      String organizationId, AuthPolicyUpdateParams policy, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT", Paths.of("/v1/organizations/{organizationId}/auth-policy", organizationId))
            .json(policy),
        options,
        AuthPolicy.class);
  }

  /**
   * How the organization's people sign in: who would be locked out by turning a method off.
   *
   * @param organizationId the organization's id
   * @return the auth policy usage
   */
  public AuthPolicyUsage getUsage(String organizationId) {
    return getUsage(organizationId, RequestOptions.none());
  }

  /**
   * How the organization's people sign in: who would be locked out by turning a method off. With
   * per-call settings: see {@link #getUsage(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the auth policy usage
   */
  public AuthPolicyUsage getUsage(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/auth-policy/usage", organizationId)),
        options,
        AuthPolicyUsage.class);
  }
}
