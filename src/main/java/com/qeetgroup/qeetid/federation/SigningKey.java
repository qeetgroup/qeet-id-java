package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * Non-secret metadata of one of the issuer's signing keys.
 *
 * @param kid the kid
 * @param alg the alg
 * @param use the use
 * @param status One of {@code "candidate"}, {@code "active"}, {@code "retired"}; others may be
 *     added
 * @param retireAt the retire time
 * @param createdAt when it was created
 * @param activatedAt the activated time
 */
public record SigningKey(
    String kid,
    String alg,
    String use,
    String status,
    @JsonProperty("retire_at") @Nullable Instant retireAt,
    @JsonProperty("created_at") @Nullable Instant createdAt,
    @JsonProperty("activated_at") @Nullable Instant activatedAt) {}
