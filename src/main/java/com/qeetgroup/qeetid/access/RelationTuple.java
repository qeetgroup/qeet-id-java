package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A relationship tuple: {@code subject} holds {@code relation} on {@code object}. {@code object} is
 * {@code type:id}; {@code subject} is {@code type:id}, or a userset {@code type:id#relation} (such
 * as {@code group:eng#member}).
 *
 * @param id its id
 * @param object the object
 * @param relation the relation
 * @param subject the subject
 * @param subjectName A readable name for the subject, when known.
 */
public record RelationTuple(
    String id,
    String object,
    String relation,
    String subject,
    @JsonProperty("subject_name") @Nullable String subjectName) {}
