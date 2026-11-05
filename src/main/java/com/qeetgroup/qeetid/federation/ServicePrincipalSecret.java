package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * A new secret, shown once, and when the old one stops working.
 *
 * @param clientId the client's id
 * @param clientSecret the client secret
 * @param previousSecretExpiresAt the previous secret expires time
 * @param warning the warning
 */
public record ServicePrincipalSecret(
    @JsonProperty("client_id") String clientId,
    @JsonProperty("client_secret") String clientSecret,
    @JsonProperty("previous_secret_expires_at") Instant previousSecretExpiresAt,
    String warning) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "ServicePrincipalSecret[clientId="
        + clientId
        + ", clientSecret=[redacted], previousSecretExpiresAt="
        + previousSecretExpiresAt
        + ", warning="
        + warning
        + "]";
  }
}
