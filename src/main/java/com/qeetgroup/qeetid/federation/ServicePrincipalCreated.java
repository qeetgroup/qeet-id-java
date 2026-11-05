package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A new principal and its credentials. The secret is shown once and can't be read again.
 *
 * @param servicePrincipal the service principal
 * @param clientId the client's id
 * @param clientSecret the client secret
 * @param warning the warning
 */
public record ServicePrincipalCreated(
    @JsonProperty("service_principal") ServicePrincipal servicePrincipal,
    @JsonProperty("client_id") String clientId,
    @JsonProperty("client_secret") String clientSecret,
    String warning) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "ServicePrincipalCreated[servicePrincipal="
        + servicePrincipal
        + ", clientId="
        + clientId
        + ", clientSecret=[redacted], warning="
        + warning
        + "]";
  }
}
