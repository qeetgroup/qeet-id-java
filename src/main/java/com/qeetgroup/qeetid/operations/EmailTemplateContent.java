package com.qeetgroup.qeetid.operations;

import org.jspecify.annotations.Nullable;

/**
 * A template's editable content. {@code {{name}}} placeholders are filled in when it is sent.
 *
 * @param subject the subject
 * @param body the body
 * @param options the options
 */
public record EmailTemplateContent(
    String subject, String body, @Nullable EmailTemplateOptions options) {}
