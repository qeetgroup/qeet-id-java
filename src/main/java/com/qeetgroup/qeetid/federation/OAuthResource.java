package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * An RFC 8707 resource: a downstream service applications may ask tokens for. A token issued for it
 * names it alone as audience.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param identifier An absolute URI without a fragment, unique on the platform.
 * @param name its name
 * @param serverClientId The application that serves the resource, which may then introspect its
 *     tokens.
 * @param createdAt when it was created
 */
public record OAuthResource(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String identifier,
    @Nullable String name,
    @JsonProperty("server_client_id") @Nullable String serverClientId,
    @JsonProperty("created_at") Instant createdAt) {}
