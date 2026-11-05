package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * What signing a user out everywhere ended.
 *
 * @param revoked the revoked
 * @param sessions the sessions
 * @param hostedLoginSessions the hosted login sessions
 * @param appGrants the app grants
 * @param trustedDevices the trusted devices
 * @param pendingAuthorizations the pending authorizations
 */
public record UserSessionsRevocation(
    int revoked,
    int sessions,
    @JsonProperty("hosted_login_sessions") int hostedLoginSessions,
    @JsonProperty("app_grants") int appGrants,
    @JsonProperty("trusted_devices") int trustedDevices,
    @JsonProperty("pending_authorizations") int pendingAuthorizations) {}
