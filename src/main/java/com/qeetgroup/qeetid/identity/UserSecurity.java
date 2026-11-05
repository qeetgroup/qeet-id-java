package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A user's authentication posture.
 *
 * @param mfaRequired whether MFA required
 * @param mfaEnabled whether MFA enabled
 * @param totpEnabled whether totp enabled
 * @param otpFactors the otp factors
 * @param pushDevices the push devices
 * @param passkeys the passkeys
 * @param passkeyLastUsedAt the passkey last used time
 * @param recoveryCodesRemaining the recovery codes remaining
 * @param passwordSet the password set
 * @param passwordChangedAt the password changed time
 * @param activeSessions the active sessions
 * @param distinctDevices the distinct devices
 */
public record UserSecurity(
    @JsonProperty("mfa_required") boolean mfaRequired,
    @JsonProperty("mfa_enabled") boolean mfaEnabled,
    @JsonProperty("totp_enabled") boolean totpEnabled,
    @JsonProperty("otp_factors") int otpFactors,
    @JsonProperty("push_devices") int pushDevices,
    int passkeys,
    @JsonProperty("passkey_last_used_at") @Nullable Instant passkeyLastUsedAt,
    @JsonProperty("recovery_codes_remaining") int recoveryCodesRemaining,
    @JsonProperty("password_set") boolean passwordSet,
    @JsonProperty("password_changed_at") @Nullable Instant passwordChangedAt,
    @JsonProperty("active_sessions") int activeSessions,
    @JsonProperty("distinct_devices") int distinctDevices) {}
