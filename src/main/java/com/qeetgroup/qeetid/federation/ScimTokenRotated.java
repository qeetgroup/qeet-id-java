package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A new SCIM token replacing another. {@code token} is shown only here.
 *
 * @param scimToken the SCIM token
 * @param token the token
 * @param replaced The token it replaced, which keeps working for the overlap.
 */
public record ScimTokenRotated(
    @JsonProperty("scim_token") ScimToken scimToken, String token, ScimToken replaced) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "ScimTokenRotated[scimToken="
        + scimToken
        + ", token=[redacted], replaced="
        + replaced
        + "]";
  }
}
