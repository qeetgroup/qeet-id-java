package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A downstream service provider that signs people in with Qeet ID as its SAML identity provider.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param name its name
 * @param entityId the entity's id
 * @param acsUrl the acs URL
 * @param nameIdFormat the name ID format
 * @param nameIdAttribute the name ID attribute
 * @param certificate the certificate
 * @param requireSignedRequests whether require signed requests
 * @param encryptAssertions the encrypt assertions
 * @param certificateDetails the certificate details
 * @param status One of {@code "draft"}, {@code "active"}, {@code "disabled"}; others may be added
 * @param attributes the attributes
 * @param assignmentRequired Only assigned users and groups may sign in to it.
 * @param createdAt when it was created
 * @param updatedAt when it last changed
 * @param lastLoginAt the last login time
 */
public record SamlServiceProvider(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String name,
    @JsonProperty("entity_id") String entityId,
    @JsonProperty("acs_url") String acsUrl,
    @JsonProperty("name_id_format") @Nullable String nameIdFormat,
    @JsonProperty("name_id_attribute") @Nullable String nameIdAttribute,
    @Nullable String certificate,
    @JsonProperty("require_signed_requests") @Nullable Boolean requireSignedRequests,
    @JsonProperty("encrypt_assertions") @Nullable Boolean encryptAssertions,
    @JsonProperty("certificate_details") @Nullable CertificateDetails certificateDetails,
    String status,
    List<SamlAttributeMapping> attributes,
    @JsonProperty("assignment_required") @Nullable Boolean assignmentRequired,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("last_login_at") @Nullable Instant lastLoginAt) {

  /**
   * Certificate details.
   *
   * @param subject the subject
   * @param fingerprintSha256 the fingerprint sha256
   * @param notBefore the not before
   * @param notAfter the not after
   */
  public record CertificateDetails(
      String subject,
      @JsonProperty("fingerprint_sha256") String fingerprintSha256,
      @JsonProperty("not_before") Instant notBefore,
      @JsonProperty("not_after") Instant notAfter) {}
}
