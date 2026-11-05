package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A social account linked to a user.
 *
 * @param id its id
 * @param userId the user's id
 * @param tenantId the organisation's id
 * @param provider the provider
 * @param subject the subject
 * @param email the email address
 * @param linkedAt the linked time
 */
public record LinkedIdentity(
    String id,
    @JsonProperty("user_id") String userId,
    @JsonProperty("tenant_id") String tenantId,
    String provider,
    String subject,
    @Nullable String email,
    @JsonProperty("linked_at") Instant linkedAt) {}
