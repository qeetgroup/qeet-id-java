package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Session bounds, MFA enforcement and step-up rules for the organisation. Durations are Go-style
 * strings such as {@code "720h"}.
 *
 * @param tenantId the organisation's id
 * @param passwordMinLength the password min length
 * @param passwordComplexity the password complexity
 * @param sessionMaxAge the session max age
 * @param sessionIdleTimeout the session idle timeout
 * @param maxSessions null: no limit.
 * @param sessionLimitAction One of {@code "evict_oldest"}, {@code "refuse"}; others may be added
 * @param mfaEnforcement One of {@code "optional"}, {@code "required"}, {@code "admin_only"}; others
 *     may be added
 * @param mfaGraceDays whether MFA grace days
 * @param mfaEnforcedSince whether MFA enforced since
 * @param stepUpWindow the step up window
 * @param stepUpMethods One of {@code "totp"}, {@code "recovery_code"}, {@code "otp"}, {@code
 *     "webauthn"}, {@code "push"}; others may be added
 * @param settings the settings
 */
public record SecurityPolicy(
    @JsonProperty("tenant_id") @Nullable String tenantId,
    @JsonProperty("password_min_length") @Nullable Integer passwordMinLength,
    @JsonProperty("password_complexity") @Nullable String passwordComplexity,
    @JsonProperty("session_max_age") @Nullable String sessionMaxAge,
    @JsonProperty("session_idle_timeout") @Nullable String sessionIdleTimeout,
    @JsonProperty("max_sessions") @Nullable Integer maxSessions,
    @JsonProperty("session_limit_action") @Nullable String sessionLimitAction,
    @JsonProperty("mfa_enforcement") @Nullable String mfaEnforcement,
    @JsonProperty("mfa_grace_days") @Nullable Integer mfaGraceDays,
    @JsonProperty("mfa_enforced_since") @Nullable Instant mfaEnforcedSince,
    @JsonProperty("step_up_window") @Nullable String stepUpWindow,
    @JsonProperty("step_up_methods") List<String> stepUpMethods,
    Map<String, Object> settings) {}
