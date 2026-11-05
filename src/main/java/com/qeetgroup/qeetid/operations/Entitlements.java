package com.qeetgroup.qeetid.operations;

import java.util.Map;

/**
 * The organisation's plan: its features and limits.
 *
 * @param plan One of {@code "free"}, {@code "starter"}, {@code "pro"}, {@code "enterprise"}; others
 *     may be added
 * @param features the features
 * @param limits the limits
 */
public record Entitlements(
    String plan, Map<String, Boolean> features, Map<String, Integer> limits) {}
