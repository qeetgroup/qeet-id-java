package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * An application: an OAuth 2.0 / OpenID Connect client that signs people in with Qeet ID.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param clientId The OAuth {@code client_id}.
 * @param type {@code "confidential"} (has a secret) or {@code "public"} (a browser or mobile app).
 *     One of {@code "confidential"}, {@code "public"}; others may be added
 * @param name its name
 * @param redirectUris the redirect uris
 * @param postLogoutUris Where the application may send people after sign-out.
 * @param grantTypes the grant types
 * @param scopes the scopes
 * @param createdAt when it was created
 * @param pkceExemptUntil When a confidential application's exemption from PKCE ends; null for none.
 * @param backchannelLogoutUri Receives a signed logout token when someone it signed in signs out;
 *     empty for none.
 * @param frontchannelLogoutUri Loaded by the hosted signed-out page; empty for none.
 * @param logoUri the logo uri
 * @param clientUri the client uri
 * @param webOrigins Browser origins allowed to call the token endpoints.
 */
public record Application(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("client_id") String clientId,
    String type,
    String name,
    @JsonProperty("redirect_uris") List<String> redirectUris,
    @JsonProperty("post_logout_uris") List<String> postLogoutUris,
    @JsonProperty("grant_types") List<String> grantTypes,
    List<String> scopes,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("pkce_exempt_until") @Nullable Instant pkceExemptUntil,
    @JsonProperty("backchannel_logout_uri") String backchannelLogoutUri,
    @JsonProperty("frontchannel_logout_uri") String frontchannelLogoutUri,
    @JsonProperty("logo_uri") String logoUri,
    @JsonProperty("client_uri") String clientUri,
    @JsonProperty("web_origins") List<String> webOrigins) {}
