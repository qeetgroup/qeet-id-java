package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A new secret, shown once. The old one stops working at once.
 *
 * @param clientSecret the client secret
 * @param warning the warning
 */
public record ApplicationSecret(
    @JsonProperty("client_secret") String clientSecret, @Nullable String warning) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "ApplicationSecret[clientSecret=[redacted], warning=" + warning + "]";
  }
}
