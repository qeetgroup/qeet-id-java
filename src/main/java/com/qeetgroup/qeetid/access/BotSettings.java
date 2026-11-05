package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bot-detection settings. Replaced whole on update.
 *
 * @param uaCheck the ua check
 * @param honeypot the honeypot
 * @param captcha the captcha
 * @param signature the signature
 * @param scoreThreshold the score threshold
 */
public record BotSettings(
    @JsonProperty("ua_check") boolean uaCheck,
    boolean honeypot,
    boolean captcha,
    boolean signature,
    @JsonProperty("score_threshold") double scoreThreshold) {}
