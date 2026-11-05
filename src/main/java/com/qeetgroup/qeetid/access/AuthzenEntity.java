package com.qeetgroup.qeetid.access;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * An AuthZEN subject, resource or action: a type and id (or a name) with optional attributes.
 *
 * @param type its type
 * @param id its id
 * @param properties the properties
 */
public record AuthzenEntity(
    @Nullable String type, @Nullable String id, Map<String, Object> properties) {}
