package com.qeetgroup.qeetid.operations;

/**
 * A token bucket: {@code rate} tokens a second, bursting to {@code capacity}.
 *
 * @param rate the rate
 * @param capacity the capacity
 */
public record RateLimitBucket(double rate, int capacity) {}
