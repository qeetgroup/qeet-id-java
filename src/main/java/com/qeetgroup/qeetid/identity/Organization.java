package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * An organization. Every API key belongs to one.
 *
 * @param id its id
 * @param slug the slug
 * @param name its name
 * @param status One of {@code "active"}, {@code "suspended"}; others may be added
 * @param plan One of {@code "free"}, {@code "starter"}, {@code "pro"}, {@code "enterprise"}; others
 *     may be added
 * @param region the region
 * @param logoUrl The organization's logo, a hosted URL or a small inline data URL; empty when none
 *     is set.
 * @param metadata free-form data
 * @param createdAt when it was created
 * @param updatedAt when it last changed
 * @param deletionDueAt When a scheduled deletion takes effect; null when none is scheduled.
 * @param memberCount the member count
 * @param mfaEnabledCount the MFA enabled count
 */
public record Organization(
    String id,
    String slug,
    String name,
    String status,
    String plan,
    String region,
    @JsonProperty("logo_url") String logoUrl,
    Map<String, Object> metadata,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("deletion_due_at") @Nullable Instant deletionDueAt,
    @JsonProperty("member_count") @Nullable Integer memberCount,
    @JsonProperty("mfa_enabled_count") @Nullable Integer mfaEnabledCount) {}
