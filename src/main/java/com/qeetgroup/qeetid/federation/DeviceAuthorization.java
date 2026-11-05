package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * RFC 8628 §3.2: what the device shows the person, and how it polls.
 *
 * @param deviceCode the device code
 * @param userCode The code the person types, {@code XXXX-XXXX}.
 * @param verificationUri the verification uri
 * @param verificationUriComplete the verification uri complete
 * @param expiresIn the expires in
 * @param interval The minimum seconds between polls.
 */
public record DeviceAuthorization(
    @JsonProperty("device_code") String deviceCode,
    @JsonProperty("user_code") String userCode,
    @JsonProperty("verification_uri") String verificationUri,
    @JsonProperty("verification_uri_complete") @Nullable String verificationUriComplete,
    @JsonProperty("expires_in") int expiresIn,
    @Nullable Integer interval) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "DeviceAuthorization[deviceCode=[redacted], userCode=[redacted], verificationUri="
        + verificationUri
        + ", verificationUriComplete=[redacted], expiresIn="
        + expiresIn
        + ", interval="
        + interval
        + "]";
  }
}
