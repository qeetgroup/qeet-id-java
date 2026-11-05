package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * One of a user's live sessions.
 *
 * @param id its id
 * @param ip the IP
 * @param userAgent the user agent
 * @param createdAt when it was created
 * @param lastSeenAt the last seen time
 * @param device the device
 * @param deviceKind One of {@code "desktop"}, {@code "mobile"}, {@code "tablet"}, {@code
 *     "unknown"}; others may be added
 * @param country the country
 * @param city the city
 */
public record UserSession(
    String id,
    @Nullable String ip,
    @JsonProperty("user_agent") @Nullable String userAgent,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("last_seen_at") Instant lastSeenAt,
    @Nullable String device,
    @JsonProperty("device_kind") @Nullable String deviceKind,
    @Nullable String country,
    @Nullable String city) {}
