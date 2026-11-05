package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * An issued credential: a signed JWT-VC.
 *
 * @param credentialId the credential's id
 * @param jwt the jwt
 * @param expiresAt when it expires
 */
public record IssuedCredential(
    @JsonProperty("credential_id") String credentialId,
    String jwt,
    @JsonProperty("expires_at") @Nullable Instant expiresAt) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "IssuedCredential[credentialId="
        + credentialId
        + ", jwt=[redacted], expiresAt="
        + expiresAt
        + "]";
  }
}
