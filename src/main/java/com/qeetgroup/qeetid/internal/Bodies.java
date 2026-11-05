package com.qeetgroup.qeetid.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Small JSON bodies built from a resource method's arguments. */
public final class Bodies {
  private Bodies() {}

  /**
   * A body of name-value pairs, in order: {@code Bodies.of("password", password)}. Every value is
   * required.
   */
  public static Map<String, Object> of(Object... pairs) {
    if (pairs.length % 2 != 0) {
      throw new IllegalStateException("Bodies.of takes name-value pairs.");
    }
    Map<String, Object> body = new LinkedHashMap<>();
    for (int i = 0; i < pairs.length; i += 2) {
      String name = (String) pairs[i];
      body.put(name, Objects.requireNonNull(pairs[i + 1], name));
    }
    return body;
  }
}
