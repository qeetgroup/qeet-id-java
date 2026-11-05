package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * The organisation's address rules, and whether they are enforced.
 *
 * @param enabled whether the rules are enforced at sign-in
 * @param data the rules
 */
public record IpRuleList(boolean enabled, @JsonProperty("items") List<IpRule> data) {}
