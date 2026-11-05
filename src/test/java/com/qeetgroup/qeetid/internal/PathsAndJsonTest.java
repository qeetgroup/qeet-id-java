package com.qeetgroup.qeetid.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class PathsAndJsonTest {
  @Test
  void encodesEachValueAsOneSegment() {
    assertEquals("/v1/users/u%2F1/sessions", Paths.of("/v1/users/{userId}/sessions", "u/1"));
    assertEquals("/v1/a/x%20y%3F%23/b/%C3%A9", Paths.of("/v1/a/{a}/b/{b}", "x y?#", "é"));
    assertEquals("/v1/vault/db.password-1_~", Paths.of("/v1/vault/{name}", "db.password-1_~"));
  }

  @Test
  void refusesBlankAndDotSegments() {
    for (String bad : new String[] {"", " ", ".", ".."}) {
      IllegalArgumentException error =
          assertThrows(IllegalArgumentException.class, () -> Paths.of("/v1/users/{userId}", bad));
      assertTrue(error.getMessage().startsWith("userId"));
    }
    assertThrows(
        IllegalArgumentException.class, () -> Paths.of("/v1/users/{userId}", (String) null));
  }

  @Test
  void refusesAMismatchedTemplate() {
    assertThrows(IllegalStateException.class, () -> Paths.of("/v1/{a}/{b}", "x"));
    assertThrows(IllegalStateException.class, () -> Paths.of("/v1/{a}", "x", "y"));
    assertThrows(IllegalStateException.class, () -> Paths.of("/v1/{a", "x"));
  }

  record Sample(
      String id,
      @JsonProperty("display_name") @Nullable String displayName,
      List<String> roles,
      Map<String, Object> metadata,
      @JsonProperty("created_at") Instant createdAt,
      @JsonProperty("seen_at") @Nullable Instant seenAt) {}

  @Test
  void ignoresUnknownFieldsAndReadsAbsentCollectionsAsEmpty() {
    Sample sample =
        Json.read(
            "{\"id\":\"u1\",\"new_field\":1,\"roles\":null,\"created_at\":\"2026-11-05T15:30:00+05:30\"}"
                .getBytes(StandardCharsets.UTF_8),
            Json.type(Sample.class));
    assertEquals("u1", sample.id());
    assertNull(sample.displayName());
    assertEquals(List.of(), sample.roles());
    assertEquals(Map.of(), sample.metadata());
    assertEquals(Instant.parse("2026-11-05T10:00:00Z"), sample.createdAt());
    assertNull(sample.seenAt());
  }

  @Test
  void writesInstantsAsRfc3339AndLeavesNullsOut() throws Exception {
    Sample sample =
        new Sample("u1", null, List.of("a"), Map.of(), Instant.parse("2026-11-05T10:00:00Z"), null);
    assertEquals(
        Json.mapper()
            .readTree(
                "{\"id\":\"u1\",\"roles\":[\"a\"],\"metadata\":{},\"created_at\":\"2026-11-05T10:00:00Z\"}"),
        Json.mapper().readTree(Json.write(sample)));
  }

  @Test
  void refusesATimeThatIsNotRfc3339() {
    assertThrows(
        java.io.UncheckedIOException.class,
        () ->
            Json.read(
                "{\"id\":\"u\",\"created_at\":\"yesterday\"}".getBytes(StandardCharsets.UTF_8),
                Json.type(Sample.class)));
    assertThrows(
        java.io.UncheckedIOException.class,
        () ->
            Json.read(
                "{\"id\":\"u\",\"created_at\":5}".getBytes(StandardCharsets.UTF_8),
                Json.type(Sample.class)));
  }

  @Test
  void theVersionIsWrittenAtBuildTime() {
    assertTrue(Version.VERSION.matches("\\d+\\.\\d+\\.\\d+.*"), Version.VERSION);
    assertTrue(
        UserAgent.build("acme", "1.0")
            .startsWith("acme/1.0 qeet-id-java/" + Version.VERSION + " java/"));
    assertTrue(UserAgent.build(null, null).startsWith("qeet-id-java/"));
  }

  @Test
  void runsOnTheRequestedJdk() {
    String wanted = System.getProperty("expectedJavaFeature");
    if (wanted != null) {
      assertEquals(wanted, String.valueOf(Runtime.version().feature()));
    }
  }
}
