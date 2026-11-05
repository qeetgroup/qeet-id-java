package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One day's sign-ins by method. A method with none is 0, not missing.
 *
 * @param date the date
 * @param password the password
 * @param passkey the passkey
 * @param social the social
 * @param saml the SAML
 * @param oidc the oidc
 * @param ldap the LDAP
 * @param magicLink the magic link
 */
public record AnalyticsActivityPoint(
    String date,
    int password,
    int passkey,
    int social,
    int saml,
    int oidc,
    int ldap,
    @JsonProperty("magic_link") int magicLink) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "AnalyticsActivityPoint[date="
        + date
        + ", password=[redacted], passkey="
        + passkey
        + ", social="
        + social
        + ", saml="
        + saml
        + ", oidc="
        + oidc
        + ", ldap="
        + ldap
        + ", magicLink="
        + magicLink
        + "]";
  }
}
