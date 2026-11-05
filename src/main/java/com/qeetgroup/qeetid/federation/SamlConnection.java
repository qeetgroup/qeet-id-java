package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * An inbound SAML connection: Qeet ID is the service provider for an external identity provider.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param name its name
 * @param spEntityId Give these to the identity provider.
 * @param acsUrl the acs URL
 * @param spMetadataUrl the sp metadata URL
 * @param idpEntityId the idp entity's id
 * @param idpSsoUrl the idp SSO URL
 * @param idpCertificate the idp certificate
 * @param certificates the certificates
 * @param metadataUrl the metadata URL
 * @param metadataRefreshedAt the metadata refreshed time
 * @param metadataError the metadata error
 * @param emailAttribute the email attribute
 * @param nameAttribute the name attribute
 * @param groupsAttribute the groups attribute
 * @param status One of {@code "draft"}, {@code "active"}, {@code "disabled"}; others may be added
 * @param allowIdpInitiated whether allow idp initiated
 * @param createdAt when it was created
 * @param updatedAt when it last changed
 * @param lastLoginAt the last login time
 */
public record SamlConnection(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String name,
    @JsonProperty("sp_entity_id") String spEntityId,
    @JsonProperty("acs_url") String acsUrl,
    @JsonProperty("sp_metadata_url") String spMetadataUrl,
    @JsonProperty("idp_entity_id") @Nullable String idpEntityId,
    @JsonProperty("idp_sso_url") @Nullable String idpSsoUrl,
    @JsonProperty("idp_certificate") @Nullable String idpCertificate,
    List<SamlCertificate> certificates,
    @JsonProperty("metadata_url") @Nullable String metadataUrl,
    @JsonProperty("metadata_refreshed_at") @Nullable Instant metadataRefreshedAt,
    @JsonProperty("metadata_error") @Nullable String metadataError,
    @JsonProperty("email_attribute") @Nullable String emailAttribute,
    @JsonProperty("name_attribute") @Nullable String nameAttribute,
    @JsonProperty("groups_attribute") @Nullable String groupsAttribute,
    String status,
    @JsonProperty("allow_idp_initiated") @Nullable Boolean allowIdpInitiated,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("last_login_at") @Nullable Instant lastLoginAt) {}
