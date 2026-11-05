package com.qeetgroup.qeetid;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * A list the API returns whole, in one answer. {@code truncated} is true when the API capped it and
 * more items exist than {@code data} holds: narrow the request to see the rest.
 *
 * @param data the items
 * @param truncated whether the API left items out
 * @param <T> the item type
 */
public record ListResult<T>(List<T> data, boolean truncated) implements Iterable<T> {
  /** Copies the items. */
  public ListResult {
    data = List.copyOf(data);
  }

  @Override
  public Iterator<T> iterator() {
    return data.iterator();
  }

  /**
   * The items as a stream.
   *
   * @return the stream
   */
  public Stream<T> stream() {
    return data.stream();
  }
}
