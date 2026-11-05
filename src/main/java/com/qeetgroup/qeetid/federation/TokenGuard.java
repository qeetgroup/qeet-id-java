package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.exception.ApiError;
import com.qeetgroup.qeetid.exception.AuthenticationException;
import com.qeetgroup.qeetid.exception.PermissionDeniedException;
import java.util.List;
import java.util.Objects;

/**
 * Admits a token for one protected service (an API or an MCP server) by introspection: the token
 * must be active, issued for the service's audience, and carry the required scopes. Unlike local
 * verification, a revoked session stops working at once. Get one from {@code
 * qeetId.oauth().tokenGuard(audience, client)}; it is thread-safe.
 */
public final class TokenGuard {
  private final OAuth oauth;
  private final String audience;
  private final OAuthClient client;

  TokenGuard(OAuth oauth, String audience, OAuthClient client) {
    Objects.requireNonNull(audience, "audience");
    if (audience.isBlank()) {
      throw new IllegalArgumentException(
          "A token guard needs the protected service's audience, as the tokens issued for it name it in aud.");
    }
    if (client.clientSecret() == null) {
      throw new IllegalArgumentException(
          "A token guard needs a confidential client: introspection authenticates it.");
    }
    this.oauth = oauth;
    this.audience = audience;
    this.client = client;
  }

  /**
   * The audience tokens must be issued for.
   *
   * @return the audience
   */
  public String audience() {
    return audience;
  }

  /**
   * Introspects {@code token} and returns its claims when every required scope is granted.
   *
   * @param token the bearer token
   * @param requiredScopes the scopes it must carry
   * @return the token's claims
   * @throws AuthenticationException code {@code invalid_token}, when the token is inactive or
   *     issued for another audience
   * @throws PermissionDeniedException code {@code insufficient_scope}, when a scope is missing
   */
  public Introspection verify(String token, String... requiredScopes) {
    return verify(token, List.of(requiredScopes), RequestOptions.none());
  }

  /**
   * Introspects {@code token}, with per-call settings. See {@link #verify(String, String...)}.
   *
   * @param token the bearer token
   * @param requiredScopes the scopes it must carry
   * @param options per-call settings
   * @return the token's claims
   */
  public Introspection verify(String token, List<String> requiredScopes, RequestOptions options) {
    Introspection claims = oauth.introspect(token, client, "access_token", options);
    if (!claims.active()) {
      throw new AuthenticationException(
          ApiError.of(401, "invalid_token", "The token is not active."));
    }
    if (!claims.audience().contains(audience)) {
      throw new AuthenticationException(
          ApiError.of(401, "invalid_token", "The token was not issued for this service."));
    }
    for (String scope : requiredScopes) {
      if (!claims.scopes().contains(scope)) {
        throw new PermissionDeniedException(
            ApiError.of(403, "insufficient_scope", "The token lacks the scope " + scope + "."));
      }
    }
    return claims;
  }

  @Override
  public String toString() {
    return "TokenGuard[audience=" + audience + ", " + client + "]";
  }
}
