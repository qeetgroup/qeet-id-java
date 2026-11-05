package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An authentication hook's switches after an update.
 *
 * @param enabled whether it is on
 * @param failOpen the fail open
 */
public record AuthHookState(boolean enabled, @JsonProperty("fail_open") boolean failOpen) {}
