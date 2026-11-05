package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * {@code active} for an immediate activation (a free plan); {@code checkout} with the URL to send
 * the person to.
 *
 * @param status One of {@code "active"}, {@code "checkout"}; others may be added
 * @param checkoutUrl the checkout URL
 * @param provider the provider
 */
public record CheckoutResult(
    String status,
    @JsonProperty("checkout_url") @Nullable String checkoutUrl,
    @Nullable String provider) {}
