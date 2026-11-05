package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;
import java.util.List;

/**
 * Manages roles, the permissions they grant, and who holds them.
 *
 * <p>Get it from {@code qeetId.roles()}. Safe for concurrent use.
 */
public final class Roles {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.roles()}.
   *
   * @param http the client's transport
   */
  public Roles(Transport http) {
    this.http = http;
  }

  /**
   * Lists the organisation's roles, built-in and custom.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<Role> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the organisation's roles, built-in and custom. With per-call settings: see {@link
   * #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Role> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/roles", tenantId)), Role.class, options);
  }

  /**
   * Creates a custom role with no permissions; grant them with {@code grantPermission} or {@code
   * setPermissions}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the role
   */
  public Role create(String tenantId, RoleCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates a custom role with no permissions; grant them with {@code grantPermission} or {@code
   * setPermissions}. With per-call settings: see {@link #create(String, RoleCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the role
   */
  public Role create(String tenantId, RoleCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/roles", tenantId)).json(params),
        options,
        Role.class);
  }

  /**
   * Renames a custom role or changes its description.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   * @param params the parameters
   * @return the role
   */
  public Role update(String tenantId, String roleId, RoleUpdateParams params) {
    return update(tenantId, roleId, params, RequestOptions.none());
  }

  /**
   * Renames a custom role or changes its description. With per-call settings: see {@link
   * #update(String, String, RoleUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   * @param params the parameters
   * @param options per-call settings
   * @return the role
   */
  public Role update(
      String tenantId, String roleId, RoleUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PATCH", Paths.of("/v1/tenants/{tenantId}/roles/{roleId}", tenantId, roleId))
            .json(params),
        options,
        Role.class);
  }

  /**
   * Deletes a custom role. Refused with 409 {@code rbac.role_in_use} while anyone holds it.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   */
  public void delete(String tenantId, String roleId) {
    delete(tenantId, roleId, RequestOptions.none());
  }

  /**
   * Deletes a custom role. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String roleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/roles/{roleId}", tenantId, roleId)),
        options);
  }

  /**
   * Lists the permissions the role grants.
   *
   * @param roleId the role's id
   * @return the list
   */
  public ListResult<Permission> listPermissions(String roleId) {
    return listPermissions(roleId, RequestOptions.none());
  }

  /**
   * Lists the permissions the role grants. With per-call settings: see {@link
   * #listPermissions(String)}.
   *
   * @param roleId the role's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Permission> listPermissions(String roleId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/roles/{roleId}/permissions", roleId)),
        Permission.class,
        options);
  }

  /**
   * Makes {@code permissionIds} the whole of a custom role's permissions, in one step, and returns
   * them.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   * @param permissionIds the permissions the role grants from now on
   * @return the role's permissions
   */
  public ListResult<Permission> setPermissions(
      String tenantId, String roleId, List<String> permissionIds) {
    return setPermissions(tenantId, roleId, permissionIds, RequestOptions.none());
  }

  /**
   * Replaces a role's permissions, with per-call settings. See {@link #setPermissions(String,
   * String, List)}.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   * @param permissionIds the permissions the role grants from now on
   * @param options per-call settings
   * @return the role's permissions
   */
  public ListResult<Permission> setPermissions(
      String tenantId, String roleId, List<String> permissionIds, RequestOptions options) {
    ApiRequest request =
        ApiRequest.of(
                "PUT",
                Paths.of("/v1/tenants/{tenantId}/roles/{roleId}/permissions", tenantId, roleId))
            .json(Bodies.of("permission_ids", List.copyOf(permissionIds)));
    return http.list(request, Permission.class, options);
  }

  /**
   * Grants one permission to a role. Granting it twice changes nothing.
   *
   * @param roleId the role's id
   * @param permissionId the permission's id
   */
  public void grantPermission(String roleId, String permissionId) {
    grantPermission(roleId, permissionId, RequestOptions.none());
  }

  /**
   * Grants one permission to a role. With per-call settings: see {@link #grantPermission(String,
   * String)}.
   *
   * @param roleId the role's id
   * @param permissionId the permission's id
   * @param options per-call settings
   */
  public void grantPermission(String roleId, String permissionId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of("/v1/roles/{roleId}/permissions/{permissionId}", roleId, permissionId)),
        options);
  }

  /**
   * Revoke permission.
   *
   * @param roleId the role's id
   * @param permissionId the permission's id
   */
  public void revokePermission(String roleId, String permissionId) {
    revokePermission(roleId, permissionId, RequestOptions.none());
  }

  /**
   * Revoke permission. With per-call settings: see {@link #revokePermission(String, String)}.
   *
   * @param roleId the role's id
   * @param permissionId the permission's id
   * @param options per-call settings
   */
  public void revokePermission(String roleId, String permissionId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of("/v1/roles/{roleId}/permissions/{permissionId}", roleId, permissionId)),
        options);
  }

  /**
   * One page of the people holding the role, and every group granted it.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   * @return the role holders
   */
  public RoleHolders listHolders(String tenantId, String roleId) {
    return listHolders(
        tenantId, roleId, RoleHoldersParams.builder().build(), RequestOptions.none());
  }

  /**
   * One page of the people holding the role, and every group granted it.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   * @param params the parameters
   * @return the role holders
   */
  public RoleHolders listHolders(String tenantId, String roleId, RoleHoldersParams params) {
    return listHolders(tenantId, roleId, params, RequestOptions.none());
  }

  /**
   * One page of the people holding the role, and every group granted it. With per-call settings:
   * see {@link #listHolders(String, String, RoleHoldersParams)}.
   *
   * @param tenantId the organisation's id
   * @param roleId the role's id
   * @param params the parameters
   * @param options per-call settings
   * @return the role holders
   */
  public RoleHolders listHolders(
      String tenantId, String roleId, RoleHoldersParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/roles/{roleId}/holders", tenantId, roleId))
            .query(params),
        options,
        RoleHolders.class);
  }

  /**
   * Assigns a role to a user in the organisation. Assigning it twice changes nothing.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @param roleId the role's id
   */
  public void assign(String tenantId, String userId, String roleId) {
    assign(tenantId, userId, roleId, RequestOptions.none());
  }

  /**
   * Assigns a role to a user in the organisation. With per-call settings: see {@link
   * #assign(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @param roleId the role's id
   * @param options per-call settings
   */
  public void assign(String tenantId, String userId, String roleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/users/{userId}/tenants/{tenantId}/roles/{roleId}", userId, tenantId, roleId)),
        options);
  }

  /**
   * Removes a directly assigned role from a user. A group may still grant it.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @param roleId the role's id
   */
  public void unassign(String tenantId, String userId, String roleId) {
    unassign(tenantId, userId, roleId, RequestOptions.none());
  }

  /**
   * Removes a directly assigned role from a user. With per-call settings: see {@link
   * #unassign(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @param roleId the role's id
   * @param options per-call settings
   */
  public void unassign(String tenantId, String userId, String roleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/users/{userId}/tenants/{tenantId}/roles/{roleId}", userId, tenantId, roleId)),
        options);
  }
}
