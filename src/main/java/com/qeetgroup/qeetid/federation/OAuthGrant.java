package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A person's consent to an application.
 *
 * @param id its id
 * @param clientId the client's id
 * @param clientName the client name
 * @param userId the user's id
 * @param userEmail the user email
 * @param userName the user name
 * @param scopes the scopes
 * @param authorizedAt the authorized time
 * @param consentedAt the consented time
 * @param lastUsedAt when it was last used
 * @param issuedAt the issued time
 * @param expiresAt when it expires
 */
public record OAuthGrant(
    String id,
    @JsonProperty("client_id") String clientId,
    @JsonProperty("client_name") String clientName,
    @JsonProperty("user_id") String userId,
    @JsonProperty("user_email") String userEmail,
    @JsonProperty("user_name") @Nullable String userName,
    List<String> scopes,
    @JsonProperty("authorized_at") Instant authorizedAt,
    @JsonProperty("consented_at") @Nullable Instant consentedAt,
    @JsonProperty("last_used_at") Instant lastUsedAt,
    @JsonProperty("issued_at") Instant issuedAt,
    @JsonProperty("expires_at") Instant expiresAt) {}
