package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * One recorded access decision.
 *
 * @param id its id
 * @param decidedAt the decided time
 * @param source One of {@code "check"}, {@code "relation_check"}, {@code "authzen"}, {@code
 *     "abac_evaluate"}, {@code "abac_draft"}; others may be added
 * @param callerType the caller type
 * @param callerId the caller's id
 * @param callerClient the caller client
 * @param subject the subject
 * @param resource the resource
 * @param action the action
 * @param allowed whether allowed
 * @param model the model
 * @param rule the rule
 * @param reasonCode the reason code
 */
public record DecisionLogEntry(
    String id,
    @JsonProperty("decided_at") Instant decidedAt,
    String source,
    @JsonProperty("caller_type") @Nullable String callerType,
    @JsonProperty("caller_id") @Nullable String callerId,
    @JsonProperty("caller_client") @Nullable String callerClient,
    String subject,
    String resource,
    String action,
    boolean allowed,
    String model,
    @Nullable String rule,
    @JsonProperty("reason_code") @Nullable String reasonCode) {}
