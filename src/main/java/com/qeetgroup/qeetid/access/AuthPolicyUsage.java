package com.qeetgroup.qeetid.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * How many live accounts sign in each way, and how many have no other way in.
 *
 * @param people the people
 * @param password the password
 * @param passkey the passkey
 * @param social the social
 * @param magicLinkEnabled whether magic link enabled
 */
public record AuthPolicyUsage(
    long people,
    Password password,
    Passkey passkey,
    List<Social> social,
    @JsonProperty("magic_link_enabled") boolean magicLinkEnabled) {

  /**
   * Password.
   *
   * @param people the people
   * @param only the only
   */
  public record Password(long people, int only) {}

  /**
   * Passkey.
   *
   * @param people the people
   * @param only the only
   */
  public record Passkey(long people, int only) {}

  /**
   * Social.
   *
   * @param provider the provider
   * @param linked the linked
   * @param only the only
   */
  public record Social(String provider, int linked, int only) {}

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "AuthPolicyUsage[people="
        + people
        + ", password=[redacted], passkey="
        + passkey
        + ", social="
        + social
        + ", magicLinkEnabled="
        + magicLinkEnabled
        + "]";
  }
}
