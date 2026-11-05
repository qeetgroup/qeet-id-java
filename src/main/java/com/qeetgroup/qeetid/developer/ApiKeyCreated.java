package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A new key. {@code secret} is shown only here: store it in a secrets manager.
 *
 * @param apiKey the API key
 * @param secret the secret
 * @param warning the warning
 */
public record ApiKeyCreated(
    @JsonProperty("api_key") ApiKey apiKey, String secret, @Nullable String warning) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "ApiKeyCreated[apiKey=" + apiKey + ", secret=[redacted], warning=" + warning + "]";
  }
}
