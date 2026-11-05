package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * A permission check's answer with its reasons.
 *
 * @param allowed whether allowed
 * @param decidedBy the decided by
 * @param paths On an allow: the grants behind it.
 * @param reason On a deny: why.
 * @param reasonCode One of {@code "authz.no_role_grant"}, {@code "authz.denied_by_policy"}, {@code
 *     "authz.attribute_absent"}, {@code "authz.condition_error"}; others may be added
 * @param reasonParams the reason params
 * @param trace the trace
 */
public record PermissionDecision(
    boolean allowed,
    @JsonProperty("decided_by") @Nullable DecidedBy decidedBy,
    List<GrantStep> paths,
    @Nullable String reason,
    @JsonProperty("reason_code") @Nullable String reasonCode,
    @JsonProperty("reason_params") Map<String, Object> reasonParams,
    List<String> trace) {}
