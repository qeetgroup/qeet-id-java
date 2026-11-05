package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A template as it is used: the organisation's override ({@code custom}), or the platform's
 * default.
 *
 * @param subject the subject
 * @param body the body
 * @param options the options
 * @param key the key
 * @param name its name
 * @param description its description
 * @param variables The placeholders it may use.
 * @param custom the custom
 * @param deliveryScope One of {@code "tenant"}, {@code "platform"}; others may be added
 */
public record EmailTemplate(
    String subject,
    String body,
    @Nullable EmailTemplateOptions options,
    String key,
    String name,
    @Nullable String description,
    List<String> variables,
    boolean custom,
    @JsonProperty("delivery_scope") @Nullable String deliveryScope) {}
