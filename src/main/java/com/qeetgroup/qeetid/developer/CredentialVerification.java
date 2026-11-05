package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Whether a presented JWT-VC is authentic and not revoked.
 *
 * @param valid the valid
 * @param reason the reason
 * @param reasonCode One of {@code "vc.not_credential"}, {@code "vc.signature_invalid"}, {@code
 *     "vc.unknown"}, {@code "vc.revoked"}; others may be added
 * @param subject the subject
 * @param issuer whether issuer
 * @param vc the vc
 */
public record CredentialVerification(
    boolean valid,
    @Nullable String reason,
    @JsonProperty("reason_code") @Nullable String reasonCode,
    @Nullable String subject,
    @Nullable String issuer,
    Map<String, Object> vc) {}
