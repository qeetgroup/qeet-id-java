package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A sign-in stopped before its session: complete it at {@code POST /v1/auth/mfa} with {@code
 * mfa_token}.
 *
 * @param mfaRequired whether MFA required
 * @param mfaToken whether MFA token
 * @param methods the methods
 * @param factors the factors
 * @param enrollmentRequired The account must set up a second factor first.
 * @param enrollmentDueAt On an enrolment challenge in a grace period: when it ends.
 */
public record MfaChallenge(
    @JsonProperty("mfa_required") boolean mfaRequired,
    @JsonProperty("mfa_token") String mfaToken,
    List<String> methods,
    List<MfaFactor> factors,
    @JsonProperty("enrollment_required") @Nullable Boolean enrollmentRequired,
    @JsonProperty("enrollment_due_at") @Nullable Instant enrollmentDueAt) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "MfaChallenge[mfaRequired="
        + mfaRequired
        + ", mfaToken=[redacted], methods="
        + methods
        + ", factors="
        + factors
        + ", enrollmentRequired="
        + enrollmentRequired
        + ", enrollmentDueAt="
        + enrollmentDueAt
        + "]";
  }
}
