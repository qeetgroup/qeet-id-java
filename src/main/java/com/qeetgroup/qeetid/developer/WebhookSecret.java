package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * A new webhook signing secret, shown once, and when the old one stops signing.
 *
 * @param secret the secret
 * @param previousSecretExpiresAt the previous secret expires time
 */
public record WebhookSecret(
    String secret, @JsonProperty("previous_secret_expires_at") Instant previousSecretExpiresAt) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "WebhookSecret[secret=[redacted], previousSecretExpiresAt="
        + previousSecretExpiresAt
        + "]";
  }
}
