package com.qeetgroup.qeetid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class QeetIDTest {
  @ParameterizedTest
  @CsvSource({
    "https://api.id.qeet.in, https://api.id.qeet.in",
    "https://api.id.qeet.in/, https://api.id.qeet.in",
    "https://eu.example.com/qeet//, https://eu.example.com/qeet",
    "http://localhost:8080, http://localhost:8080",
    "http://api.dev.id.qeet.localhost, http://api.dev.id.qeet.localhost",
    "http://127.0.0.1:9000, http://127.0.0.1:9000",
    "http://[::1]:9000, http://[::1]:9000",
  })
  void acceptsHttpsAndLoopbackHttp(String given, String normalised) {
    assertEquals(normalised, QeetID.builder().baseUrl(given).build().baseUrl());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "http://api.id.qeet.in",
        "ftp://api.id.qeet.in",
        "api.id.qeet.in",
        "https://user:pass@api.id.qeet.in",
        "https://api.id.qeet.in?x=1",
        "https://api.id.qeet.in#f",
        "http://localhost.evil.com",
        "not a url"
      })
  void refusesAnythingElse(String given) {
    assertThrows(IllegalArgumentException.class, () -> QeetID.builder().baseUrl(given).build());
  }

  @Test
  void defaultsToProduction() {
    assertEquals(QeetID.DEFAULT_BASE_URL, QeetID.builder().build().baseUrl());
  }

  @Test
  void takesOneCredential() {
    assertThrows(
        IllegalStateException.class,
        () -> QeetID.builder().apiKey("qk_1").clientCredentials("svc", "secret").build());
    assertThrows(IllegalArgumentException.class, () -> QeetID.builder().apiKey(" "));
    assertThrows(
        IllegalArgumentException.class, () -> QeetID.builder().clientCredentials("svc", ""));
  }

  @Test
  void refusesNonsenseSettings() {
    QeetID.Builder builder = QeetID.builder();
    assertThrows(IllegalArgumentException.class, () -> builder.timeout(Duration.ZERO));
    assertThrows(IllegalArgumentException.class, () -> builder.maxRetries(-1));
    assertThrows(
        IllegalArgumentException.class,
        () -> builder.retryBackoff(Duration.ofSeconds(2), Duration.ofSeconds(1)));
    assertThrows(IllegalArgumentException.class, () -> builder.header("Authorization", "x"));
    assertThrows(IllegalArgumentException.class, () -> builder.header("Idempotency-Key", "x"));
    assertThrows(IllegalArgumentException.class, () -> builder.header("X-Ok", "a\r\nInjected: 1"));
    assertThrows(IllegalArgumentException.class, () -> builder.appInfo("my app", null));
  }

  @Test
  void neverPrintsACredential() {
    String withKey = QeetID.builder().apiKey("qk_live_supersecret").build().toString();
    assertFalse(withKey.contains("supersecret"), withKey);
    assertTrue(withKey.contains("apiKey=[redacted]"), withKey);
    String withClient =
        QeetID.builder().clientCredentials("svc_1", "cs_supersecret").build().toString();
    assertFalse(withClient.contains("supersecret"), withClient);
    assertTrue(withClient.contains("svc_1"), withClient);
  }
}
