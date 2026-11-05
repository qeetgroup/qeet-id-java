package com.qeetgroup.qeetid.federation;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * An OAuth application's credentials, for introspection, revocation and the token guard. They are
 * sent in the form body ({@code client_secret_post}), never logged, and never put in an error.
 *
 * @param clientId the application's client ID
 * @param clientSecret its secret; null for a public client
 */
public record OAuthClient(String clientId, @Nullable String clientSecret) {
  /** Checks the client ID. */
  public OAuthClient {
    Objects.requireNonNull(clientId, "clientId");
    if (clientId.isBlank()) {
      throw new IllegalArgumentException("clientId must not be blank.");
    }
  }

  /** The client ID, with the secret shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "OAuthClient[clientId="
        + clientId
        + ", clientSecret="
        + (clientSecret == null ? "null" : "[redacted]")
        + "]";
  }
}
