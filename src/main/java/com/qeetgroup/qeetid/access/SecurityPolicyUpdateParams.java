package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** The security policy fields to save. Durations are Go-style strings such as {@code "720h"}. */
public final class SecurityPolicyUpdateParams extends Params {
  private SecurityPolicyUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SecurityPolicyUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SecurityPolicyUpdateParams}. */
  public static final class Builder extends Params.Builder<SecurityPolicyUpdateParams, Builder> {
    private Builder() {}

    /**
     * The password min length.
     *
     * @param passwordMinLength the value
     * @return this builder
     */
    public Builder passwordMinLength(int passwordMinLength) {
      return set("password_min_length", passwordMinLength);
    }

    /**
     * The password complexity.
     *
     * @param passwordComplexity the value
     * @return this builder
     */
    public Builder passwordComplexity(String passwordComplexity) {
      return set("password_complexity", nonNull(passwordComplexity, "passwordComplexity"));
    }

    /**
     * The session max age.
     *
     * @param sessionMaxAge the value
     * @return this builder
     */
    public Builder sessionMaxAge(String sessionMaxAge) {
      return set("session_max_age", nonNull(sessionMaxAge, "sessionMaxAge"));
    }

    /**
     * The session idle timeout.
     *
     * @param sessionIdleTimeout the value
     * @return this builder
     */
    public Builder sessionIdleTimeout(String sessionIdleTimeout) {
      return set("session_idle_timeout", nonNull(sessionIdleTimeout, "sessionIdleTimeout"));
    }

    /**
     * null: no limit. Null sends an explicit null, which clears it.
     *
     * @param maxSessions the value
     * @return this builder
     */
    public Builder maxSessions(@Nullable Integer maxSessions) {
      return set("max_sessions", maxSessions);
    }

    /**
     * One of {@code "evict_oldest"}, {@code "refuse"}.
     *
     * @param sessionLimitAction the value
     * @return this builder
     */
    public Builder sessionLimitAction(String sessionLimitAction) {
      return set("session_limit_action", nonNull(sessionLimitAction, "sessionLimitAction"));
    }

    /**
     * One of {@code "optional"}, {@code "required"}, {@code "admin_only"}.
     *
     * @param mfaEnforcement the value
     * @return this builder
     */
    public Builder mfaEnforcement(String mfaEnforcement) {
      return set("mfa_enforcement", nonNull(mfaEnforcement, "mfaEnforcement"));
    }

    /**
     * Whether MFA grace days.
     *
     * @param mfaGraceDays the value
     * @return this builder
     */
    public Builder mfaGraceDays(int mfaGraceDays) {
      return set("mfa_grace_days", mfaGraceDays);
    }

    /**
     * Whether MFA enforced since. Null sends an explicit null, which clears it.
     *
     * @param mfaEnforcedSince the value
     * @return this builder
     */
    public Builder mfaEnforcedSince(@Nullable Instant mfaEnforcedSince) {
      return set("mfa_enforced_since", mfaEnforcedSince);
    }

    /**
     * The step up window.
     *
     * @param stepUpWindow the value
     * @return this builder
     */
    public Builder stepUpWindow(String stepUpWindow) {
      return set("step_up_window", nonNull(stepUpWindow, "stepUpWindow"));
    }

    /**
     * One of {@code "totp"}, {@code "recovery_code"}, {@code "otp"}, {@code "webauthn"}, {@code
     * "push"}.
     *
     * @param stepUpMethods the value
     * @return this builder
     */
    public Builder stepUpMethods(List<String> stepUpMethods) {
      return set("step_up_methods", listOf(stepUpMethods));
    }

    /**
     * The settings.
     *
     * @param settings the value
     * @return this builder
     */
    public Builder settings(Map<String, ?> settings) {
      return set("settings", nonNull(settings, "settings"));
    }

    @Override
    public SecurityPolicyUpdateParams build() {
      return new SecurityPolicyUpdateParams(this);
    }
  }
}
