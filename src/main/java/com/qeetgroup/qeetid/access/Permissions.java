package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Checks permissions and reads the permission catalogue.
 *
 * <p>Get it from {@code qeetId.permissions()}. Safe for concurrent use.
 */
public final class Permissions {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.permissions()}.
   *
   * @param http the client's transport
   */
  public Permissions(Transport http) {
    this.http = http;
  }

  /**
   * The platform's permission catalogue.
   *
   * @return the list
   */
  public ListResult<Permission> list() {
    return list(RequestOptions.none());
  }

  /**
   * The platform's permission catalogue. With per-call settings: see {@link #list()}.
   *
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Permission> list(RequestOptions options) {
    return http.list(ApiRequest.get("/v1/permissions"), Permission.class, options);
  }

  /**
   * Whether the user may use the permission: the composed decision of roles and attribute policies
   * (an ABAC deny always wins).
   *
   * @param params who, where and what
   * @return whether it is allowed
   */
  public boolean check(PermissionCheckParams params) {
    return check(params, RequestOptions.none());
  }

  /**
   * Whether the user may use the permission, with per-call settings. See {@link
   * #check(PermissionCheckParams)}.
   *
   * @param params who, where and what
   * @param options per-call settings
   * @return whether it is allowed
   */
  public boolean check(PermissionCheckParams params, RequestOptions options) {
    return http.get("/v1/check", params, PermissionDecision.class, options).allowed();
  }

  /**
   * {@code check} with its reasons: the deciding model and rule, and the grants or the reason for a
   * deny.
   *
   * @param params who, where and what
   * @return the decision
   */
  public PermissionDecision explain(PermissionCheckParams params) {
    return explain(params, RequestOptions.none());
  }

  /**
   * {@code check} with its reasons, with per-call settings. See {@link
   * #explain(PermissionCheckParams)}.
   *
   * @param params who, where and what
   * @param options per-call settings
   * @return the decision
   */
  public PermissionDecision explain(PermissionCheckParams params, RequestOptions options) {
    ApiRequest request = ApiRequest.get("/v1/check").query(params).query("explain", "true");
    return http.execute(request, options, PermissionDecision.class);
  }

  /**
   * Every permission the user holds in the organisation, directly or through groups.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @return the effective permissions
   */
  public EffectivePermissions listEffective(String tenantId, String userId) {
    return listEffective(tenantId, userId, RequestOptions.none());
  }

  /**
   * Every permission the user holds in the organisation, directly or through groups. With per-call
   * settings: see {@link #listEffective(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @param options per-call settings
   * @return the effective permissions
   */
  public EffectivePermissions listEffective(
      String tenantId, String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/users/{userId}/tenants/{tenantId}/permissions", userId, tenantId)),
        options,
        EffectivePermissions.class);
  }
}
