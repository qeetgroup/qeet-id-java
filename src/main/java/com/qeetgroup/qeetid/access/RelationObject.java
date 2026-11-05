package com.qeetgroup.qeetid.access;

/**
 * An object the relationship tuples name, with how many tuples name it.
 *
 * @param object the object
 * @param id its id
 * @param tuples the tuples
 */
public record RelationObject(String object, String id, int tuples) {}
