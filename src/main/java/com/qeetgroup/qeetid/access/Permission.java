package com.qeetgroup.qeetid.access;

import org.jspecify.annotations.Nullable;

/**
 * A permission key, such as {@code user.read}, from the platform's fixed catalogue.
 *
 * @param id its id
 * @param key the key
 * @param description its description
 */
public record Permission(String id, String key, @Nullable String description) {}
