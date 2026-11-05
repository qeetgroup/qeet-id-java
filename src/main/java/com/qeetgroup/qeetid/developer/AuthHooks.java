package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages authentication hooks.
 *
 * <p>Get it from {@code qeetId.authHooks()}. Safe for concurrent use.
 */
public final class AuthHooks {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.authHooks()}.
   *
   * @param http the client's transport
   */
  public AuthHooks(Transport http) {
    this.http = http;
  }

  /**
   * Lists the auth hooks.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<AuthHook> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the auth hooks. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<AuthHook> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/auth-hooks", tenantId)),
        AuthHook.class,
        options);
  }

  /**
   * Creates an auth hook.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the auth hook
   */
  public AuthHook create(String tenantId, AuthHookCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates an auth hook. With per-call settings: see {@link #create(String,
   * AuthHookCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the auth hook
   */
  public AuthHook create(String tenantId, AuthHookCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/auth-hooks", tenantId)).json(params),
        options,
        AuthHook.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param hookId the hook's id
   * @param params the parameters
   * @return the auth hook state
   */
  public AuthHookState update(String tenantId, String hookId, AuthHookUpdateParams params) {
    return update(tenantId, hookId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * AuthHookUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param hookId the hook's id
   * @param params the parameters
   * @param options per-call settings
   * @return the auth hook state
   */
  public AuthHookState update(
      String tenantId, String hookId, AuthHookUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH", Paths.of("/v1/tenants/{tenantId}/auth-hooks/{hookId}", tenantId, hookId))
            .json(params),
        options,
        AuthHookState.class);
  }

  /**
   * Deletes an auth hook.
   *
   * @param tenantId the organisation's id
   * @param hookId the hook's id
   */
  public void delete(String tenantId, String hookId) {
    delete(tenantId, hookId, RequestOptions.none());
  }

  /**
   * Deletes an auth hook. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param hookId the hook's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String hookId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/auth-hooks/{hookId}", tenantId, hookId)),
        options);
  }

  /**
   * Calls the hook with a sample payload and reports what it answered.
   *
   * @param tenantId the organisation's id
   * @param hookId the hook's id
   * @return the auth hook test result
   */
  public AuthHookTestResult test(String tenantId, String hookId) {
    return test(tenantId, hookId, RequestOptions.none());
  }

  /**
   * Calls the hook with a sample payload and reports what it answered. With per-call settings: see
   * {@link #test(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param hookId the hook's id
   * @param options per-call settings
   * @return the auth hook test result
   */
  public AuthHookTestResult test(String tenantId, String hookId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of("/v1/tenants/{tenantId}/auth-hooks/{hookId}/test", tenantId, hookId)),
        options,
        AuthHookTestResult.class);
  }
}
