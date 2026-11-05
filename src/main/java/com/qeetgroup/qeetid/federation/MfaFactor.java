package com.qeetgroup.qeetid.federation;

import org.jspecify.annotations.Nullable;

/**
 * A second factor the person may complete a challenge with.
 *
 * @param type One of {@code "totp"}, {@code "recovery_code"}, {@code "email"}, {@code "sms"},
 *     {@code "passkey"}, {@code "push"}; others may be added
 * @param id An email or SMS factor's id.
 * @param destination An email or SMS factor's masked address or number.
 */
public record MfaFactor(String type, @Nullable String id, @Nullable String destination) {}
