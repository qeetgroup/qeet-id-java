package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * A third-party OAuth provider people can connect accounts from. Its client secret is never
 * returned.
 *
 * @param id its id
 * @param provider the provider
 * @param clientId the client's id
 * @param authorizeUrl the authorize URL
 * @param tokenUrl the token URL
 * @param scopes Space-separated scopes to ask for.
 * @param createdAt when it was created
 * @param updatedAt when it last changed
 */
public record TokenVaultProvider(
    String id,
    String provider,
    @JsonProperty("client_id") String clientId,
    @JsonProperty("authorize_url") String authorizeUrl,
    @JsonProperty("token_url") String tokenUrl,
    String scopes,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt) {}
