package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A registered application and, for a confidential one, its secret: shown once, never again.
 *
 * @param client the client
 * @param clientSecret the client secret
 * @param warning the warning
 */
public record ApplicationCreated(
    Application client,
    @JsonProperty("client_secret") @Nullable String clientSecret,
    @Nullable String warning) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "ApplicationCreated[client="
        + client
        + ", clientSecret=[redacted], warning="
        + warning
        + "]";
  }
}
