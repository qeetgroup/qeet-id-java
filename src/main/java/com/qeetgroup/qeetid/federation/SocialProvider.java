package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A social sign-in provider. Its client secret is never returned.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param provider Its name: {@code "github"}, {@code "facebook"}, {@code "x"}, {@code "discord"},
 *     {@code "apple"}, or any name for an OpenID Connect provider.
 * @param kind How it signs people in: its own OAuth flow, or {@code "oidc"} by discovery. One of
 *     {@code "oidc"}, {@code "github"}, {@code "facebook"}, {@code "x"}, {@code "discord"}, {@code
 *     "apple"}; others may be added
 * @param clientId the client's id
 * @param discoveryUrl the discovery URL
 * @param enabled whether it is on
 * @param appleTeamId the apple team's id
 * @param appleKeyId the apple key's id
 * @param createdAt when it was created
 */
public record SocialProvider(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String provider,
    String kind,
    @JsonProperty("client_id") String clientId,
    @JsonProperty("discovery_url") @Nullable String discoveryUrl,
    boolean enabled,
    @JsonProperty("apple_team_id") @Nullable String appleTeamId,
    @JsonProperty("apple_key_id") @Nullable String appleKeyId,
    @JsonProperty("created_at") Instant createdAt) {}
