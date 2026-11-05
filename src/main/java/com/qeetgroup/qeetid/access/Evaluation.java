package com.qeetgroup.qeetid.access;

import java.util.Map;

/**
 * An AuthZEN decision. {@code context} always carries {@code decided_by}.
 *
 * @param decision the decision
 * @param context the context
 */
public record Evaluation(boolean decision, Map<String, Object> context) {}
