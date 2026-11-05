package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * The organisation's Qeet ID subscription. Status {@code none} means it has none.
 *
 * @param status One of {@code "none"}, {@code "trialing"}, {@code "active"}, {@code "past_due"},
 *     {@code "canceled"}; others may be added
 * @param planCode the plan code
 * @param planName the plan name
 * @param currency the currency
 * @param amountMinor In minor units (paise, cents).
 * @param interval the interval
 * @param currentPeriodStart the current period start
 * @param currentPeriodEnd the current period end
 * @param trialEnd the trial end
 * @param cancelAtPeriodEnd whether cancel at period end
 * @param endedAt the ended time
 */
public record Subscription(
    String status,
    @JsonProperty("plan_code") String planCode,
    @JsonProperty("plan_name") String planName,
    String currency,
    @JsonProperty("amount_minor") long amountMinor,
    String interval,
    @JsonProperty("current_period_start") @Nullable Instant currentPeriodStart,
    @JsonProperty("current_period_end") @Nullable Instant currentPeriodEnd,
    @JsonProperty("trial_end") @Nullable Instant trialEnd,
    @JsonProperty("cancel_at_period_end") boolean cancelAtPeriodEnd,
    @JsonProperty("ended_at") @Nullable Instant endedAt) {}
