package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * What the rules make of an address.
 *
 * @param enabled whether it is on
 * @param allowed whether allowed
 * @param reason the reason
 * @param reasonCode One of {@code "ip.allowed"}, {@code "ip.deny_rule"}, {@code
 *     "ip.not_in_allow_rules"}, {@code "ip.address_unknown"}, {@code "ip.enforcement_disabled"};
 *     others may be added
 * @param reasonParams the reason params
 * @param rules What the rules alone decide, whether or not they are enforced.
 */
public record IpRuleCheck(
    boolean enabled,
    boolean allowed,
    String reason,
    @JsonProperty("reason_code") String reasonCode,
    @JsonProperty("reason_params") Map<String, Object> reasonParams,
    @Nullable Rules rules) {

  /**
   * What the rules alone decide, whether or not they are enforced.
   *
   * @param allowed whether allowed
   * @param reason the reason
   * @param reasonCode One of {@code "ip.allowed"}, {@code "ip.deny_rule"}, {@code
   *     "ip.not_in_allow_rules"}, {@code "ip.address_unknown"}, {@code "ip.enforcement_disabled"};
   *     others may be added
   * @param reasonParams the reason params
   */
  public record Rules(
      boolean allowed,
      String reason,
      @JsonProperty("reason_code") String reasonCode,
      @JsonProperty("reason_params") Map<String, Object> reasonParams) {}
}
