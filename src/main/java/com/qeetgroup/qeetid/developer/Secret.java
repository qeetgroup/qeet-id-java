package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A secret's record. Its value is returned only by {@code reveal} and {@code getValue}.
 *
 * @param id its id
 * @param name its name
 * @param scope the scope
 * @param last4 The value's last four characters.
 * @param createdAt when it was created
 * @param updatedAt when it last changed
 * @param valueChangedAt the value changed time
 * @param lastRevealedAt the last revealed time
 * @param lastRevealedBy the last revealed by
 * @param lastRevealedCredential the last revealed credential
 * @param rotationIntervalDays the rotation interval days
 * @param rotationDueAt the rotation due time
 * @param rotationOverdue the rotation overdue
 */
public record Secret(
    String id,
    String name,
    String scope,
    String last4,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("value_changed_at") Instant valueChangedAt,
    @JsonProperty("last_revealed_at") @Nullable Instant lastRevealedAt,
    @JsonProperty("last_revealed_by") @Nullable LastRevealedBy lastRevealedBy,
    @JsonProperty("last_revealed_credential") @Nullable String lastRevealedCredential,
    @JsonProperty("rotation_interval_days") @Nullable Integer rotationIntervalDays,
    @JsonProperty("rotation_due_at") @Nullable Instant rotationDueAt,
    @JsonProperty("rotation_overdue") boolean rotationOverdue) {

  /**
   * Last revealed by.
   *
   * @param id its id
   * @param email the email address
   * @param displayName the display name, or null
   */
  public record LastRevealedBy(
      String id, String email, @JsonProperty("display_name") String displayName) {}
}
