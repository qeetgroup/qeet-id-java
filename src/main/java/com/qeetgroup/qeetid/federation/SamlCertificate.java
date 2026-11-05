package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * One of an identity provider's signing certificates.
 *
 * @param id its id
 * @param certificate the certificate
 * @param subject the subject
 * @param fingerprintSha256 the fingerprint sha256
 * @param notBefore the not before
 * @param notAfter the not after
 * @param source {@code "manual"}, or {@code "metadata"} when it came from the provider's metadata.
 *     One of {@code "manual"}, {@code "metadata"}; others may be added
 * @param createdAt when it was created
 */
public record SamlCertificate(
    String id,
    String certificate,
    String subject,
    @JsonProperty("fingerprint_sha256") String fingerprintSha256,
    @JsonProperty("not_before") @Nullable Instant notBefore,
    @JsonProperty("not_after") @Nullable Instant notAfter,
    String source,
    @JsonProperty("created_at") Instant createdAt) {}
