package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A SAML attribute the identity provider sends, and the user field it comes from.
 *
 * @param name its name
 * @param source the source
 * @param nameFormat One of {@code "basic"}, {@code "uri"}, {@code "unspecified"}; others may be
 *     added
 */
public record SamlAttributeMapping(
    String name, String source, @JsonProperty("name_format") @Nullable String nameFormat) {}
