package com.qeetgroup.qeetid.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * The authorization server's metadata (OpenID Connect Discovery 1.0, RFC 8414), from {@code
 * /.well-known/openid-configuration}.
 *
 * @param issuer the issuer, as tokens carry it in {@code iss}
 * @param authorizationEndpoint where people are sent to sign in
 * @param tokenEndpoint where codes, refresh tokens and the other grants are redeemed
 * @param jwksUri where the signing keys are published
 * @param userinfoEndpoint the OpenID Connect userinfo endpoint
 * @param revocationEndpoint RFC 7009 revocation
 * @param introspectionEndpoint RFC 7662 introspection
 * @param endSessionEndpoint RP-initiated logout
 * @param deviceAuthorizationEndpoint RFC 8628 device authorization
 * @param backchannelAuthenticationEndpoint OpenID CIBA
 * @param grantTypesSupported the grants served
 * @param scopesSupported the scopes offered
 * @param codeChallengeMethodsSupported the PKCE methods accepted
 * @param idTokenSigningAlgValuesSupported the ID token algorithms
 * @param tokenEndpointAuthMethodsSupported how clients authenticate
 */
public record DiscoveryDocument(
    String issuer,
    @JsonProperty("authorization_endpoint") @Nullable String authorizationEndpoint,
    @JsonProperty("token_endpoint") @Nullable String tokenEndpoint,
    @JsonProperty("jwks_uri") String jwksUri,
    @JsonProperty("userinfo_endpoint") @Nullable String userinfoEndpoint,
    @JsonProperty("revocation_endpoint") @Nullable String revocationEndpoint,
    @JsonProperty("introspection_endpoint") @Nullable String introspectionEndpoint,
    @JsonProperty("end_session_endpoint") @Nullable String endSessionEndpoint,
    @JsonProperty("device_authorization_endpoint") @Nullable String deviceAuthorizationEndpoint,
    @JsonProperty("backchannel_authentication_endpoint")
        @Nullable String backchannelAuthenticationEndpoint,
    @JsonProperty("grant_types_supported") List<String> grantTypesSupported,
    @JsonProperty("scopes_supported") List<String> scopesSupported,
    @JsonProperty("code_challenge_methods_supported") List<String> codeChallengeMethodsSupported,
    @JsonProperty("id_token_signing_alg_values_supported")
        List<String> idTokenSigningAlgValuesSupported,
    @JsonProperty("token_endpoint_auth_methods_supported")
        List<String> tokenEndpointAuthMethodsSupported) {}
