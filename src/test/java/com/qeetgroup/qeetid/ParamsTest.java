package com.qeetgroup.qeetid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class ParamsTest {
  static final class SampleParams extends Params {
    private SampleParams(Builder builder) {
      super(builder);
    }

    static Builder builder() {
      return new Builder();
    }

    static final class Builder extends Params.Builder<SampleParams, Builder> {
      Builder name(String name) {
        return set("name", name);
      }

      Builder displayName(@Nullable String displayName) {
        return set("display_name", displayName);
      }

      Builder password(String password) {
        return setSecret("password", password);
      }

      Builder tags(List<String> tags) {
        return set("tags", tags);
      }

      @Override
      public SampleParams build() {
        require("name", "name");
        return new SampleParams(this);
      }
    }
  }

  static final class OtherParams extends Params {
    private OtherParams(Builder builder) {
      super(builder);
    }

    static final class Builder extends Params.Builder<OtherParams, Builder> {
      Builder name(String name) {
        return set("name", name);
      }

      @Override
      public OtherParams build() {
        return new OtherParams(this);
      }
    }
  }

  @Test
  void keepsTheFieldsSetInOrderWithExplicitNulls() {
    SampleParams params = SampleParams.builder().name("a").displayName(null).build();
    Map<String, Object> expected = new LinkedHashMap<>();
    expected.put("name", "a");
    expected.put("display_name", null);
    assertEquals(expected, params.toMap());
    assertEquals(List.of("name", "display_name"), List.copyOf(params.toMap().keySet()));
  }

  @Test
  void requiresRequiredFields() {
    IllegalStateException error =
        assertThrows(IllegalStateException.class, () -> SampleParams.builder().build());
    assertEquals("name is required.", error.getMessage());
  }

  @Test
  void copiesCollectionsSoLaterChangesDontLeakIn() {
    List<String> tags = new ArrayList<>(List.of("a"));
    SampleParams params = SampleParams.builder().name("n").tags(tags).build();
    tags.add("b");
    assertEquals(List.of("a"), params.toMap().get("tags"));
    assertThrows(UnsupportedOperationException.class, () -> params.toMap().put("x", 1));
  }

  @Test
  void redactsSecretsInToString() {
    String text = SampleParams.builder().name("n").password("hunter2").build().toString();
    assertEquals("SampleParams{name=n, password=[redacted]}", text);
    assertFalse(text.contains("hunter2"));
  }

  @Test
  void equalityIsByTypeAndFields() {
    assertEquals(
        SampleParams.builder().name("a").build(), SampleParams.builder().name("a").build());
    assertEquals(
        SampleParams.builder().name("a").build().hashCode(),
        SampleParams.builder().name("a").build().hashCode());
    assertNotEquals(
        SampleParams.builder().name("a").build(), new OtherParams.Builder().name("a").build());
  }

  @Test
  void requestOptionsCheckWhatTheyAreGiven() {
    RequestOptions options =
        RequestOptions.builder()
            .timeout(Duration.ofSeconds(3))
            .maxRetries(1)
            .header("traceparent", "x")
            .idempotencyKey("abc-123")
            .build();
    assertEquals(Duration.ofSeconds(3), options.timeout());
    assertEquals(1, options.maxRetries());
    assertEquals(Map.of("traceparent", "x"), options.headers());
    assertEquals("abc-123", options.idempotencyKey());
    assertTrue(options.toString().contains("traceparent"));
    assertThrows(IllegalArgumentException.class, () -> RequestOptions.builder().idempotencyKey(""));
    assertThrows(
        IllegalArgumentException.class, () -> RequestOptions.builder().idempotencyKey("has space"));
    assertThrows(
        IllegalArgumentException.class,
        () -> RequestOptions.builder().idempotencyKey("x".repeat(256)));
    assertThrows(
        IllegalArgumentException.class, () -> RequestOptions.builder().header("User-Agent", "x"));
    assertThrows(IllegalArgumentException.class, () -> RequestOptions.builder().header(" ", "x"));
    assertThrows(
        IllegalArgumentException.class,
        () -> RequestOptions.builder().timeout(Duration.ofSeconds(-1)));
    assertThrows(IllegalArgumentException.class, () -> RequestOptions.builder().maxRetries(-1));
  }

  @Test
  void listResultsAreIterable() {
    ListResult<String> list = new ListResult<>(List.of("a", "b"), false);
    List<String> seen = new ArrayList<>();
    list.forEach(seen::add);
    assertEquals(List.of("a", "b"), seen);
    assertEquals(2, list.stream().count());
  }
}
