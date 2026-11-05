package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * A new agent secret, shown once, and when the old one stops working.
 *
 * @param id its id
 * @param secret the secret
 * @param previousSecretExpiresAt the previous secret expires time
 */
public record AgentSecret(
    String id,
    String secret,
    @JsonProperty("previous_secret_expires_at") Instant previousSecretExpiresAt) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "AgentSecret[id="
        + id
        + ", secret=[redacted], previousSecretExpiresAt="
        + previousSecretExpiresAt
        + "]";
  }
}
