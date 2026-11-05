package com.qeetgroup.qeetid.developer;

/**
 * An event a webhook subscription can name.
 *
 * @param name its name
 * @param description its description
 */
public record WebhookEventType(String name, String description) {}
