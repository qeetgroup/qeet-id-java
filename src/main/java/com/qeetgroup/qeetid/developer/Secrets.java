package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * The organization's encrypted secrets store.
 *
 * <p>Get it from {@code qeetId.secrets()}. Safe for concurrent use.
 */
public final class Secrets {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.secrets()}.
   *
   * @param http the client's transport
   */
  public Secrets(Transport http) {
    this.http = http;
  }

  /**
   * Lists the secrets.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<Secret> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the secrets. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Secret> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/secrets", organizationId)),
        Secret.class,
        options);
  }

  /**
   * Creates a secret.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the secret
   */
  public Secret create(String organizationId, SecretCreateParams params) {
    return create(organizationId, params, RequestOptions.none());
  }

  /**
   * Creates a secret. With per-call settings: see {@link #create(String, SecretCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the secret
   */
  public Secret create(String organizationId, SecretCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/secrets", organizationId))
            .json(params),
        options,
        Secret.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param organizationId the organization's id
   * @param secretId the secret's id
   * @param params the parameters
   * @return the secret
   */
  public Secret update(String organizationId, String secretId, SecretUpdateParams params) {
    return update(organizationId, secretId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * SecretUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param secretId the secret's id
   * @param params the parameters
   * @param options per-call settings
   * @return the secret
   */
  public Secret update(
      String organizationId, String secretId, SecretUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/organizations/{organizationId}/secrets/{secretId}",
                    organizationId,
                    secretId))
            .json(params),
        options,
        Secret.class);
  }

  /**
   * Deletes a secret.
   *
   * @param organizationId the organization's id
   * @param secretId the secret's id
   */
  public void delete(String organizationId, String secretId) {
    delete(organizationId, secretId, RequestOptions.none());
  }

  /**
   * Deletes a secret. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param organizationId the organization's id
   * @param secretId the secret's id
   * @param options per-call settings
   */
  public void delete(String organizationId, String secretId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/secrets/{secretId}", organizationId, secretId)),
        options);
  }

  /**
   * Returns the secret's value. Audited.
   *
   * @param organizationId the organization's id
   * @param secretId the secret's id
   * @return the secret's value; keep it out of logs
   */
  public String reveal(String organizationId, String secretId) {
    return reveal(organizationId, secretId, RequestOptions.none());
  }

  /**
   * Returns the secret's value. With per-call settings: see {@link #reveal(String, String)}.
   *
   * @param organizationId the organization's id
   * @param secretId the secret's id
   * @param options per-call settings
   * @return the secret's value; keep it out of logs
   */
  public String reveal(String organizationId, String secretId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/secrets/{secretId}/reveal",
                    organizationId,
                    secretId)),
            options)
        .path("value")
        .asText();
  }

  /**
   * Returns a secret's value by name. Needs the {@code vault:read} scope for it.
   *
   * @param name the secret's name
   * @return the secret value
   */
  public SecretValue getValue(String name) {
    return getValue(name, RequestOptions.none());
  }

  /**
   * Returns a secret's value by name. With per-call settings: see {@link #getValue(String)}.
   *
   * @param name the secret's name
   * @param options per-call settings
   * @return the secret value
   */
  public SecretValue getValue(String name, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/vault/{name}", name)), options, SecretValue.class);
  }
}
