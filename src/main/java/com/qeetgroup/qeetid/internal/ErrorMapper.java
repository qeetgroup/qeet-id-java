package com.qeetgroup.qeetid.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.qeetgroup.qeetid.exception.ApiError;
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
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Turns an error response into the matching {@link ApiException}. */
public final class ErrorMapper {
  /** The code of a 401 for a session that has ended. Never retried. */
  public static final String SESSION_REVOKED = "auth.session_revoked";

  /** A longer {@code Retry-After} is read as this. */
  private static final Duration LONGEST_RETRY_AFTER = Duration.ofDays(1);

  private ErrorMapper() {}

  /**
   * The typed exception for a failed response.
   *
   * @param status the HTTP status
   * @param headers the response headers, names in any case
   * @param body the response body, possibly empty or not JSON
   */
  public static ApiException fromResponse(
      int status, Map<String, List<String>> headers, byte[] body) {
    return fromResponse(status, headers, body, Clock.systemUTC());
  }

  static ApiException fromResponse(
      int status, Map<String, List<String>> rawHeaders, byte[] body, Clock clock) {
    Map<String, List<String>> headers = lowerCase(rawHeaders);
    String headerRequestId = first(headers, "x-request-id");
    Duration retryAfter = parseRetryAfter(first(headers, "retry-after"), clock);
    JsonNode root = parse(body);
    JsonNode error = root == null ? null : root.get("error");

    if (error != null && error.isTextual() && !error.asText().isEmpty()) {
      // RFC 6749 section 5.2: {"error": "invalid_grant", "error_description": "..."}
      String description = text(root, "error_description");
      ApiError details =
          new ApiError(
              status,
              error.asText(),
              description != null ? description : error.asText(),
              headerRequestId,
              null,
              List.of(),
              Map.of(),
              false,
              retryAfter,
              headers);
      return new OAuthException(details, description, text(root, "error_uri"));
    }

    JsonNode envelope = error != null && error.isObject() ? error : null;
    String code = envelope == null ? null : text(envelope, "code");
    String message = envelope == null ? null : text(envelope, "message");
    String requestId = headerRequestId != null ? headerRequestId : text(envelope, "request_id");
    Map<String, Object> metadata = metadata(envelope);
    if (retryAfter == null && metadata.get("retry_after") instanceof Number seconds) {
      double value = seconds.doubleValue();
      if (value > 0 && value < 86_400) {
        retryAfter = Duration.ofMillis(Math.round(value * 1000));
      }
    }
    ApiError details =
        new ApiError(
            status,
            code != null && !code.isEmpty() ? code : "http_" + status,
            message != null && !message.isEmpty()
                ? message
                : "The request failed with HTTP status " + status + ".",
            requestId,
            text(envelope, "detail"),
            fields(envelope),
            metadata,
            envelope != null && envelope.path("retryable").asBoolean(false),
            retryAfter,
            headers);
    return switch (status) {
      case 400 -> new BadRequestException(details);
      case 401 ->
          SESSION_REVOKED.equals(details.code())
              ? new SessionRevokedException(details)
              : new AuthenticationException(details);
      case 402 -> new PaymentRequiredException(details);
      case 403 -> new PermissionDeniedException(details);
      case 404 -> new NotFoundException(details);
      case 409 -> new ConflictException(details);
      case 422 -> new ValidationException(details);
      case 429 -> new RateLimitException(details);
      case 503 -> new ServiceUnavailableException(details);
      default -> status >= 500 ? new InternalServerException(details) : new ApiException(details);
    };
  }

  /**
   * Reads {@code Retry-After}: delta-seconds or an HTTP date. Null when absent or unreadable; a
   * date in the past is zero.
   */
  static @Nullable Duration parseRetryAfter(@Nullable String value, Clock clock) {
    if (value == null || value.isBlank()) {
      return null;
    }
    String trimmed = value.trim();
    if (trimmed.chars().allMatch(Character::isDigit)) {
      try {
        Duration asked = Duration.ofSeconds(Long.parseLong(trimmed));
        return asked.compareTo(LONGEST_RETRY_AFTER) > 0 ? LONGEST_RETRY_AFTER : asked;
      } catch (NumberFormatException e) {
        return LONGEST_RETRY_AFTER;
      }
    }
    try {
      ZonedDateTime at = ZonedDateTime.parse(trimmed, DateTimeFormatter.RFC_1123_DATE_TIME);
      Duration wait = Duration.between(clock.instant(), at.toInstant());
      return wait.isNegative() ? Duration.ZERO : wait;
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  private static @Nullable JsonNode parse(byte[] body) {
    if (body.length == 0) {
      return null;
    }
    try {
      JsonNode node = Json.mapper().readTree(body);
      return node != null && node.isObject() ? node : null;
    } catch (IOException e) {
      return null; // a proxy's HTML page, say: the status still tells the story
    }
  }

  private static @Nullable String text(@Nullable JsonNode node, String field) {
    if (node == null) {
      return null;
    }
    JsonNode value = node.get(field);
    return value != null && value.isTextual() ? value.asText() : null;
  }

  private static List<FieldError> fields(@Nullable JsonNode envelope) {
    JsonNode fields = envelope == null ? null : envelope.get("fields");
    if (fields == null || !fields.isArray()) {
      return List.of();
    }
    List<FieldError> out = new ArrayList<>();
    for (JsonNode field : fields) {
      if (field.isObject()) {
        String name = text(field, "field");
        String code = text(field, "code");
        String message = text(field, "message");
        out.add(
            new FieldError(
                name == null ? "" : name,
                code == null || code.isEmpty() ? "invalid" : code,
                message == null ? "" : message));
      }
    }
    return out;
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> metadata(@Nullable JsonNode envelope) {
    JsonNode metadata = envelope == null ? null : envelope.get("metadata");
    if (metadata == null || !metadata.isObject()) {
      return Map.of();
    }
    return Json.mapper().convertValue(metadata, LinkedHashMap.class);
  }

  private static Map<String, List<String>> lowerCase(Map<String, List<String>> headers) {
    Map<String, List<String>> out = new LinkedHashMap<>();
    headers.forEach((name, values) -> {
      if (name != null && values != null) {
        out.computeIfAbsent(name.toLowerCase(Locale.ROOT), k -> new ArrayList<>()).addAll(values);
      }
    });
    out.replaceAll((name, values) -> List.copyOf(values));
    return out;
  }

  private static @Nullable String first(Map<String, List<String>> headers, String name) {
    List<String> values = headers.get(name);
    return values == null || values.isEmpty() ? null : values.get(0);
  }
}
