package com.qeetgroup.qeetid.internal;

import com.qeetgroup.qeetid.Params;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** One API call, as a resource method describes it. Built and sent at once; not shared. */
public final class ApiRequest {
  final String method;
  final String path;
  final List<Map.Entry<String, String>> query = new ArrayList<>();
  @Nullable Object json;
  @Nullable Map<String, String> form;
  byte @Nullable [] raw;
  @Nullable String contentType;
  boolean authenticate = true;
  boolean retrySafe;
  boolean idempotent;
  boolean neverRetry;

  private ApiRequest(String method, String path) {
    this.method = method;
    this.path = path;
  }

  /** A request with {@code method} to the already encoded {@code path}. */
  public static ApiRequest of(String method, String path) {
    return new ApiRequest(method, path);
  }

  /** A GET. */
  public static ApiRequest get(String path) {
    return new ApiRequest("GET", path);
  }

  /** A POST. */
  public static ApiRequest post(String path) {
    return new ApiRequest("POST", path);
  }

  /** Adds every field of {@code params} to the query string. */
  public ApiRequest query(@Nullable Params params) {
    if (params != null) {
      params.toMap().forEach(this::query);
    }
    return this;
  }

  /** Adds a query parameter; a null value is left out and a collection repeats the name. */
  public ApiRequest query(String name, @Nullable Object value) {
    if (value instanceof Collection<?> values) {
      for (Object item : values) {
        query(name, item);
      }
    } else if (value != null) {
      query.add(new AbstractMap.SimpleImmutableEntry<>(name, queryValue(value)));
    }
    return this;
  }

  /** A copy of this request with the query parameter {@code name} replaced. */
  ApiRequest copyWithQuery(String name, @Nullable String value) {
    ApiRequest copy = new ApiRequest(method, path);
    query.stream().filter(entry -> !entry.getKey().equals(name)).forEach(copy.query::add);
    copy.json = json;
    copy.form = form;
    copy.raw = raw;
    copy.contentType = contentType;
    copy.authenticate = authenticate;
    copy.retrySafe = retrySafe;
    copy.idempotent = idempotent;
    copy.neverRetry = neverRetry;
    return copy.query(name, value);
  }

  /** Sends {@code body} as JSON. */
  public ApiRequest json(@Nullable Object body) {
    this.json = body;
    return this;
  }

  /**
   * Sends {@code fields} as {@code application/x-www-form-urlencoded}; null values are left out.
   */
  public ApiRequest form(Map<String, @Nullable String> fields) {
    Map<String, String> out = new LinkedHashMap<>();
    fields.forEach((name, value) -> {
      if (value != null) {
        out.put(name, value);
      }
    });
    this.form = out;
    return this;
  }

  /** Sends {@code body} as is, with {@code contentType}. */
  public ApiRequest raw(byte[] body, String contentType) {
    this.raw = body.clone();
    this.contentType = contentType;
    return this;
  }

  /** Sends no credential: the endpoint authenticates otherwise, or not at all. */
  public ApiRequest unauthenticated() {
    this.authenticate = false;
    return this;
  }

  /** Marks a POST as safe to repeat: one that changes nothing, such as a check or a preview. */
  public ApiRequest retrySafe() {
    this.retrySafe = true;
    return this;
  }

  /** The route honours an {@code Idempotency-Key}: one is always sent, making it retry-safe. */
  public ApiRequest idempotent() {
    this.idempotent = true;
    return this;
  }

  /** Never retried, whatever the method: an OAuth grant, a sign-in attempt. */
  public ApiRequest neverRetry() {
    this.neverRetry = true;
    return this;
  }

  private static String queryValue(Object value) {
    if (value instanceof Instant instant) {
      return instant.toString();
    }
    return String.valueOf(value);
  }
}
