package com.qeetgroup.qeetid.internal;

import java.nio.charset.StandardCharsets;

/** Builds request paths, percent-encoding each identifier as one path segment. */
public final class Paths {
  private static final char[] HEX = "0123456789ABCDEF".toCharArray();

  private Paths() {}

  /**
   * Fills {@code template}'s {@code {name}} placeholders with {@code values}, in order, each
   * percent-encoded as one path segment. Refuses a blank value and the dot segments {@code .} and
   * {@code ..}, which a URL parser would resolve into another endpoint.
   *
   * <pre>{@code Paths.of("/v1/users/{userId}/sessions", "u/1") // "/v1/users/u%2F1/sessions"}</pre>
   */
  public static String of(String template, String... values) {
    StringBuilder out = new StringBuilder(template.length() + 40);
    int next = 0;
    int i = 0;
    while (i < template.length()) {
      char c = template.charAt(i);
      if (c != '{') {
        out.append(c);
        i++;
        continue;
      }
      int close = template.indexOf('}', i);
      if (close < 0) {
        throw new IllegalStateException("Malformed path template " + template);
      }
      String name = template.substring(i + 1, close);
      if (next >= values.length) {
        throw new IllegalStateException("No value for " + name + " in " + template);
      }
      String value = values[next++];
      if (value == null || value.isBlank() || value.equals(".") || value.equals("..")) {
        throw new IllegalArgumentException(name + " must be a non-blank identifier.");
      }
      encodeSegment(value, out);
      i = close + 1;
    }
    if (next != values.length) {
      throw new IllegalStateException("Too many values for " + template);
    }
    return out.toString();
  }

  private static void encodeSegment(String value, StringBuilder out) {
    for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
      int c = b & 0xff;
      if ((c >= 'a' && c <= 'z')
          || (c >= 'A' && c <= 'Z')
          || (c >= '0' && c <= '9')
          || c == '-'
          || c == '.'
          || c == '_'
          || c == '~') {
        out.append((char) c);
      } else {
        out.append('%').append(HEX[c >> 4]).append(HEX[c & 0xf]);
      }
    }
  }
}
