package com.qeetgroup.qeetid.access;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * An AuthZEN action: a name, such as a permission key or a relation, with optional attributes.
 *
 * @param name its name
 * @param properties the properties
 */
public record AuthzenAction(@Nullable String name, Map<String, Object> properties) {}
