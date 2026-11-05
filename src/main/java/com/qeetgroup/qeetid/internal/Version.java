package com.qeetgroup.qeetid.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** This SDK's version, written into {@code version.properties} at build time. */
public final class Version {
  /** The version, such as {@code 1.2.0}; {@code 0.0.0-development} when run from sources. */
  public static final String VERSION = load();

  private Version() {}

  private static String load() {
    try (InputStream in = Version.class.getResourceAsStream("version.properties")) {
      if (in == null) {
        return "0.0.0-development";
      }
      Properties properties = new Properties();
      properties.load(in);
      String version = properties.getProperty("version", "");
      return version.isEmpty() || version.startsWith("$") ? "0.0.0-development" : version;
    } catch (IOException e) {
      return "0.0.0-development";
    }
  }
}
