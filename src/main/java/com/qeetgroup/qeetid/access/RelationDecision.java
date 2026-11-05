package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A relationship check's answer. {@code path} and {@code trace} come with {@code explain}.
 *
 * @param allowed whether allowed
 * @param decidedBy the decided by
 * @param trace the trace
 * @param path the path
 */
public record RelationDecision(
    boolean allowed,
    @JsonProperty("decided_by") @Nullable DecidedBy decidedBy,
    List<String> trace,
    List<Path> path) {

  /**
   * Path.
   *
   * @param object the object
   * @param relation the relation
   * @param subject the subject
   * @param depth the depth
   */
  public record Path(String object, String relation, String subject, int depth) {}
}
