package com.qeetgroup.qeetid.exception;

import java.io.Serializable;

/**
 * One field the API refused in a {@code 422 validation_failed} answer.
 *
 * @param field the field, as the API names it (snake_case)
 * @param code why, as a stable code: {@code required}, {@code invalid_email}, {@code too_short},
 *     {@code too_long}, {@code invalid_uuid}, {@code invalid_url}, {@code not_allowed}, {@code
 *     invalid_phone} or {@code invalid}
 * @param message a message safe to show a person
 */
public record FieldError(String field, String code, String message) implements Serializable {}
