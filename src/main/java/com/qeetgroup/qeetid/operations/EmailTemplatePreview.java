package com.qeetgroup.qeetid.operations;

import org.jspecify.annotations.Nullable;

/**
 * A rendered template. Placeholders left as {@code {{name}}} were not given.
 *
 * @param subject the subject
 * @param body the body
 * @param html the html
 * @param locale the locale
 */
public record EmailTemplatePreview(
    String subject, String body, @Nullable String html, @Nullable String locale) {}
