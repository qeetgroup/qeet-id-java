package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * The organisation's SCIM provisioning state. Tokens themselves are never returned after they are
 * issued.
 *
 * @param tokenSet the token set
 * @param tokenCount the token count
 * @param tokenPrefix the token prefix
 * @param createdAt when it was created
 * @param lastUsedAt when it was last used
 * @param provisionedCount the provisioned count
 * @param lastRequest The identity provider's latest request, when there was one.
 */
public record ScimConfig(
    @JsonProperty("token_set") boolean tokenSet,
    @JsonProperty("token_count") int tokenCount,
    @JsonProperty("token_prefix") @Nullable String tokenPrefix,
    @JsonProperty("created_at") @Nullable Instant createdAt,
    @JsonProperty("last_used_at") @Nullable Instant lastUsedAt,
    @JsonProperty("provisioned_count") int provisionedCount,
    @JsonProperty("last_request") @Nullable LastRequest lastRequest) {

  /**
   * The identity provider's latest request, when there was one.
   *
   * @param at the at
   * @param operation One of {@code "post"}, {@code "put"}, {@code "patch"}, {@code "delete"};
   *     others may be added
   * @param resource One of {@code "user"}, {@code "group"}; others may be added
   * @param httpStatus the http status
   * @param status One of {@code "success"}, {@code "warning"}, {@code "failed"}; others may be
   *     added
   * @param errorCode the error code
   * @param reason the reason
   */
  public record LastRequest(
      Instant at,
      String operation,
      String resource,
      @JsonProperty("http_status") int httpStatus,
      String status,
      @JsonProperty("error_code") @Nullable String errorCode,
      @Nullable String reason) {}
}
