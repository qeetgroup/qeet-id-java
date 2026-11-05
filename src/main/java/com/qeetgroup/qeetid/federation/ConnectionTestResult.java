package com.qeetgroup.qeetid.federation;

import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A connection test: each check and the overall result.
 *
 * @param ok the ok
 * @param checks the checks
 */
public record ConnectionTestResult(boolean ok, List<Check> checks) {

  /**
   * Check.
   *
   * @param name its name
   * @param ok the ok
   * @param detail the detail
   */
  public record Check(String name, boolean ok, @Nullable String detail) {}
}
