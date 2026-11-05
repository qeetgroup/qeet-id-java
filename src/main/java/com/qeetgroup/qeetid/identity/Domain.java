package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A domain the organisation claimed, with the DNS record that proves it.
 *
 * @param id its id
 * @param domain the domain
 * @param status The verification state, for example {@code "pending"} or {@code "verified"}.
 * @param verificationToken the verification token
 * @param dnsRecordName Publish this TXT record, then call {@code "verify"}.
 * @param dnsRecordType the dns record type
 * @param dnsRecordValue the dns record value
 * @param verifiedAt Set once DNS verification succeeds.
 * @param lastCheckedAt the last checked time
 * @param lastError the last error
 * @param ssoEnabled People on the domain sign in only through the organisation's SSO connection.
 * @param jitEnabled Accounts are created on first SSO sign-in (just-in-time provisioning).
 * @param createdAt when it was created
 */
public record Domain(
    String id,
    String domain,
    String status,
    @JsonProperty("verification_token") String verificationToken,
    @JsonProperty("dns_record_name") String dnsRecordName,
    @JsonProperty("dns_record_type") String dnsRecordType,
    @JsonProperty("dns_record_value") String dnsRecordValue,
    @JsonProperty("verified_at") @Nullable Instant verifiedAt,
    @JsonProperty("last_checked_at") @Nullable Instant lastCheckedAt,
    @JsonProperty("last_error") @Nullable String lastError,
    @JsonProperty("sso_enabled") boolean ssoEnabled,
    @JsonProperty("jit_enabled") boolean jitEnabled,
    @JsonProperty("created_at") Instant createdAt) {}
