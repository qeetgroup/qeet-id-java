package com.qeetgroup.qeetid.developer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qeetgroup.qeetid.identity.User;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * An issued W3C verifiable credential's record.
 *
 * @param id its id
 * @param subject the subject
 * @param type its type
 * @param issuedAt the issued time
 * @param expiresAt when it expires
 * @param revoked the revoked
 * @param user The user it was issued for, when it names one.
 */
public record VerifiableCredential(
    String id,
    String subject,
    String type,
    @JsonProperty("issued_at") Instant issuedAt,
    @JsonProperty("expires_at") @Nullable Instant expiresAt,
    boolean revoked,
    @Nullable User user) {

  /**
   * The user it was issued for, when it names one.
   *
   * @param id its id
   * @param email the email address
   * @param displayName the display name, or null
   */
  public record User(String id, String email, @JsonProperty("display_name") String displayName) {}
}
