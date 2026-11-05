package com.qeetgroup.qeetid.developer;

/**
 * A queued {@code test.ping} delivery.
 *
 * @param queued the queued
 * @param subscription the subscription
 */
public record WebhookTestResult(boolean queued, String subscription) {}
