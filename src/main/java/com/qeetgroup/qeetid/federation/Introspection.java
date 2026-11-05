package com.qeetgroup.qeetid.federation;

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
 * An RFC 7662 introspection answer. Only {@link #active()} is always present; the claims come with
 * an active token. An application learns only about its own tokens and tokens for resources it
 * serves; any other answers inactive.
 */
public final class Introspection {
  private final Map<String, Object> claims;

  /**
   * Wraps an introspection answer.
   *
   * @param claims the answer's fields
   */
  public Introspection(Map<String, Object> claims) {
    this.claims = Collections.unmodifiableMap(new LinkedHashMap<>(claims));
  }

  /**
   * Every field of the answer, as the API sent it.
   *
   * @return the fields
   */
  public Map<String, Object> claims() {
    return claims;
  }

  /**
   * Whether the token is active: issued, unexpired and unrevoked.
   *
   * @return whether it is active
   */
  public boolean active() {
    return Boolean.TRUE.equals(claims.get("active"));
  }

  /**
   * The subject: a user's id, or a service principal's or agent's.
   *
   * @return the subject, or null
   */
  public @Nullable String subject() {
    return string("sub");
  }

  /**
   * The granted scopes, space-separated.
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
    return scope == null || scope.isBlank()
        ? Set.of()
        : Collections.unmodifiableSet(
            new LinkedHashSet<>(Arrays.asList(scope.trim().split("\\s+", -1))));
  }

  /**
   * The audiences the token was issued for.
   *
   * @return the audiences
   */
  public List<String> audience() {
    return strings("aud");
  }

  /**
   * RFC 8707: the resources the token was issued for.
   *
   * @return the resources
   */
  public List<String> resource() {
    return strings("resource");
  }

  /**
   * The application the token was issued to.
   *
   * @return the client ID, or null
   */
  public @Nullable String clientId() {
    return string("client_id");
  }

  /**
   * The issuer.
   *
   * @return the issuer, or null
   */
  public @Nullable String issuer() {
    return string("iss");
  }

  /**
   * The token's type, such as {@code access_token}.
   *
   * @return the type, or null
   */
  public @Nullable String tokenType() {
    return string("token_type");
  }

  /**
   * When the token expires.
   *
   * @return the expiry, or null
   */
  public @Nullable Instant expiresAt() {
    return time("exp");
  }

  /**
   * When the token was issued.
   *
   * @return the time, or null
   */
  public @Nullable Instant issuedAt() {
    return time("iat");
  }

  /**
   * The organisation.
   *
   * @return the organisation's id, or null
   */
  public @Nullable String tenantId() {
    return string("tenant_id");
  }

  /**
   * The session the token belongs to.
   *
   * @return the session's id, or null
   */
  public @Nullable String sessionId() {
    return string("sid");
  }

  /**
   * {@code user}, {@code service} or {@code agent}.
   *
   * @return the actor type, or null
   */
  public @Nullable String actorType() {
    return string("actor_type");
  }

  /**
   * The agent, on an agent's token.
   *
   * @return the agent's id, or null
   */
  public @Nullable String agentId() {
    return string("agent_id");
  }

  /**
   * RFC 8693: the party acting for the subject.
   *
   * @return the actor's subject, or null
   */
  public @Nullable String actor() {
    return claims.get("act") instanceof Map<?, ?> act && act.get("sub") instanceof String sub
        ? sub
        : null;
  }

  @Override
  public String toString() {
    return "Introspection[active=" + active() + ", sub=" + subject() + ", scope=" + scope() + "]";
  }

  private @Nullable String string(String name) {
    return claims.get(name) instanceof String value ? value : null;
  }

  private List<String> strings(String name) {
    Object value = claims.get(name);
    if (value instanceof String single) {
      return List.of(single);
    }
    if (value instanceof List<?> many) {
      return many.stream().filter(String.class::isInstance).map(String.class::cast).toList();
    }
    return List.of();
  }

  private @Nullable Instant time(String name) {
    return claims.get(name) instanceof Number seconds
        ? Instant.ofEpochSecond(seconds.longValue())
        : null;
  }
}
