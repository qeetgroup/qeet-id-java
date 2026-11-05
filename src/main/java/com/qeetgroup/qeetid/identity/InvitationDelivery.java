package com.qeetgroup.qeetid.identity;

import org.jspecify.annotations.Nullable;

/**
 * Whether an invitation's email went out.
 *
 * @param status {@code "sent"} when a provider accepted the email; {@code "not_sent"} with a {@code
 *     code} and {@code message} otherwise. One of {@code "sent"}, {@code "not_sent"}; others may be
 *     added
 * @param code For {@code not_sent}: {@code email.suppressed} or {@code email.provider_unavailable},
 *     for example.
 * @param message the message
 */
public record InvitationDelivery(String status, @Nullable String code, @Nullable String message) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "InvitationDelivery[status=" + status + ", code=[redacted], message=" + message + "]";
  }
}
