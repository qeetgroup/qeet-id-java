package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * The card or mandate on file.
 *
 * @param provider the provider
 * @param type its type
 * @param brand the brand
 * @param last4 the last4
 * @param expMonth the exp month
 * @param expYear the exp year
 * @param lastUsedAt when it was last used
 */
public record PaymentMethod(
    String provider,
    String type,
    @Nullable String brand,
    @Nullable String last4,
    @JsonProperty("exp_month") @Nullable Integer expMonth,
    @JsonProperty("exp_year") @Nullable Integer expYear,
    @JsonProperty("last_used_at") Instant lastUsedAt) {}
