package com.qeetgroup.qeetid.jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * The claims of a verified Qeet ID access token. The typed accessors read the claims Qeet ID
 * issues; {@link #claims()} has every claim as the token carries it.
 */
public final class AccessTokenClaims {
  private final Map<String, Object> claims;

  /**
   * Wraps a token's claims, as {@link TokenVerifier} does once it has verified them.
   *
   * @param claims the claims
   */
  public AccessTokenClaims(Map<String, Object> claims) {
    this.claims = Collections.unmodifiableMap(new LinkedHashMap<>(claims));
  }

  /**
   * Every claim, as the token carries it.
   *
   * @return the claims
   */
  public Map<String, Object> claims() {
    return claims;
  }

  /**
   * The issuer ({@code iss}).
   *
   * @return the issuer
   */
  public String issuer() {
    String value = string("iss");
    return value == null ? "" : value;
  }

  /**
   * The subject ({@code sub}): the user's id, or a service principal's or agent's on a machine
   * token.
   *
   * @return the subject
   */
  public String subject() {
    String value = string("sub");
    return value == null ? "" : value;
  }

  /**
   * The audiences ({@code aud}): the platform audience, or the RFC 8707 resource the token was
   * issued for.
   *
   * @return the audiences
   */
  public List<String> audience() {
    Object aud = claims.get("aud");
    if (aud instanceof String single) {
      return List.of(single);
    }
    if (aud instanceof List<?> many) {
      return many.stream().filter(String.class::isInstance).map(String.class::cast).toList();
    }
    return List.of();
  }

  /**
   * When the token expires ({@code exp}).
   *
   * @return the expiry
   */
  public Instant expiresAt() {
    Instant at = time("exp");
    return at == null ? Instant.EPOCH : at;
  }

  /**
   * When the token was issued ({@code iat}).
   *
   * @return the time, or null
   */
  public @Nullable Instant issuedAt() {
    return time("iat");
  }

  /**
   * When the token becomes valid ({@code nbf}).
   *
   * @return the time, or null
   */
  public @Nullable Instant notBefore() {
    return time("nbf");
  }

  /**
   * The token's id ({@code jti}).
   *
   * @return the id, or null
   */
  public @Nullable String jwtId() {
    return string("jti");
  }

  /**
   * The user the token is for ({@code user_id}).
   *
   * @return the user id, or null
   */
  public @Nullable String userId() {
    return string("user_id");
  }

  /**
   * The user's organisation ({@code tenant_id}).
   *
   * @return the organisation id, or null
   */
  public @Nullable String tenantId() {
    return string("tenant_id");
  }

  /**
   * The session the token belongs to ({@code sid}).
   *
   * @return the session id, or null
   */
  public @Nullable String sessionId() {
    return string("sid");
  }

  /**
   * The granted scopes, space-separated ({@code scope}).
   *
   * @return the scope, or null
   */
  public @Nullable String scope() {
    return string("scope");
  }

  /**
   * The granted scopes, one by one.
   *
   * @return the scopes
   */
  public Set<String> scopes() {
    String scope = scope();
    if (scope == null || scope.isBlank()) {
      return Set.of();
    }
    return Collections.unmodifiableSet(
        new LinkedHashSet<>(Arrays.asList(scope.trim().split("\\s+", -1))));
  }

  /**
   * Whether the token grants {@code scope}.
   *
   * @param scope the scope, such as {@code users:read}
   * @return whether it is granted
   */
  public boolean hasScope(String scope) {
    return scopes().contains(scope);
  }

  /**
   * {@code service} or {@code agent} on a machine token; null on a person's ({@code actor_type}).
   *
   * @return the actor type, or null
   */
  public @Nullable String actorType() {
    return string("actor_type");
  }

  /**
   * The agent, on an agent's token ({@code agent_id}).
   *
   * @return the agent id, or null
   */
  public @Nullable String agentId() {
    return string("agent_id");
  }

  /**
   * RFC 8693: the party acting for the subject, on a delegated token ({@code act.sub}).
   *
   * @return the actor's subject, or null
   */
  public @Nullable String actor() {
    return claims.get("act") instanceof Map<?, ?> act && act.get("sub") instanceof String sub
        ? sub
        : null;
  }

  /**
   * RFC 9068: the application the token was issued to ({@code client_id}).
   *
   * @return the client id, or null
   */
  public @Nullable String clientId() {
    return string("client_id");
  }

  /**
   * How many token exchanges produced this token ({@code exchange_depth}); 0 for none.
   *
   * @return the depth
   */
  public int exchangeDepth() {
    return claims.get("exchange_depth") instanceof Number depth ? depth.intValue() : 0;
  }

  /**
   * Claims an authentication hook added ({@code claims}).
   *
   * @return the claims, empty when there are none
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> customClaims() {
    return claims.get("claims") instanceof Map<?, ?> custom
        ? Collections.unmodifiableMap((Map<String, Object>) custom)
        : Map.of();
  }

  @Override
  public String toString() {
    return "AccessTokenClaims[sub="
        + subject()
        + ", tenant_id="
        + tenantId()
        + ", scope="
        + scope()
        + "]";
  }

  private @Nullable String string(String name) {
    return claims.get(name) instanceof String value ? value : null;
  }

  private @Nullable Instant time(String name) {
    return claims.get(name) instanceof Number seconds
        ? Instant.ofEpochSecond(seconds.longValue())
        : null;
  }
}
