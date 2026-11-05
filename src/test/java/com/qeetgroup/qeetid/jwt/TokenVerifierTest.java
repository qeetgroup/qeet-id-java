package com.qeetgroup.qeetid.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qeetgroup.qeetid.exception.ApiException;
import com.qeetgroup.qeetid.exception.NetworkException;
import com.qeetgroup.qeetid.exception.TokenVerificationException;
import com.qeetgroup.qeetid.exception.TokenVerificationException.Reason;
import com.qeetgroup.qeetid.testing.MockServer;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class TokenVerifierTest {
  static final String ISSUER = "https://api.id.qeet.in";
  static final String AUDIENCE = "https://api.brightleaffoods.in";
  static final Instant NOW = Instant.parse("2026-11-05T10:00:00Z");

  /** A clock the test moves. */
  static final class MutableClock extends Clock {
    Instant now = NOW;

    @Override
    public ZoneId getZone() {
      return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
      return this;
    }

    @Override
    public Instant instant() {
      return now;
    }

    void advance(Duration by) {
      now = now.plus(by);
    }
  }

  private final MockServer server = new MockServer();
  private final MutableClock clock = new MutableClock();
  private final Jwts.Key key1 = Jwts.key("k1");
  private final Jwts.Key key2 = Jwts.key("k2");

  @AfterEach
  void stop() {
    server.close();
  }

  private TokenVerifier verifier() {
    return TokenVerifier.builder()
        .issuer(ISSUER)
        .audience(AUDIENCE)
        .jwksUri(server.url() + "/.well-known/jwks.json")
        .clock(clock)
        .build();
  }

  private Map<String, Object> claims() {
    Map<String, Object> claims = new LinkedHashMap<>();
    claims.put("iss", ISSUER);
    claims.put("sub", "c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f");
    claims.put("aud", List.of(AUDIENCE));
    claims.put("exp", NOW.plusSeconds(600).getEpochSecond());
    claims.put("iat", NOW.minusSeconds(10).getEpochSecond());
    claims.put("nbf", NOW.minusSeconds(10).getEpochSecond());
    claims.put("jti", "jti-1");
    claims.put("user_id", "c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f");
    claims.put("tenant_id", "7d2f9a1c-3b4e-4c5d-8e6f-1a2b3c4d5e6f");
    claims.put("sid", "3f0d9a6c-0d5e-4f2c-9a7b-6e1c9d5a3b84");
    claims.put("scope", "users:read  audit:read");
    return claims;
  }

  private void serveKeys(Jwts.Key... keys) {
    server.enqueue(200, Jwts.jwks(java.util.Arrays.stream(keys).map(Jwts::jwk).toList()));
  }

  private Reason refusal(TokenVerifier verifier, String token) {
    return assertThrows(TokenVerificationException.class, () -> verifier.verify(token)).reason();
  }

  @Test
  void verifiesATokenAndReadsItsClaims() {
    serveKeys(key1);
    Map<String, Object> claims = claims();
    claims.put("actor_type", "agent");
    claims.put("agent_id", "agt_1");
    claims.put("act", Map.of("sub", "svc_7"));
    claims.put("client_id", "app_1");
    claims.put("exchange_depth", 1);
    claims.put("claims", Map.of("plan", "pro"));
    AccessTokenClaims verified = verifier().verify(Jwts.token(key1, Map.of(), claims));
    assertEquals(ISSUER, verified.issuer());
    assertEquals("c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f", verified.subject());
    assertEquals(List.of(AUDIENCE), verified.audience());
    assertEquals(NOW.plusSeconds(600), verified.expiresAt());
    assertEquals(NOW.minusSeconds(10), verified.issuedAt());
    assertEquals(NOW.minusSeconds(10), verified.notBefore());
    assertEquals("jti-1", verified.jwtId());
    assertEquals("7d2f9a1c-3b4e-4c5d-8e6f-1a2b3c4d5e6f", verified.tenantId());
    assertEquals("c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f", verified.userId());
    assertEquals("3f0d9a6c-0d5e-4f2c-9a7b-6e1c9d5a3b84", verified.sessionId());
    assertEquals(Set.of("users:read", "audit:read"), verified.scopes());
    assertTrue(verified.hasScope("audit:read"));
    assertFalse(verified.hasScope("users:write"));
    assertEquals("agent", verified.actorType());
    assertEquals("agt_1", verified.agentId());
    assertEquals("svc_7", verified.actor());
    assertEquals("app_1", verified.clientId());
    assertEquals(1, verified.exchangeDepth());
    assertEquals(Map.of("plan", "pro"), verified.customClaims());
    assertEquals("GET", server.onlyRequest().method());
    assertEquals("/.well-known/jwks.json", server.onlyRequest().path());
  }

  @Test
  void acceptsASingleStringAudienceAndAnyConfiguredAudience() {
    serveKeys(key1);
    TokenVerifier verifier =
        TokenVerifier.builder()
            .issuer(ISSUER)
            .audience("other")
            .audience(AUDIENCE)
            .jwksUri(server.url() + "/jwks")
            .clock(clock)
            .build();
    Map<String, Object> claims = claims();
    claims.put("aud", AUDIENCE);
    AccessTokenClaims verified = verifier.verify(Jwts.token(key1, Map.of(), claims));
    assertEquals(List.of(AUDIENCE), verified.audience());
    assertNull(verified.actorType());
    assertEquals(0, verified.exchangeDepth());
    assertEquals(Map.of(), verified.customClaims());
  }

  @Test
  void refusesTheWrongIssuerOrAudience() {
    serveKeys(key1);
    TokenVerifier verifier = verifier();
    Map<String, Object> wrongIssuer = claims();
    wrongIssuer.put("iss", "https://evil.example");
    assertEquals(Reason.INVALID_ISSUER, refusal(verifier, Jwts.token(key1, Map.of(), wrongIssuer)));
    Map<String, Object> wrongAudience = claims();
    wrongAudience.put("aud", List.of("https://other.example"));
    assertEquals(
        Reason.INVALID_AUDIENCE, refusal(verifier, Jwts.token(key1, Map.of(), wrongAudience)));
    Map<String, Object> noAudience = claims();
    noAudience.remove("aud");
    assertEquals(
        Reason.INVALID_AUDIENCE, refusal(verifier, Jwts.token(key1, Map.of(), noAudience)));
    Map<String, Object> noSubject = claims();
    noSubject.remove("sub");
    assertEquals(Reason.MALFORMED_TOKEN, refusal(verifier, Jwts.token(key1, Map.of(), noSubject)));
  }

  @Test
  void checksTimesWithTheClockSkew() {
    serveKeys(key1);
    TokenVerifier verifier = verifier();
    Map<String, Object> expired = claims();
    expired.put("exp", NOW.minusSeconds(31).getEpochSecond());
    assertEquals(Reason.TOKEN_EXPIRED, refusal(verifier, Jwts.token(key1, Map.of(), expired)));
    Map<String, Object> justExpired = claims();
    justExpired.put("exp", NOW.minusSeconds(29).getEpochSecond());
    verifier.verify(Jwts.token(key1, Map.of(), justExpired));
    Map<String, Object> early = claims();
    early.put("nbf", NOW.plusSeconds(31).getEpochSecond());
    assertEquals(Reason.TOKEN_NOT_YET_VALID, refusal(verifier, Jwts.token(key1, Map.of(), early)));
    Map<String, Object> future = claims();
    future.put("iat", NOW.plusSeconds(120).getEpochSecond());
    assertEquals(Reason.TOKEN_NOT_YET_VALID, refusal(verifier, Jwts.token(key1, Map.of(), future)));
    Map<String, Object> noExpiry = claims();
    noExpiry.remove("exp");
    assertEquals(Reason.MALFORMED_TOKEN, refusal(verifier, Jwts.token(key1, Map.of(), noExpiry)));
    TokenVerifier strict =
        TokenVerifier.builder()
            .issuer(ISSUER)
            .audience(AUDIENCE)
            .jwksUri(server.url() + "/jwks")
            .clockSkew(Duration.ZERO)
            .clock(clock)
            .build();
    serveKeys(key1);
    assertEquals(Reason.TOKEN_EXPIRED, refusal(strict, Jwts.token(key1, Map.of(), justExpired)));
  }

  @Test
  void acceptsOnlyEs256WithAKidAndNoCriticalHeaders() {
    serveKeys(key1);
    TokenVerifier verifier = verifier();
    for (String alg : new String[] {"none", "HS256", "RS256", "ES384", "es256"}) {
      assertEquals(
          Reason.UNSUPPORTED_ALGORITHM,
          refusal(verifier, Jwts.token(key1, Map.of("alg", alg), claims())),
          alg);
    }
    Map<String, Object> noKid = new LinkedHashMap<>();
    noKid.put("kid", "");
    assertEquals(Reason.UNKNOWN_KEY, refusal(verifier, Jwts.token(key1, noKid, claims())));
    assertEquals(
        Reason.MALFORMED_TOKEN,
        refusal(verifier, Jwts.token(key1, Map.of("crit", List.of("exp")), claims())));
  }

  @Test
  void refusesMalformedTokensAndForgedSignatures() {
    serveKeys(key1);
    TokenVerifier verifier = verifier();
    for (String bad :
        new String[] {"", "a.b", "a.b.c.d", "!!.e30.e30", "W10.e30.sig", "x".repeat(20_000)}) {
      assertEquals(
          Reason.MALFORMED_TOKEN, refusal(verifier, bad), bad.length() > 30 ? "long" : bad);
    }
    assertEquals(Reason.UNSUPPORTED_ALGORITHM, refusal(verifier, "e30.e30.e30"));
    String token = Jwts.token(key1, Map.of(), claims());
    String[] parts = token.split("\\.", -1);
    Map<String, Object> elevated = claims();
    elevated.put("scope", "admin");
    String forged =
        parts[0]
            + "."
            + Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                    Jwts.json(elevated).getBytes(java.nio.charset.StandardCharsets.UTF_8))
            + "."
            + parts[2];
    assertEquals(Reason.INVALID_SIGNATURE, refusal(verifier, forged));
    String otherKey = Jwts.token(new Jwts.Key("k1", Jwts.key("x").pair()), Map.of(), claims());
    assertEquals(Reason.INVALID_SIGNATURE, refusal(verifier, otherKey));
    String shortSignature = parts[0] + "." + parts[1] + ".AAAA";
    assertEquals(Reason.INVALID_SIGNATURE, refusal(verifier, shortSignature));
  }

  @Test
  void cachesTheKeys() {
    serveKeys(key1);
    TokenVerifier verifier = verifier();
    for (int i = 0; i < 5; i++) {
      verifier.verify(Jwts.token(key1, Map.of(), claims()));
    }
    assertEquals(1, server.requests().size());
  }

  @Test
  void refetchesForAKeyRotatedInButAtMostOncePerCooldown() {
    serveKeys(key1);
    TokenVerifier verifier = verifier();
    verifier.verify(Jwts.token(key1, Map.of(), claims()));
    clock.advance(Duration.ofSeconds(31));
    serveKeys(key1, key2);
    verifier.verify(Jwts.token(key2, Map.of(), claims()));
    assertEquals(2, server.requests().size());

    Jwts.Key unknown = Jwts.key("made-up");
    assertEquals(Reason.UNKNOWN_KEY, refusal(verifier, Jwts.token(unknown, Map.of(), claims())));
    assertEquals(Reason.UNKNOWN_KEY, refusal(verifier, Jwts.token(unknown, Map.of(), claims())));
    assertEquals(2, server.requests().size(), "an unknown kid within the cooldown fetches nothing");

    clock.advance(Duration.ofSeconds(31));
    serveKeys(key1, key2);
    assertEquals(Reason.UNKNOWN_KEY, refusal(verifier, Jwts.token(unknown, Map.of(), claims())));
    assertEquals(3, server.requests().size());
  }

  @Test
  void refetchesWhenTheCacheExpiresAndForgetsRetiredKeys() {
    serveKeys(key1, key2);
    TokenVerifier verifier = verifier();
    verifier.verify(Jwts.token(key1, Map.of(), claims()));
    clock.advance(Duration.ofMinutes(6));
    serveKeys(key2);
    Map<String, Object> claims = claims();
    claims.put("exp", clock.now.plusSeconds(600).getEpochSecond());
    verifier.verify(Jwts.token(key2, Map.of(), claims));
    assertEquals(2, server.requests().size());
    assertEquals(Reason.UNKNOWN_KEY, refusal(verifier, Jwts.token(key1, Map.of(), claims)));
  }

  @Test
  void keepsVerifyingWithStaleKeysThroughAnOutage() {
    serveKeys(key1);
    TokenVerifier verifier = verifier();
    verifier.verify(Jwts.token(key1, Map.of(), claims()));
    clock.advance(Duration.ofMinutes(6));
    server.enqueue(503, "{}");
    Map<String, Object> claims = claims();
    claims.put("exp", clock.now.plusSeconds(600).getEpochSecond());
    verifier.verify(Jwts.token(key1, Map.of(), claims));
    verifier.verify(Jwts.token(key1, Map.of(), claims));
    assertEquals(2, server.requests().size(), "a failed refresh is not retried on every token");
  }

  @Test
  void anIssuerThatNeverAnswersIsUnavailableAndIsNotHammered() {
    server.enqueue(500, "{}");
    TokenVerifier verifier = verifier();
    assertEquals(Reason.JWKS_UNAVAILABLE, refusal(verifier, Jwts.token(key1, Map.of(), claims())));
    assertEquals(Reason.JWKS_UNAVAILABLE, refusal(verifier, Jwts.token(key1, Map.of(), claims())));
    assertEquals(1, server.requests().size());
    clock.advance(Duration.ofSeconds(31));
    server.enqueue(200, "not json");
    assertEquals(Reason.JWKS_UNAVAILABLE, refusal(verifier, Jwts.token(key1, Map.of(), claims())));
    clock.advance(Duration.ofSeconds(31));
    serveKeys(key1);
    verifier.verify(Jwts.token(key1, Map.of(), claims()));
  }

  @Test
  void skipsKeysItCannotUse() {
    Map<String, Object> rsa = new LinkedHashMap<>(Jwts.jwk(key2));
    rsa.put("kty", "RSA");
    Map<String, Object> enc = new LinkedHashMap<>(Jwts.jwk(Jwts.key("enc")));
    enc.put("use", "enc");
    Map<String, Object> broken = new LinkedHashMap<>(Jwts.jwk(Jwts.key("broken")));
    broken.put("x", "AAAA");
    server.enqueue(200, Jwts.jwks(List.of(rsa, enc, broken, Jwts.jwk(key1))));
    TokenVerifier verifier = verifier();
    verifier.verify(Jwts.token(key1, Map.of(), claims()));
    assertEquals(Reason.UNKNOWN_KEY, refusal(verifier, Jwts.token(key2, Map.of(), claims())));
  }

  @Test
  void needsAnIssuerAnAudienceAndAKeySource() {
    assertThrows(
        IllegalStateException.class,
        () -> TokenVerifier.builder().audience(AUDIENCE).jwksUri(ISSUER).build());
    assertThrows(
        IllegalStateException.class,
        () -> TokenVerifier.builder().issuer(ISSUER).jwksUri(ISSUER).build());
    assertThrows(
        IllegalStateException.class,
        () -> TokenVerifier.builder().issuer(ISSUER).audience(AUDIENCE).build());
    assertThrows(
        IllegalArgumentException.class,
        () -> TokenVerifier.builder().jwksUri("http://api.id.qeet.in/jwks"));
    assertThrows(IllegalArgumentException.class, () -> TokenVerifier.builder().audience(" "));
    assertThrows(
        IllegalArgumentException.class,
        () -> TokenVerifier.builder().clockSkew(Duration.ofSeconds(-1)));
  }

  @Test
  void discoversTheIssuerAndKeys() {
    server.enqueue(
        200,
        "{\"issuer\":\""
            + ISSUER
            + "\",\"jwks_uri\":\""
            + server.url()
            + "/.well-known/jwks.json\","
            + "\"token_endpoint\":\""
            + ISSUER
            + "/v1/oauth/token-code\",\"grant_types_supported\":[\"authorization_code\"],\"x_new\":1}");
    DiscoveryDocument document = Discovery.fetch(server.url() + "/");
    assertEquals("/.well-known/openid-configuration", server.onlyRequest().path());
    assertEquals(List.of("authorization_code"), document.grantTypesSupported());
    assertEquals(List.of(), document.scopesSupported());
    TokenVerifier verifier =
        TokenVerifier.builder().discovery(document).audience(AUDIENCE).clock(clock).build();
    assertEquals(ISSUER, verifier.issuer());
    serveKeys(key1);
    verifier.verify(Jwts.token(key1, Map.of(), claims()));
  }

  @Test
  void aDiscoveryFailureIsTyped() {
    server.enqueue(404, "{}").enqueue(200, "{\"no\":\"issuer\"}");
    assertEquals(
        "http_404", assertThrows(ApiException.class, () -> Discovery.fetch(server.url())).code());
    assertEquals(
        "invalid_response",
        assertThrows(ApiException.class, () -> Discovery.fetch(server.url())).code());
    server.close();
    assertThrows(NetworkException.class, () -> Discovery.fetch(server.url()));
  }
}
