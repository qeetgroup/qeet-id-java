package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A new SCIM token. {@code token} is shown only here: give it to the identity provider.
 *
 * @param scimToken the SCIM token
 * @param token the token
 */
public record ScimTokenCreated(@JsonProperty("scim_token") ScimToken scimToken, String token) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "ScimTokenCreated[scimToken=" + scimToken + ", token=[redacted]]";
  }
}
