package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * One bot-detection verdict on a sign-in attempt.
 *
 * @param id its id
 * @param ip the IP
 * @param userAgent the user agent
 * @param score the score
 * @param verdict One of {@code "allowed"}, {@code "challenged"}, {@code "blocked"}; others may be
 *     added
 * @param outcome One of {@code "signed_in"}, {@code "failed"}, {@code "unknown"}; others may be
 *     added
 * @param userId the user's id
 * @param userEmail the user email
 * @param createdAt when it was created
 */
public record BotEvent(
    String id,
    @Nullable String ip,
    @JsonProperty("user_agent") String userAgent,
    double score,
    String verdict,
    String outcome,
    @JsonProperty("user_id") @Nullable String userId,
    @JsonProperty("user_email") @Nullable String userEmail,
    @JsonProperty("created_at") Instant createdAt) {}
