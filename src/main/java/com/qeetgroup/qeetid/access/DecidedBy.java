package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Which model decided an authorization question, and by which rule.
 *
 * @param model One of {@code "tenant"}, {@code "abac"}, {@code "rbac"}, {@code "rebac"}, {@code
 *     "none"}; others may be added
 * @param rule the rule
 * @param reasonCode the reason code
 * @param reasonParams the reason params
 * @param policyId The deciding ABAC policy, named only to a caller allowed to read policies.
 */
public record DecidedBy(
    String model,
    @Nullable String rule,
    @JsonProperty("reason_code") @Nullable String reasonCode,
    @JsonProperty("reason_params") Map<String, Object> reasonParams,
    @JsonProperty("policy_id") @Nullable String policyId) {}
