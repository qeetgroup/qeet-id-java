package com.qeetgroup.qeetid.webhook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qeetgroup.qeetid.exception.WebhookVerificationException;
import com.qeetgroup.qeetid.exception.WebhookVerificationException.Reason;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/** Verifies deliveries made by the server's own signing code (src/test/resources/webhook). */
class WebhookVerifierTest {
  record Delivery(String name, List<String> secrets, Map<String, String> headers, byte[] body) {
    long timestamp() {
      return Long.parseLong(headers.get("X-Qeet-Timestamp"));
    }

    Function<String, String> lookup() {
      return name ->
          headers.entrySet().stream()
              .filter(e -> e.getKey().equalsIgnoreCase(name))
              .map(Map.Entry::getValue)
              .findFirst()
              .orElse(null);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  static List<Delivery> deliveries() throws IOException {
    try (InputStream in =
        WebhookVerifierTest.class.getResourceAsStream("/webhook/deliveries.json")) {
      JsonNode root = new ObjectMapper().readTree(in);
      List<Delivery> out = new ArrayList<>();
      for (JsonNode d : root.get("deliveries")) {
        List<String> secrets = new ArrayList<>();
        d.get("signed_with").forEach(s -> secrets.add(s.asText()));
        Map<String, String> headers = new LinkedHashMap<>();
        d.get("headers").properties().forEach(e -> headers.put(e.getKey(), e.getValue().asText()));
        out.add(
            new Delivery(
                d.get("name").asText(),
                secrets,
                headers,
                d.get("body").asText().getBytes(StandardCharsets.UTF_8)));
      }
      return out;
    }
  }

  private static WebhookVerifier verifier(Delivery delivery, List<String> secrets) {
    WebhookVerifier.Builder builder =
        WebhookVerifier.builder()
            .clock(Clock.fixed(Instant.ofEpochSecond(delivery.timestamp() + 30), ZoneOffset.UTC));
    secrets.forEach(builder::secret);
    return builder.build();
  }

  private static Delivery first() throws IOException {
    return deliveries().get(0);
  }

  @ParameterizedTest
  @MethodSource("deliveries")
  void acceptsEveryDeliveryTheServerSigned(Delivery delivery) {
    WebhookEvent event =
        verifier(delivery, delivery.secrets()).verify(delivery.body(), delivery.lookup());
    assertEquals(delivery.headers().get("X-Qeet-Event"), event.type());
    assertEquals("7d2f9a1c-3b4e-4c5d-8e6f-1a2b3c4d5e6f", event.tenantId());
    assertEquals(Instant.parse("2026-11-05T09:30:00Z"), event.occurredAt());
  }

  @ParameterizedTest
  @MethodSource("deliveries")
  void acceptsARotatedDeliveryWithEitherSecretAlone(Delivery delivery) {
    for (String secret : delivery.secrets()) {
      assertDoesNotThrow(
          () -> verifier(delivery, List.of(secret)).verify(delivery.body(), delivery.lookup()));
    }
  }

  @ParameterizedTest
  @MethodSource("deliveries")
  void refusesAChangedByte(Delivery delivery) {
    byte[] tampered = delivery.body().clone();
    tampered[tampered.length - 3] ^= 1;
    WebhookVerificationException error =
        assertThrows(
            WebhookVerificationException.class,
            () -> verifier(delivery, delivery.secrets()).verify(tampered, delivery.lookup()));
    assertEquals(Reason.SIGNATURE_MISMATCH, error.reason());
    assertEquals("signature_mismatch", error.code());
  }

  @Test
  void refusesAnotherSecret() throws IOException {
    Delivery delivery = first();
    WebhookVerificationException error =
        assertThrows(
            WebhookVerificationException.class,
            () ->
                verifier(delivery, List.of("not-the-secret"))
                    .verify(delivery.body(), delivery.lookup()));
    assertEquals(Reason.SIGNATURE_MISMATCH, error.reason());
    assertFalse(error.getMessage().contains("not-the-secret"));
  }

  @Test
  void acceptsTheRightSecretAmongWrongOnes() throws IOException {
    Delivery delivery = first();
    List<String> secrets = List.of("old-one", delivery.secrets().get(0));
    assertDoesNotThrow(
        () -> verifier(delivery, secrets).verify(delivery.body(), delivery.lookup()));
  }

  @Test
  void refusesAReplayOutsideTheTolerance() throws IOException {
    Delivery delivery = first();
    for (long offset : new long[] {-301, 301, 3600}) {
      WebhookVerifier verifier =
          WebhookVerifier.builder()
              .secret(delivery.secrets().get(0))
              .clock(
                  Clock.fixed(Instant.ofEpochSecond(delivery.timestamp() + offset), ZoneOffset.UTC))
              .build();
      WebhookVerificationException error =
          assertThrows(
              WebhookVerificationException.class,
              () -> verifier.verify(delivery.body(), delivery.lookup()));
      assertEquals(Reason.TIMESTAMP_OUTSIDE_TOLERANCE, error.reason(), "offset " + offset);
    }
    WebhookVerifier wide =
        WebhookVerifier.builder()
            .secret(delivery.secrets().get(0))
            .tolerance(Duration.ofHours(2))
            .clock(Clock.fixed(Instant.ofEpochSecond(delivery.timestamp() + 3600), ZoneOffset.UTC))
            .build();
    assertDoesNotThrow(() -> wide.verify(delivery.body(), delivery.lookup()));
  }

  @Test
  void aSignatureCannotBeMovedToAnotherTimestamp() throws IOException {
    Delivery delivery = first();
    Map<String, String> headers = new LinkedHashMap<>(delivery.headers());
    headers.put("X-Qeet-Timestamp", String.valueOf(delivery.timestamp() + 10));
    WebhookVerificationException error =
        assertThrows(
            WebhookVerificationException.class,
            () -> verifier(delivery, delivery.secrets()).verify(delivery.body(), toMap(headers)));
    assertEquals(Reason.SIGNATURE_MISMATCH, error.reason());
  }

  static Stream<Object[]> badHeaders() {
    return Stream.of(
        new Object[] {"X-Qeet-Timestamp", null, Reason.MISSING_TIMESTAMP},
        new Object[] {"X-Qeet-Timestamp", " ", Reason.MISSING_TIMESTAMP},
        new Object[] {"X-Qeet-Timestamp", "12.5", Reason.INVALID_TIMESTAMP},
        new Object[] {"X-Qeet-Timestamp", "-1", Reason.INVALID_TIMESTAMP},
        new Object[] {"X-Qeet-Signature", null, Reason.MISSING_SIGNATURE},
        new Object[] {"X-Qeet-Signature", "v1=abc", Reason.INVALID_SIGNATURE_HEADER},
        new Object[] {"X-Qeet-Signature", "sha256=nothex", Reason.INVALID_SIGNATURE_HEADER},
        new Object[] {"X-Qeet-Signature", "sha256=" + "0".repeat(64), Reason.SIGNATURE_MISMATCH});
  }

  @ParameterizedTest
  @MethodSource("badHeaders")
  void refusesMissingOrMalformedHeaders(String name, String value, Reason expected)
      throws IOException {
    Delivery delivery = first();
    Map<String, String> headers = new LinkedHashMap<>(delivery.headers());
    if (value == null) {
      headers.remove(name);
    } else {
      headers.put(name, value);
    }
    WebhookVerificationException error =
        assertThrows(
            WebhookVerificationException.class,
            () -> verifier(delivery, delivery.secrets()).verify(delivery.body(), toMap(headers)));
    assertEquals(expected, error.reason());
  }

  @Test
  void ignoresSignatureSchemesItDoesNotKnow() throws IOException {
    Delivery delivery = first();
    Map<String, String> headers = new LinkedHashMap<>(delivery.headers());
    headers.put("X-Qeet-Signature", "v2=whatever, " + delivery.headers().get("X-Qeet-Signature"));
    assertDoesNotThrow(
        () -> verifier(delivery, delivery.secrets()).verify(delivery.body(), toMap(headers)));
  }

  @Test
  void readsHeadersWhateverTheirCase() throws IOException {
    Delivery delivery = first();
    Map<String, List<String>> lower = new LinkedHashMap<>();
    delivery
        .headers()
        .forEach((k, v) -> lower.put(k.toLowerCase(java.util.Locale.ROOT), List.of(v)));
    assertDoesNotThrow(() -> verifier(delivery, delivery.secrets()).verify(delivery.body(), lower));
  }

  @Test
  void returnsTheEventAndItsData() throws IOException {
    Delivery delivery = deliveries().get(2);
    WebhookEvent event =
        verifier(delivery, delivery.secrets())
            .verify(new String(delivery.body(), StandardCharsets.UTF_8), delivery.lookup());
    assertEquals("7c8d9e0f-1a2b-4c3d-8e4f-5a6b7c8d9e0f", event.id());
    assertEquals(WebhookEventTypes.SESSION_REVOKED, event.type());
    assertEquals("admin <revoked> & signed out", event.data().get("reason"));
    assertEquals("Aarav Shāh 🙂", event.data().get("display_name"));
    record Revoked(@JsonProperty("session_id") String sessionId, String reason) {}
    Revoked revoked = event.dataAs(Revoked.class);
    assertEquals("3f0d9a6c-0d5e-4f2c-9a7b-6e1c9d5a3b84", revoked.sessionId());
    assertFalse(event.isTest());
    assertTrue(
        verifier(deliveries().get(1), deliveries().get(1).secrets())
            .verify(deliveries().get(1).body(), deliveries().get(1).lookup())
            .isTest());
  }

  @Test
  void refusesAnAuthenticBodyThatIsNotAnEvent() {
    Instant now = Instant.parse("2026-11-05T10:00:00Z");
    WebhookVerifier verifier =
        WebhookVerifier.builder().secret("s3cret").clock(Clock.fixed(now, ZoneOffset.UTC)).build();
    for (String body :
        new String[] {
          "not json",
          "[1,2]",
          "{\"id\":\"e\",\"type\":\"user.created\",\"tenant_id\":\"t\",\"occurred_at\":\"2026-11-05T10:00:00Z\"}",
          "{\"id\":\"e\",\"type\":\"user.created\",\"tenant_id\":\"t\",\"occurred_at\":\"yesterday\",\"data\":{}}"
        }) {
      byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
      WebhookVerifier.Signature signature = WebhookVerifier.sign(bytes, "s3cret", now);
      WebhookVerificationException error =
          assertThrows(
              WebhookVerificationException.class,
              () ->
                  verifier.verify(
                      bytes,
                      toMap(
                          Map.of(
                              "X-Qeet-Timestamp",
                              signature.timestamp(),
                              "X-Qeet-Signature",
                              signature.signature()))));
      assertEquals(Reason.INVALID_PAYLOAD, error.reason(), body);
    }
  }

  @Test
  void signsTheWayTheServerDoes() throws IOException {
    for (Delivery delivery : deliveries()) {
      WebhookVerifier.Signature signature =
          WebhookVerifier.sign(
              delivery.body(),
              delivery.secrets().get(0),
              Instant.ofEpochSecond(delivery.timestamp()));
      assertEquals(delivery.headers().get("X-Qeet-Timestamp"), signature.timestamp());
      assertTrue(
          delivery.headers().get("X-Qeet-Signature").startsWith(signature.signature()),
          delivery.name());
    }
  }

  @Test
  void needsASecret() {
    assertThrows(IllegalStateException.class, () -> WebhookVerifier.builder().build());
    assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.builder().secret(""));
    assertThrows(
        IllegalArgumentException.class, () -> WebhookVerifier.builder().tolerance(Duration.ZERO));
    assertFalse(
        WebhookVerifier.builder()
            .secret("abc123secret")
            .build()
            .toString()
            .contains("abc123secret"));
  }

  @Test
  void listsTheCatalogue() {
    assertEquals(46, WebhookEventTypes.ALL.size());
    assertTrue(WebhookEventTypes.ALL.contains("user.created"));
    assertFalse(WebhookEventTypes.ALL.contains(WebhookEventTypes.TEST_PING));
  }

  private static Map<String, List<String>> toMap(Map<String, String> headers) {
    Map<String, List<String>> out = new LinkedHashMap<>();
    headers.forEach((k, v) -> out.put(k, List.of(v)));
    return out;
  }
}
