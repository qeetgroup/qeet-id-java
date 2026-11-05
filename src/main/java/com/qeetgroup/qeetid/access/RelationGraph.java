package com.qeetgroup.qeetid.access;

import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Every identity reachable from an object and relation.
 *
 * @param nodes the nodes
 * @param edges the edges
 */
public record RelationGraph(List<Node> nodes, List<Edge> edges) {

  /**
   * Node.
   *
   * @param id its id
   * @param type its type
   * @param label the label
   * @param name its name
   */
  public record Node(String id, String type, String label, @Nullable String name) {}

  /**
   * Edge.
   *
   * @param from the from
   * @param to the to
   * @param relation the relation
   */
  public record Edge(String from, String to, String relation) {}
}
