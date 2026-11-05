package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A template's text in one language.
 *
 * @param subject the subject
 * @param body the body
 * @param preheader the preheader
 * @param bodyHtml the body html
 */
public record EmailLocalizedContent(
    String subject,
    String body,
    @Nullable String preheader,
    @JsonProperty("body_html") @Nullable String bodyHtml) {}
