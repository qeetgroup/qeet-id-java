package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Issues, lists, revokes and verifies W3C verifiable credentials (JWT-VC).
 *
 * <p>Get it from {@code qeetId.verifiableCredentials()}. Safe for concurrent use.
 */
public final class VerifiableCredentials {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.verifiableCredentials()}.
   *
   * @param http the client's transport
   */
  public VerifiableCredentials(Transport http) {
    this.http = http;
  }

  /**
   * Issue.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the issued credential
   */
  public IssuedCredential issue(String organizationId, VerifiableCredentialIssueParams params) {
    return issue(organizationId, params, RequestOptions.none());
  }

  /**
   * Issue. With per-call settings: see {@link #issue(String, VerifiableCredentialIssueParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the issued credential
   */
  public IssuedCredential issue(
      String organizationId, VerifiableCredentialIssueParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/credentials", organizationId))
            .json(params),
        options,
        IssuedCredential.class);
  }

  /**
   * Lists the verifiable credentials.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<VerifiableCredential> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the verifiable credentials. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<VerifiableCredential> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/credentials", organizationId)),
        VerifiableCredential.class,
        options);
  }

  /**
   * Revokes a verifiable credential.
   *
   * @param organizationId the organization's id
   * @param credentialId the credential's id
   */
  public void revoke(String organizationId, String credentialId) {
    revoke(organizationId, credentialId, RequestOptions.none());
  }

  /**
   * Revokes a verifiable credential. With per-call settings: see {@link #revoke(String, String)}.
   *
   * @param organizationId the organization's id
   * @param credentialId the credential's id
   * @param options per-call settings
   */
  public void revoke(String organizationId, String credentialId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/credentials/{credentialId}/revoke",
                    organizationId,
                    credentialId))
            .retrySafe(),
        options);
  }

  /**
   * Checks a presented JWT-VC. Public: sends no API key.
   *
   * @param credential the credential
   * @return the credential verification
   */
  public CredentialVerification verify(String credential) {
    return verify(credential, RequestOptions.none());
  }

  /**
   * Checks a presented JWT-VC. With per-call settings: see {@link #verify(String)}.
   *
   * @param credential the credential
   * @param options per-call settings
   * @return the credential verification
   */
  public CredentialVerification verify(String credential, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/credentials/verify")
            .json(Bodies.of("credential", credential))
            .retrySafe()
            .unauthenticated(),
        options,
        CredentialVerification.class);
  }
}
