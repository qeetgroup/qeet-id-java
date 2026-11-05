package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * The organisation's encrypted secrets store.
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
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<Secret> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the secrets. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Secret> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/secrets", tenantId)),
        Secret.class,
        options);
  }

  /**
   * Creates a secret.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the secret
   */
  public Secret create(String tenantId, SecretCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates a secret. With per-call settings: see {@link #create(String, SecretCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the secret
   */
  public Secret create(String tenantId, SecretCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/secrets", tenantId)).json(params),
        options,
        Secret.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param secretId the secret's id
   * @param params the parameters
   * @return the secret
   */
  public Secret update(String tenantId, String secretId, SecretUpdateParams params) {
    return update(tenantId, secretId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * SecretUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param secretId the secret's id
   * @param params the parameters
   * @param options per-call settings
   * @return the secret
   */
  public Secret update(
      String tenantId, String secretId, SecretUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH", Paths.of("/v1/tenants/{tenantId}/secrets/{secretId}", tenantId, secretId))
            .json(params),
        options,
        Secret.class);
  }

  /**
   * Deletes a secret.
   *
   * @param tenantId the organisation's id
   * @param secretId the secret's id
   */
  public void delete(String tenantId, String secretId) {
    delete(tenantId, secretId, RequestOptions.none());
  }

  /**
   * Deletes a secret. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param secretId the secret's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String secretId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/secrets/{secretId}", tenantId, secretId)),
        options);
  }

  /**
   * Returns the secret's value. Audited.
   *
   * @param tenantId the organisation's id
   * @param secretId the secret's id
   * @return the secret's value; keep it out of logs
   */
  public String reveal(String tenantId, String secretId) {
    return reveal(tenantId, secretId, RequestOptions.none());
  }

  /**
   * Returns the secret's value. With per-call settings: see {@link #reveal(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param secretId the secret's id
   * @param options per-call settings
   * @return the secret's value; keep it out of logs
   */
  public String reveal(String tenantId, String secretId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/secrets/{secretId}/reveal", tenantId, secretId)),
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
