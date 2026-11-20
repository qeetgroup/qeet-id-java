package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organization's address rules: which networks may sign in.
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
   * The organization's address rules, and whether they are enforced.
   *
   * @param organizationId the organization's id
   * @return the rules
   */
  public IpRuleList list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * The organization's address rules, with per-call settings. See {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the rules
   */
  public IpRuleList list(String organizationId, RequestOptions options) {
    return http.get(
        Paths.of("/v1/organizations/{organizationId}/ip-rules", organizationId),
        null,
        IpRuleList.class,
        options);
  }

  /**
   * Creates an IP rule.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the IP rule
   */
  public IpRule create(String organizationId, IpRuleCreateParams params) {
    return create(organizationId, params, RequestOptions.none());
  }

  /**
   * Creates an IP rule. With per-call settings: see {@link #create(String, IpRuleCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the IP rule
   */
  public IpRule create(String organizationId, IpRuleCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/ip-rules", organizationId))
            .json(params),
        options,
        IpRule.class);
  }

  /**
   * Deletes an IP rule.
   *
   * @param organizationId the organization's id
   * @param ruleId the rule's id
   */
  public void delete(String organizationId, String ruleId) {
    delete(organizationId, ruleId, RequestOptions.none());
  }

  /**
   * Deletes an IP rule. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param organizationId the organization's id
   * @param ruleId the rule's id
   * @param options per-call settings
   */
  public void delete(String organizationId, String ruleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/ip-rules/{ruleId}", organizationId, ruleId)),
        options);
  }

  /**
   * What the rules make of {@code ip}: the same decision a sign-in gets. Changes nothing.
   *
   * @param organizationId the organization's id
   * @param ip the ip
   * @return the IP rule check
   */
  public IpRuleCheck check(String organizationId, String ip) {
    return check(organizationId, ip, RequestOptions.none());
  }

  /**
   * What the rules make of {@code ip}: the same decision a sign-in gets. With per-call settings:
   * see {@link #check(String, String)}.
   *
   * @param organizationId the organization's id
   * @param ip the ip
   * @param options per-call settings
   * @return the IP rule check
   */
  public IpRuleCheck check(String organizationId, String ip, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/ip-rules/check", organizationId))
            .json(Bodies.of("ip", ip))
            .retrySafe(),
        options,
        IpRuleCheck.class);
  }

  /**
   * Turns enforcement of the rules on or off.
   *
   * @param organizationId the organization's id
   * @param enabled the enabled
   * @return whether the rules are now enforced
   */
  public boolean setEnforcement(String organizationId, boolean enabled) {
    return setEnforcement(organizationId, enabled, RequestOptions.none());
  }

  /**
   * Turns enforcement of the rules on or off. With per-call settings: see {@link
   * #setEnforcement(String, boolean)}.
   *
   * @param organizationId the organization's id
   * @param enabled the enabled
   * @param options per-call settings
   * @return whether the rules are now enforced
   */
  public boolean setEnforcement(String organizationId, boolean enabled, RequestOptions options) {
    return http.tree(
            ApiRequest.of(
                    "PUT",
                    Paths.of("/v1/organizations/{organizationId}/ip-rules/config", organizationId))
                .json(Bodies.of("enabled", enabled)),
            options)
        .path("enabled")
        .asBoolean();
  }
}
