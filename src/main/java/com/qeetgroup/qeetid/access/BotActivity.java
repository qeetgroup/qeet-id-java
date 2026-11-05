package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Recent bot-detection verdicts, a page at a time, with totals.
 *
 * @param recent the recent
 * @param nextCursor the next cursor
 * @param total the total
 * @param stats the stats
 */
public record BotActivity(
    List<BotEvent> recent,
    @JsonProperty("next_cursor") @Nullable String nextCursor,
    long total,
    Stats stats) {

  /**
   * Stats.
   *
   * @param blocked24h the blocked 24h
   * @param challenged24h the challenged 24h
   * @param threshold the threshold
   */
  public record Stats(
      @JsonProperty("blocked_24h") int blocked24h,
      @JsonProperty("challenged_24h") int challenged24h,
      double threshold) {}
}
