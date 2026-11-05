package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * How the hosted sign-in pages and emails look for the organisation.
 *
 * @param tenantId the organisation's id
 * @param logoUrl A URL or a base64 {@code data:} image (PNG, JPEG, WEBP or SVG, at most 2 MiB).
 * @param primaryColor the primary color
 * @param secondaryColor the secondary color
 * @param customDomain the custom domain
 * @param emailFromName the email from name
 * @param emailFromAddress the email from address
 * @param settings {@code background_color} (six-digit hex) and {@code email_reply_to}; keep unknown
 *     keys when writing back.
 */
public record TenantBranding(
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("logo_url") @Nullable String logoUrl,
    @JsonProperty("primary_color") @Nullable String primaryColor,
    @JsonProperty("secondary_color") @Nullable String secondaryColor,
    @JsonProperty("custom_domain") @Nullable String customDomain,
    @JsonProperty("email_from_name") @Nullable String emailFromName,
    @JsonProperty("email_from_address") @Nullable String emailFromAddress,
    Map<String, Object> settings) {}
