package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * Why, by whom and when an organisation's own user was suspended.
 *
 * @param reason the reason
 * @param source the source
 * @param suspendedAt the suspended time
 * @param suspendedBy the suspended by
 * @param suspendedByEmail the suspended by email
 */
public record UserSuspension(
    String reason,
    String source,
    @JsonProperty("suspended_at") @Nullable Instant suspendedAt,
    @JsonProperty("suspended_by") @Nullable String suspendedBy,
    @JsonProperty("suspended_by_email") @Nullable String suspendedByEmail) {}
