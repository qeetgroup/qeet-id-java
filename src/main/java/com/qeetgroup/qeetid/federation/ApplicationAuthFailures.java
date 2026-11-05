package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * An application's wrong-secret lockout and its last failed authentications.
 *
 * @param lockedUntil the locked until
 * @param items the items
 */
public record ApplicationAuthFailures(
    @JsonProperty("locked_until") @Nullable Instant lockedUntil, List<Item> items) {

  /**
   * Item.
   *
   * @param at the at
   * @param ip the IP
   * @param reason the reason
   */
  public record Item(Instant at, String ip, String reason) {}
}
