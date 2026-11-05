package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A signed call to your endpoint during sign-in, which may allow, deny or add claims. Its secret is
 * never returned.
 *
 * @param id its id
 * @param trigger the trigger
 * @param url the URL
 * @param enabled whether it is on
 * @param failOpen Whether a failing hook lets sign-in proceed.
 * @param createdAt when it was created
 * @param lastCalledAt the last called time
 * @param lastOutcome One of {@code "allowed"}, {@code "denied"}, {@code "skipped"}; others may be
 *     added
 * @param lastStatusCode the last status code
 * @param calls the calls
 * @param skips whether skips
 * @param denies the denies
 */
public record AuthHook(
    String id,
    String trigger,
    String url,
    boolean enabled,
    @JsonProperty("fail_open") boolean failOpen,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("last_called_at") @Nullable Instant lastCalledAt,
    @JsonProperty("last_outcome") @Nullable String lastOutcome,
    @JsonProperty("last_status_code") @Nullable Integer lastStatusCode,
    @Nullable Integer calls,
    @Nullable Integer skips,
    @Nullable Integer denies) {}
