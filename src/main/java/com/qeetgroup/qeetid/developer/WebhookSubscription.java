package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A webhook subscription: an endpoint and the events it receives.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param url the URL
 * @param events the events
 * @param disabledAt Set while disabled; null while enabled.
 * @param disabledReason {@code "disabled"} by an administrator, or {@code "failing"} after three
 *     days of failed deliveries. One of {@code "disabled"}, {@code "failing"}; others may be added
 * @param disabledBy the disabled by
 * @param failingSince When deliveries started failing.
 * @param previousSecretExpiresAt While a rotated-out secret still signs deliveries alongside the
 *     new one.
 * @param lastDeliveredAt the last delivered time
 * @param lastStatusCode the last status code
 * @param counts the counts
 * @param createdAt when it was created
 * @param secret The signing secret: only in {@code "create"}'s answer, once.
 */
public record WebhookSubscription(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String url,
    List<String> events,
    @JsonProperty("disabled_at") @Nullable Instant disabledAt,
    @JsonProperty("disabled_reason") @Nullable String disabledReason,
    @JsonProperty("disabled_by") @Nullable String disabledBy,
    @JsonProperty("failing_since") @Nullable Instant failingSince,
    @JsonProperty("previous_secret_expires_at") @Nullable Instant previousSecretExpiresAt,
    @JsonProperty("last_delivered_at") @Nullable Instant lastDeliveredAt,
    @JsonProperty("last_status_code") @Nullable Integer lastStatusCode,
    @Nullable Counts counts,
    @JsonProperty("created_at") Instant createdAt,
    @Nullable String secret) {

  /**
   * Counts.
   *
   * @param pending the pending
   * @param retrying the retrying
   * @param gaveUp the gave up
   */
  public record Counts(int pending, int retrying, @JsonProperty("gave_up") int gaveUp) {}

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "WebhookSubscription[id="
        + id
        + ", tenantId="
        + tenantId
        + ", url="
        + url
        + ", events="
        + events
        + ", disabledAt="
        + disabledAt
        + ", disabledReason="
        + disabledReason
        + ", disabledBy="
        + disabledBy
        + ", failingSince="
        + failingSince
        + ", previousSecretExpiresAt="
        + previousSecretExpiresAt
        + ", lastDeliveredAt="
        + lastDeliveredAt
        + ", lastStatusCode="
        + lastStatusCode
        + ", counts="
        + counts
        + ", createdAt="
        + createdAt
        + ", secret=[redacted]]";
  }
}
