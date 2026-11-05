package com.qeetgroup.qeetid.internal;

import java.util.Locale;
import org.jspecify.annotations.Nullable;

/** Builds the User-Agent: the caller's application, then this SDK and the runtime. */
public final class UserAgent {
  private UserAgent() {}

  /** For example {@code acme-billing/2.3.1 qeet-id-java/1.0.0 java/21.0.5 (Linux; amd64)}. */
  public static String build(@Nullable String appName, @Nullable String appVersion) {
    String sdk =
        "qeet-id-java/"
            + Version.VERSION
            + " java/"
            + System.getProperty("java.version", "unknown")
            + " ("
            + System.getProperty("os.name", "unknown")
            + "; "
            + System.getProperty("os.arch", "unknown").toLowerCase(Locale.ROOT)
            + ")";
    if (appName == null) {
      return sdk;
    }
    return appName + (appVersion == null ? "" : "/" + appVersion) + " " + sdk;
  }

  /**
   * Checks an application name or version for the User-Agent: a short token of letters, digits and
   * {@code . _ - @ /}.
   */
  public static String checkToken(String value, String what) {
    if (value.isEmpty() || value.length() > 100 || !value.matches("[A-Za-z0-9._\\-@/+]+")) {
      throw new IllegalArgumentException(
          what + " must be a short token such as acme-billing (letters, digits and . _ - @ / +).");
    }
    return value;
  }
}
