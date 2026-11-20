package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organization's security policy. Which addresses may sign in is {@code ipRules()}.
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
   * @param organizationId the organization's id
   * @return the security policy
   */
  public SecurityPolicy get(String organizationId) {
    return get(organizationId, RequestOptions.none());
  }

  /**
   * The policy, or the platform defaults when none is saved. With per-call settings: see {@link
   * #get(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the security policy
   */
  public SecurityPolicy get(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/policy", organizationId)),
        options,
        SecurityPolicy.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the security policy
   */
  public SecurityPolicy update(String organizationId, SecurityPolicyUpdateParams params) {
    return update(organizationId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String,
   * SecurityPolicyUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the security policy
   */
  public SecurityPolicy update(
      String organizationId, SecurityPolicyUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH", Paths.of("/v1/organizations/{organizationId}/policy", organizationId))
            .json(params),
        options,
        SecurityPolicy.class);
  }

  /**
   * Replaces the whole policy.
   *
   * @param organizationId the organization's id
   * @param policy the policy
   * @return the security policy
   */
  public SecurityPolicy replace(String organizationId, SecurityPolicyUpdateParams policy) {
    return replace(organizationId, policy, RequestOptions.none());
  }

  /**
   * Replaces the whole policy. With per-call settings: see {@link #replace(String,
   * SecurityPolicyUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param policy the policy
   * @param options per-call settings
   * @return the security policy
   */
  public SecurityPolicy replace(
      String organizationId, SecurityPolicyUpdateParams policy, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/organizations/{organizationId}/policy", organizationId))
            .json(policy),
        options,
        SecurityPolicy.class);
  }
}
