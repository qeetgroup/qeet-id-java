package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * A template's presentation and its translations.
 *
 * @param preheader the preheader
 * @param bodyHtml the body html
 * @param defaultLocale the default locale
 * @param showLogo the show logo
 * @param alignment One of {@code "left"}, {@code "center"}; others may be added
 * @param translations the translations
 */
public record EmailTemplateOptions(
    @Nullable String preheader,
    @JsonProperty("body_html") @Nullable String bodyHtml,
    @JsonProperty("default_locale") @Nullable String defaultLocale,
    @JsonProperty("show_logo") @Nullable Boolean showLogo,
    @Nullable String alignment,
    Map<String, EmailLocalizedContent> translations) {}
