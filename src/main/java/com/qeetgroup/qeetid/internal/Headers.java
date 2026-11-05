package com.qeetgroup.qeetid.internal;

import java.util.Locale;
import java.util.Set;

/** Checks the headers a caller adds. */
public final class Headers {
  /** Headers the SDK or the HTTP client set themselves; a caller can't replace them. */
  private static final Set<String> RESERVED =
      Set.of(
          "authorization",
          "accept",
          "content-type",
          "content-length",
          "user-agent",
          "idempotency-key",
          "host",
          "connection",
          "expect",
          "upgrade");

  private Headers() {}

  /**
   * Refuses an empty or reserved header name and a line break in either part.
   *
   * @throws IllegalArgumentException when the header can't be sent
   */
  public static void check(String name, String value) {
    String trimmed = name.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("A header name must not be empty.");
    }
    if (RESERVED.contains(trimmed.toLowerCase(Locale.ROOT))) {
      throw new IllegalArgumentException("The SDK sets " + trimmed + " itself.");
    }
    if (hasLineBreak(name) || hasLineBreak(value)) {
      throw new IllegalArgumentException("Header " + trimmed + " contains a line break.");
    }
  }

  /**
   * Checks an idempotency key the way the server does: 1 to 255 printable ASCII characters, no
   * spaces.
   *
   * @throws IllegalArgumentException when the key would be refused
   */
  public static String checkIdempotencyKey(String key) {
    boolean valid = !key.isEmpty() && key.length() <= 255;
    for (int i = 0; valid && i < key.length(); i++) {
      char c = key.charAt(i);
      valid = c >= 0x21 && c <= 0x7e;
    }
    if (!valid) {
      throw new IllegalArgumentException(
          "An idempotency key must be 1 to 255 printable ASCII characters, without spaces.");
    }
    return key;
  }

  private static boolean hasLineBreak(String text) {
    return text.indexOf('\r') >= 0 || text.indexOf('\n') >= 0;
  }
}
