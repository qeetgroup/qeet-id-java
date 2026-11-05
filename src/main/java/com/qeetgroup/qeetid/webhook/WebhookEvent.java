package com.qeetgroup.qeetid.webhook;

import com.qeetgroup.qeetid.internal.Json;
import java.time.Instant;
import java.util.Map;

/**
 * A verified webhook delivery's event. {@code id} is the same in every delivery of one event,
 * retries included: deduplicate on it. {@code type} may be an event newer than this SDK.
 *
 * @param id the event's id
 * @param type the event, such as {@code user.created}; see {@link WebhookEventTypes}
 * @param tenantId the organisation it happened in
 * @param occurredAt when the change happened
 * @param data what changed; its fields depend on {@code type}
 */
public record WebhookEvent(
    String id, String type, String tenantId, Instant occurredAt, Map<String, Object> data) {

  /** Copies the data. */
  public WebhookEvent {
    data = java.util.Collections.unmodifiableMap(new java.util.LinkedHashMap<>(data));
  }

  /**
   * The data as a type of your own, such as a record whose components are the snake_case fields
   * (annotate them with {@code @JsonProperty} where the names differ). Unknown fields are ignored.
   *
   * <pre>{@code record UserCreated(@JsonProperty("user_id") String userId, String email) {}
   * UserCreated created = event.dataAs(UserCreated.class);}</pre>
   *
   * @param type the type to read the data as
   * @param <T> that type
   * @return the data
   * @throws IllegalArgumentException when the data doesn't fit the type
   */
  public <T> T dataAs(Class<T> type) {
    return Json.convert(data, type);
  }

  /**
   * Whether this is the event the API's test call sends.
   *
   * @return whether {@code type} is {@code test.ping}
   */
  public boolean isTest() {
    return WebhookEventTypes.TEST_PING.equals(type);
  }
}
