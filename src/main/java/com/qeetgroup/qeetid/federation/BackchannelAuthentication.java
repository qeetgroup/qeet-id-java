package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OpenID CIBA: the request to poll with.
 *
 * @param authReqId the auth req's id
 * @param expiresIn the expires in
 * @param interval the interval
 */
public record BackchannelAuthentication(
    @JsonProperty("auth_req_id") String authReqId,
    @JsonProperty("expires_in") int expiresIn,
    int interval) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "BackchannelAuthentication[authReqId=[redacted], expiresIn="
        + expiresIn
        + ", interval="
        + interval
        + "]";
  }
}
