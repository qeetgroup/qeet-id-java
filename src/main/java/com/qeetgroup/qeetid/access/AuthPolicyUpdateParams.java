package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;
import org.jspecify.annotations.Nullable;

/** The sign-in policy to save. Read it first and send it back whole: the update replaces it. */
public final class AuthPolicyUpdateParams extends Params {
  private AuthPolicyUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AuthPolicyUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AuthPolicyUpdateParams}. */
  public static final class Builder extends Params.Builder<AuthPolicyUpdateParams, Builder> {
    private Builder() {}

    /**
     * Whether password enabled.
     *
     * @param passwordEnabled the value
     * @return this builder
     */
    public Builder passwordEnabled(boolean passwordEnabled) {
      return set("password_enabled", passwordEnabled);
    }

    /**
     * 8 to 128; clamped.
     *
     * @param passwordMinLength the value
     * @return this builder
     */
    public Builder passwordMinLength(int passwordMinLength) {
      return set("password_min_length", passwordMinLength);
    }

    /**
     * The password require uppercase.
     *
     * @param passwordRequireUppercase the value
     * @return this builder
     */
    public Builder passwordRequireUppercase(boolean passwordRequireUppercase) {
      return set("password_require_uppercase", passwordRequireUppercase);
    }

    /**
     * The password require number.
     *
     * @param passwordRequireNumber the value
     * @return this builder
     */
    public Builder passwordRequireNumber(boolean passwordRequireNumber) {
      return set("password_require_number", passwordRequireNumber);
    }

    /**
     * The password require symbol.
     *
     * @param passwordRequireSymbol the value
     * @return this builder
     */
    public Builder passwordRequireSymbol(boolean passwordRequireSymbol) {
      return set("password_require_symbol", passwordRequireSymbol);
    }

    /**
     * Whether magic link enabled.
     *
     * @param magicLinkEnabled the value
     * @return this builder
     */
    public Builder magicLinkEnabled(boolean magicLinkEnabled) {
      return set("magic_link_enabled", magicLinkEnabled);
    }

    /**
     * 5 to 1440; clamped.
     *
     * @param magicLinkTtlMinutes the value
     * @return this builder
     */
    public Builder magicLinkTtlMinutes(int magicLinkTtlMinutes) {
      return set("magic_link_ttl_minutes", magicLinkTtlMinutes);
    }

    /**
     * Whether passkey enabled.
     *
     * @param passkeyEnabled the value
     * @return this builder
     */
    public Builder passkeyEnabled(boolean passkeyEnabled) {
      return set("passkey_enabled", passkeyEnabled);
    }

    /**
     * Whether otp email enabled.
     *
     * @param otpEmailEnabled the value
     * @return this builder
     */
    public Builder otpEmailEnabled(boolean otpEmailEnabled) {
      return set("otp_email_enabled", otpEmailEnabled);
    }

    /**
     * Whether otp sms enabled.
     *
     * @param otpSmsEnabled the value
     * @return this builder
     */
    public Builder otpSmsEnabled(boolean otpSmsEnabled) {
      return set("otp_sms_enabled", otpSmsEnabled);
    }

    /**
     * Whether self registration enabled.
     *
     * @param selfRegistrationEnabled the value
     * @return this builder
     */
    public Builder selfRegistrationEnabled(boolean selfRegistrationEnabled) {
      return set("self_registration_enabled", selfRegistrationEnabled);
    }

    /**
     * Whether remember device enabled.
     *
     * @param rememberDeviceEnabled the value
     * @return this builder
     */
    public Builder rememberDeviceEnabled(boolean rememberDeviceEnabled) {
      return set("remember_device_enabled", rememberDeviceEnabled);
    }

    /**
     * The remember device days.
     *
     * @param rememberDeviceDays the value
     * @return this builder
     */
    public Builder rememberDeviceDays(int rememberDeviceDays) {
      return set("remember_device_days", rememberDeviceDays);
    }

    /**
     * null: the platform default. Null sends an explicit null, which clears it.
     *
     * @param lockoutThreshold the value
     * @return this builder
     */
    public Builder lockoutThreshold(@Nullable Integer lockoutThreshold) {
      return set("lockout_threshold", lockoutThreshold);
    }

    /**
     * The lockout window minutes. Null sends an explicit null, which clears it.
     *
     * @param lockoutWindowMinutes the value
     * @return this builder
     */
    public Builder lockoutWindowMinutes(@Nullable Integer lockoutWindowMinutes) {
      return set("lockout_window_minutes", lockoutWindowMinutes);
    }

    /**
     * The lockout duration minutes. Null sends an explicit null, which clears it.
     *
     * @param lockoutDurationMinutes the value
     * @return this builder
     */
    public Builder lockoutDurationMinutes(@Nullable Integer lockoutDurationMinutes) {
      return set("lockout_duration_minutes", lockoutDurationMinutes);
    }

    @Override
    public AuthPolicyUpdateParams build() {
      return new AuthPolicyUpdateParams(this);
    }
  }
}
