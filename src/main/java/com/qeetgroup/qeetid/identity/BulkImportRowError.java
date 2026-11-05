package com.qeetgroup.qeetid.identity;

import org.jspecify.annotations.Nullable;

/**
 * One row of a bulk create or import that failed.
 *
 * @param line 1-based: the row of {@code "users"}, or the line of the uploaded file.
 * @param email the email address
 * @param code Why, as a stable code such as {@code auth.email_exists} or {@code
 *     user.import_field_invalid}.
 * @param field One of {@code "email"}, {@code "password"}, {@code "display_name"}, {@code "phone"};
 *     others may be added
 * @param message the message
 */
public record BulkImportRowError(
    @Nullable Integer line,
    @Nullable String email,
    String code,
    @Nullable String field,
    String message) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "BulkImportRowError[line="
        + line
        + ", email="
        + email
        + ", code=[redacted], field="
        + field
        + ", message="
        + message
        + "]";
  }
}
