package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * How people may sign in to the organisation.
 *
 * @param passwordEnabled whether password enabled
 * @param passwordMinLength 8 to 128; clamped.
 * @param passwordRequireUppercase the password require uppercase
 * @param passwordRequireNumber the password require number
 * @param passwordRequireSymbol the password require symbol
 * @param magicLinkEnabled whether magic link enabled
 * @param magicLinkTtlMinutes 5 to 1440; clamped.
 * @param passkeyEnabled whether passkey enabled
 * @param otpEmailEnabled whether otp email enabled
 * @param otpSmsEnabled whether otp sms enabled
 * @param selfRegistrationEnabled whether self registration enabled
 * @param rememberDeviceEnabled whether remember device enabled
 * @param rememberDeviceDays the remember device days
 * @param lockoutThreshold null: the platform default.
 * @param lockoutWindowMinutes the lockout window minutes
 * @param lockoutDurationMinutes the lockout duration minutes
 * @param lockoutLimits Read-only: the platform defaults and bounds of the lockout settings.
 */
public record AuthPolicy(
    @JsonProperty("password_enabled") @Nullable Boolean passwordEnabled,
    @JsonProperty("password_min_length") @Nullable Integer passwordMinLength,
    @JsonProperty("password_require_uppercase") @Nullable Boolean passwordRequireUppercase,
    @JsonProperty("password_require_number") @Nullable Boolean passwordRequireNumber,
    @JsonProperty("password_require_symbol") @Nullable Boolean passwordRequireSymbol,
    @JsonProperty("magic_link_enabled") @Nullable Boolean magicLinkEnabled,
    @JsonProperty("magic_link_ttl_minutes") @Nullable Integer magicLinkTtlMinutes,
    @JsonProperty("passkey_enabled") @Nullable Boolean passkeyEnabled,
    @JsonProperty("otp_email_enabled") @Nullable Boolean otpEmailEnabled,
    @JsonProperty("otp_sms_enabled") @Nullable Boolean otpSmsEnabled,
    @JsonProperty("self_registration_enabled") @Nullable Boolean selfRegistrationEnabled,
    @JsonProperty("remember_device_enabled") @Nullable Boolean rememberDeviceEnabled,
    @JsonProperty("remember_device_days") @Nullable Integer rememberDeviceDays,
    @JsonProperty("lockout_threshold") @Nullable Integer lockoutThreshold,
    @JsonProperty("lockout_window_minutes") @Nullable Integer lockoutWindowMinutes,
    @JsonProperty("lockout_duration_minutes") @Nullable Integer lockoutDurationMinutes,
    @JsonProperty("lockout_limits") @Nullable LockoutLimits lockoutLimits) {

  /**
   * Read-only: the platform defaults and bounds of the lockout settings.
   *
   * @param defaults the defaults
   * @param min the min
   * @param max the max
   */
  public record LockoutLimits(
      @Nullable LockoutSettings defaults,
      @Nullable LockoutSettings min,
      @Nullable LockoutSettings max) {}
}
