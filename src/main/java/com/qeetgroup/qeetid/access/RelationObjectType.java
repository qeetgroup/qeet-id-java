package com.qeetgroup.qeetid.access;

/**
 * An object type the relationship tuples name, with counts.
 *
 * @param type its type
 * @param objects the objects
 * @param tuples the tuples
 */
public record RelationObjectType(String type, int objects, int tuples) {}
