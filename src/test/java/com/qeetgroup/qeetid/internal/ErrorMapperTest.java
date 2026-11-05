package com.qeetgroup.qeetid.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qeetgroup.qeetid.exception.ApiException;
import com.qeetgroup.qeetid.exception.AuthenticationException;
import com.qeetgroup.qeetid.exception.BadRequestException;
import com.qeetgroup.qeetid.exception.ConflictException;
import com.qeetgroup.qeetid.exception.FieldError;
import com.qeetgroup.qeetid.exception.InternalServerException;
import com.qeetgroup.qeetid.exception.NotFoundException;
import com.qeetgroup.qeetid.exception.OAuthException;
import com.qeetgroup.qeetid.exception.PaymentRequiredException;
import com.qeetgroup.qeetid.exception.PermissionDeniedException;
import com.qeetgroup.qeetid.exception.RateLimitException;
import com.qeetgroup.qeetid.exception.ServiceUnavailableException;
import com.qeetgroup.qeetid.exception.SessionRevokedException;
import com.qeetgroup.qeetid.exception.ValidationException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ErrorMapperTest {
  private static final Clock CLOCK =
      Clock.fixed(Instant.parse("2026-11-05T10:00:00Z"), ZoneOffset.UTC);

  private static ApiException map(int status, String body, Map<String, List<String>> headers) {
    return ErrorMapper.fromResponse(status, headers, body.getBytes(StandardCharsets.UTF_8), CLOCK);
  }

  private static ApiException map(int status, String body) {
    return map(status, body, Map.of());
  }

  static Stream<Arguments> statuses() {
    return Stream.of(
        Arguments.of(400, BadRequestException.class),
        Arguments.of(401, AuthenticationException.class),
        Arguments.of(402, PaymentRequiredException.class),
        Arguments.of(403, PermissionDeniedException.class),
        Arguments.of(404, NotFoundException.class),
        Arguments.of(405, ApiException.class),
        Arguments.of(409, ConflictException.class),
        Arguments.of(413, ApiException.class),
        Arguments.of(422, ValidationException.class),
        Arguments.of(429, RateLimitException.class),
        Arguments.of(500, InternalServerException.class),
        Arguments.of(502, InternalServerException.class),
        Arguments.of(503, ServiceUnavailableException.class),
        Arguments.of(504, InternalServerException.class));
  }

  @ParameterizedTest
  @MethodSource("statuses")
  void mapsEachStatusToItsClass(int status, Class<? extends ApiException> expected) {
    ApiException error = map(status, "{\"error\":{\"code\":\"some.code\",\"message\":\"Nope.\"}}");
    assertSame(expected, error.getClass());
    assertEquals(status, error.status());
    assertEquals("some.code", error.code());
  }

  @Test
  void aRevokedSessionHasItsOwnClass() {
    ApiException error =
        map(
            401,
            "{\"error\":{\"code\":\"auth.session_revoked\",\"message\":\"Signed out.\",\"retryable\":true}}");
    assertInstanceOf(SessionRevokedException.class, error);
    assertInstanceOf(AuthenticationException.class, error);
    assertTrue(error.retryable());
  }

  @Test
  void readsTheWholeEnvelope() {
    ApiException error =
        map(
            422,
            "{\"error\":{\"code\":\"validation_failed\",\"message\":\"Check the fields.\",\"detail\":\"email\","
                + "\"fields\":[{\"field\":\"email\",\"code\":\"invalid_email\",\"message\":\"Must be a valid email address.\"},{\"field\":\"x\"}],"
                + "\"metadata\":{\"remaining_attempts\":2},\"retryable\":false,\"request_id\":\"req_body\",\"translation_key\":\"k\"}}",
            Map.of("X-Request-Id", List.of("req_header")));
    ValidationException validation = assertInstanceOf(ValidationException.class, error);
    assertEquals("Check the fields.", validation.apiError().message());
    assertEquals("email", validation.detail());
    assertEquals(
        List.of(
            new FieldError("email", "invalid_email", "Must be a valid email address."),
            new FieldError("x", "invalid", "")),
        validation.fields());
    assertEquals(2, validation.metadata().get("remaining_attempts"));
    assertEquals("req_header", validation.requestId(), "the header wins over the body");
    assertEquals(
        "422 validation_failed: Check the fields. (request req_header)", validation.getMessage());
    assertEquals(List.of("req_header"), validation.headers().get("x-request-id"));
  }

  @Test
  void fallsBackToTheBodysRequestId() {
    assertEquals(
        "req_body",
        map(
                404,
                "{\"error\":{\"code\":\"not_found\",\"message\":\"m\",\"request_id\":\"req_body\"}}")
            .requestId());
  }

  @Test
  void anAnswerWithoutAnEnvelopeStillHasACodeAndMessage() {
    ApiException error = map(502, "<html>Bad gateway</html>");
    assertEquals("http_502", error.code());
    assertEquals("The request failed with HTTP status 502.", error.apiError().message());
    assertTrue(error.fields().isEmpty());
    assertTrue(error.metadata().isEmpty());
    assertNull(error.requestId());
    assertEquals("http_404", map(404, "").code());
  }

  @Test
  void readsAnRfc6749Error() {
    OAuthException error =
        assertInstanceOf(
            OAuthException.class,
            map(
                400,
                "{\"error\":\"invalid_grant\",\"error_description\":\"The code was used.\",\"error_uri\":\"https://x\"}",
                Map.of("x-request-id", List.of("req_o"))));
    assertEquals("invalid_grant", error.error());
    assertEquals("invalid_grant", error.code());
    assertEquals("The code was used.", error.errorDescription());
    assertEquals("https://x", error.errorUri());
    assertEquals("req_o", error.requestId());
    assertEquals(400, error.status());
  }

  @Test
  void anRfc6749ErrorWithoutADescriptionUsesTheCode() {
    OAuthException error =
        assertInstanceOf(OAuthException.class, map(400, "{\"error\":\"slow_down\"}"));
    assertEquals("slow_down", error.apiError().message());
    assertNull(error.errorDescription());
  }

  @Test
  void readsRetryAfterAsSecondsADateOrMetadata() {
    assertEquals(
        Duration.ofSeconds(7), map(429, "{}", Map.of("Retry-After", List.of("7"))).retryAfter());
    assertEquals(
        Duration.ofSeconds(30),
        map(503, "{}", Map.of("retry-after", List.of("Thu, 05 Nov 2026 10:00:30 GMT")))
            .retryAfter());
    assertEquals(
        Duration.ZERO,
        map(503, "{}", Map.of("retry-after", List.of("Thu, 05 Nov 2026 09:00:00 GMT")))
            .retryAfter());
    assertNull(map(503, "{}", Map.of("retry-after", List.of("soon"))).retryAfter());
    assertEquals(
        Duration.ofDays(1),
        map(503, "{}", Map.of("retry-after", List.of("99999999999999999999"))).retryAfter());
    assertEquals(
        Duration.ofSeconds(3),
        map(
                503,
                "{\"error\":{\"code\":\"server.overloaded\",\"message\":\"m\",\"metadata\":{\"retry_after\":3}}}")
            .retryAfter());
    assertNull(map(500, "{}").retryAfter());
  }

  @Test
  void anErrorNeverCarriesTheRequestCredential() {
    ApiException error =
        map(401, "{\"error\":{\"code\":\"unauthorized\",\"message\":\"Bad key.\"}}");
    assertFalse(error.toString().contains("qk_"));
  }
}
