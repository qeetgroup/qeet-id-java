package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's address rules: which networks may sign in.
 *
 * <p>Get it from {@code qeetId.ipRules()}. Safe for concurrent use.
 */
public final class IpRules {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.ipRules()}.
   *
   * @param http the client's transport
   */
  public IpRules(Transport http) {
    this.http = http;
  }

  /**
   * The organisation's address rules, and whether they are enforced.
   *
   * @param tenantId the organisation's id
   * @return the rules
   */
  public IpRuleList list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * The organisation's address rules, with per-call settings. See {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the rules
   */
  public IpRuleList list(String tenantId, RequestOptions options) {
    return http.get(
        Paths.of("/v1/tenants/{tenantId}/ip-rules", tenantId), null, IpRuleList.class, options);
  }

  /**
   * Creates an IP rule.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the IP rule
   */
  public IpRule create(String tenantId, IpRuleCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates an IP rule. With per-call settings: see {@link #create(String, IpRuleCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the IP rule
   */
  public IpRule create(String tenantId, IpRuleCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/ip-rules", tenantId)).json(params),
        options,
        IpRule.class);
  }

  /**
   * Deletes an IP rule.
   *
   * @param tenantId the organisation's id
   * @param ruleId the rule's id
   */
  public void delete(String tenantId, String ruleId) {
    delete(tenantId, ruleId, RequestOptions.none());
  }

  /**
   * Deletes an IP rule. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param ruleId the rule's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String ruleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/ip-rules/{ruleId}", tenantId, ruleId)),
        options);
  }

  /**
   * What the rules make of {@code ip}: the same decision a sign-in gets. Changes nothing.
   *
   * @param tenantId the organisation's id
   * @param ip the ip
   * @return the IP rule check
   */
  public IpRuleCheck check(String tenantId, String ip) {
    return check(tenantId, ip, RequestOptions.none());
  }

  /**
   * What the rules make of {@code ip}: the same decision a sign-in gets. With per-call settings:
   * see {@link #check(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param ip the ip
   * @param options per-call settings
   * @return the IP rule check
   */
  public IpRuleCheck check(String tenantId, String ip, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/ip-rules/check", tenantId))
            .json(Bodies.of("ip", ip))
            .retrySafe(),
        options,
        IpRuleCheck.class);
  }

  /**
   * Turns enforcement of the rules on or off.
   *
   * @param tenantId the organisation's id
   * @param enabled the enabled
   * @return whether the rules are now enforced
   */
  public boolean setEnforcement(String tenantId, boolean enabled) {
    return setEnforcement(tenantId, enabled, RequestOptions.none());
  }

  /**
   * Turns enforcement of the rules on or off. With per-call settings: see {@link
   * #setEnforcement(String, boolean)}.
   *
   * @param tenantId the organisation's id
   * @param enabled the enabled
   * @param options per-call settings
   * @return whether the rules are now enforced
   */
  public boolean setEnforcement(String tenantId, boolean enabled, RequestOptions options) {
    return http.tree(
            ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/ip-rules/config", tenantId))
                .json(Bodies.of("enabled", enabled)),
            options)
        .path("enabled")
        .asBoolean();
  }
}
