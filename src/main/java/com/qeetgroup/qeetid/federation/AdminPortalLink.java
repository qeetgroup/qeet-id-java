package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A time-limited link that lets a customer's IT administrator set up SSO, SCIM or domains without a
 * Qeet ID account.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param capabilities One of {@code "saml"}, {@code "scim"}, {@code "domain_verification"}; others
 *     may be added
 * @param createdBy the created by
 * @param expiresAt when it expires
 * @param revokedAt the revoked time
 * @param lastUsedAt when it was last used
 * @param createdAt when it was created
 */
public record AdminPortalLink(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    List<String> capabilities,
    @JsonProperty("created_by") @Nullable String createdBy,
    @JsonProperty("expires_at") Instant expiresAt,
    @JsonProperty("revoked_at") @Nullable Instant revokedAt,
    @JsonProperty("last_used_at") @Nullable Instant lastUsedAt,
    @JsonProperty("created_at") Instant createdAt) {}
