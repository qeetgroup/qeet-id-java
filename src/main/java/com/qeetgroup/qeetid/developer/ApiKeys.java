package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages API keys.
 *
 * <p>Get it from {@code qeetId.apiKeys()}. Safe for concurrent use.
 */
public final class ApiKeys {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.apiKeys()}.
   *
   * @param http the client's transport
   */
  public ApiKeys(Transport http) {
    this.http = http;
  }

  /**
   * Creates an API key. The API makes it idempotent: an {@code Idempotency-Key} is sent with it,
   * generated per call unless you give one in {@link RequestOptions}, so a retry never acts twice.
   *
   * @param params the parameters
   * @return the API key created
   */
  public ApiKeyCreated create(ApiKeyCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /**
   * Creates an API key. With per-call settings: see {@link #create(ApiKeyCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the API key created
   */
  public ApiKeyCreated create(ApiKeyCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/api-keys").json(params).idempotent(), options, ApiKeyCreated.class);
  }

  /**
   * Lists the API keys.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<ApiKey> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the API keys. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<ApiKey> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/api-keys", tenantId)),
        ApiKey.class,
        options);
  }

  /**
   * Replaces a key with a new one, shown once. The old one keeps working for {@code overlap_hours},
   * so deployments can switch over without downtime.
   *
   * @param tenantId the organisation's id
   * @param apiKeyId the api key's id
   * @return the API key created
   */
  public ApiKeyCreated rotate(String tenantId, String apiKeyId) {
    return rotate(tenantId, apiKeyId, ApiKeyRotateParams.builder().build(), RequestOptions.none());
  }

  /**
   * Replaces a key with a new one, shown once. The old one keeps working for {@code overlap_hours},
   * so deployments can switch over without downtime.
   *
   * @param tenantId the organisation's id
   * @param apiKeyId the api key's id
   * @param params the parameters
   * @return the API key created
   */
  public ApiKeyCreated rotate(String tenantId, String apiKeyId, ApiKeyRotateParams params) {
    return rotate(tenantId, apiKeyId, params, RequestOptions.none());
  }

  /**
   * Replaces a key with a new one, shown once. With per-call settings: see {@link #rotate(String,
   * String, ApiKeyRotateParams)}.
   *
   * @param tenantId the organisation's id
   * @param apiKeyId the api key's id
   * @param params the parameters
   * @param options per-call settings
   * @return the API key created
   */
  public ApiKeyCreated rotate(
      String tenantId, String apiKeyId, ApiKeyRotateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/api-keys/{apiKeyId}/rotate", tenantId, apiKeyId))
            .json(params),
        options,
        ApiKeyCreated.class);
  }

  /**
   * Revokes a key at once.
   *
   * @param apiKeyId the api key's id
   */
  public void revoke(String apiKeyId) {
    revoke(apiKeyId, RequestOptions.none());
  }

  /**
   * Revokes a key at once. With per-call settings: see {@link #revoke(String)}.
   *
   * @param apiKeyId the api key's id
   * @param options per-call settings
   */
  public void revoke(String apiKeyId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/api-keys/{apiKeyId}", apiKeyId)), options);
  }
}
