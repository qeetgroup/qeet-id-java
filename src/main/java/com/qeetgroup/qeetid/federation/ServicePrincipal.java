package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A machine identity that gets tokens with the {@code client_credentials} grant (see {@code
 * oauth().clientCredentialsGrant}).
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param name its name
 * @param description its description
 * @param scopes the scopes
 * @param disabledAt Set while the principal is disabled.
 * @param lastUsedAt when it was last used
 * @param previousSecretExpiresAt While a rotated-out secret still gets tokens.
 * @param createdAt when it was created
 */
public record ServicePrincipal(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String name,
    String description,
    List<String> scopes,
    @JsonProperty("disabled_at") @Nullable Instant disabledAt,
    @JsonProperty("last_used_at") @Nullable Instant lastUsedAt,
    @JsonProperty("previous_secret_expires_at") @Nullable Instant previousSecretExpiresAt,
    @JsonProperty("created_at") Instant createdAt) {}
