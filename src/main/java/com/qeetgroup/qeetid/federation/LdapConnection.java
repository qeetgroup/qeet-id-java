package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A directory (LDAP or Active Directory) connection. The bind password is never returned.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param name its name
 * @param serverUrl the server URL
 * @param startTls the start tls
 * @param skipTlsVerify whether skip tls verify
 * @param bindDn the bind dn
 * @param baseDn the base dn
 * @param userFilter the user filter
 * @param emailAttribute the email attribute
 * @param nameAttribute the name attribute
 * @param status One of {@code "draft"}, {@code "active"}, {@code "disabled"}; others may be added
 * @param createdAt when it was created
 * @param updatedAt when it last changed
 * @param lastLoginAt the last login time
 */
public record LdapConnection(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String name,
    @JsonProperty("server_url") String serverUrl,
    @JsonProperty("start_tls") boolean startTls,
    @JsonProperty("skip_tls_verify") boolean skipTlsVerify,
    @JsonProperty("bind_dn") String bindDn,
    @JsonProperty("base_dn") String baseDn,
    @JsonProperty("user_filter") String userFilter,
    @JsonProperty("email_attribute") String emailAttribute,
    @JsonProperty("name_attribute") String nameAttribute,
    String status,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("last_login_at") @Nullable Instant lastLoginAt) {}
