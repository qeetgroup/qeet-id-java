package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * An invitation to join the organisation. Its token is never returned: the link goes only to the
 * invited address.
 *
 * @param id its id
 * @param tenantId the organisation's id
 * @param email the email address
 * @param roleId The role the invitee receives on accepting.
 * @param status its state
 * @param expiresAt when it expires
 * @param acceptedAt the accepted time
 * @param createdAt when it was created
 * @param delivery Present on {@code "create"} and {@code "resend"}.
 */
public record Invitation(
    String id,
    @JsonProperty("tenant_id") String tenantId,
    String email,
    @JsonProperty("role_id") @Nullable String roleId,
    String status,
    @JsonProperty("expires_at") Instant expiresAt,
    @JsonProperty("accepted_at") @Nullable Instant acceptedAt,
    @JsonProperty("created_at") Instant createdAt,
    @Nullable InvitationDelivery delivery) {}
