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
   * Lists the organization's roles, built-in and custom.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<Role> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the organization's roles, built-in and custom. With per-call settings: see {@link
   * #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Role> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/roles", organizationId)),
        Role.class,
        options);
  }

  /**
   * Creates a custom role with no permissions; grant them with {@code grantPermission} or {@code
   * setPermissions}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the role
   */
  public Role create(String organizationId, RoleCreateParams params) {
    return create(organizationId, params, RequestOptions.none());
  }

  /**
   * Creates a custom role with no permissions; grant them with {@code grantPermission} or {@code
   * setPermissions}. With per-call settings: see {@link #create(String, RoleCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the role
   */
  public Role create(String organizationId, RoleCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/roles", organizationId))
            .json(params),
        options,
        Role.class);
  }

  /**
   * Renames a custom role or changes its description.
   *
   * @param organizationId the organization's id
   * @param roleId the role's id
   * @param params the parameters
   * @return the role
   */
  public Role update(String organizationId, String roleId, RoleUpdateParams params) {
    return update(organizationId, roleId, params, RequestOptions.none());
  }

  /**
   * Renames a custom role or changes its description. With per-call settings: see {@link
   * #update(String, String, RoleUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param roleId the role's id
   * @param params the parameters
   * @param options per-call settings
   * @return the role
   */
  public Role update(
      String organizationId, String roleId, RoleUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/organizations/{organizationId}/roles/{roleId}", organizationId, roleId))
            .json(params),
        options,
        Role.class);
  }

  /**
   * Deletes a custom role. Refused with 409 {@code rbac.role_in_use} while anyone holds it.
   *
   * @param organizationId the organization's id
   * @param roleId the role's id
   */
  public void delete(String organizationId, String roleId) {
    delete(organizationId, roleId, RequestOptions.none());
  }

  /**
   * Deletes a custom role. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param organizationId the organization's id
   * @param roleId the role's id
   * @param options per-call settings
   */
  public void delete(String organizationId, String roleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of("/v1/organizations/{organizationId}/roles/{roleId}", organizationId, roleId)),
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
   * @param organizationId the organization's id
   * @param roleId the role's id
   * @param permissionIds the permissions the role grants from now on
   * @return the role's permissions
   */
  public ListResult<Permission> setPermissions(
      String organizationId, String roleId, List<String> permissionIds) {
    return setPermissions(organizationId, roleId, permissionIds, RequestOptions.none());
  }

  /**
   * Replaces a role's permissions, with per-call settings. See {@link #setPermissions(String,
   * String, List)}.
   *
   * @param organizationId the organization's id
   * @param roleId the role's id
   * @param permissionIds the permissions the role grants from now on
   * @param options per-call settings
   * @return the role's permissions
   */
  public ListResult<Permission> setPermissions(
      String organizationId, String roleId, List<String> permissionIds, RequestOptions options) {
    ApiRequest request =
        ApiRequest.of(
                "PUT",
                Paths.of(
                    "/v1/organizations/{organizationId}/roles/{roleId}/permissions",
                    organizationId,
                    roleId))
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
   * @param organizationId the organization's id
   * @param roleId the role's id
   * @return the role holders
   */
  public RoleHolders listHolders(String organizationId, String roleId) {
    return listHolders(
        organizationId, roleId, RoleHoldersParams.builder().build(), RequestOptions.none());
  }

  /**
   * One page of the people holding the role, and every group granted it.
   *
   * @param organizationId the organization's id
   * @param roleId the role's id
   * @param params the parameters
   * @return the role holders
   */
  public RoleHolders listHolders(String organizationId, String roleId, RoleHoldersParams params) {
    return listHolders(organizationId, roleId, params, RequestOptions.none());
  }

  /**
   * One page of the people holding the role, and every group granted it. With per-call settings:
   * see {@link #listHolders(String, String, RoleHoldersParams)}.
   *
   * @param organizationId the organization's id
   * @param roleId the role's id
   * @param params the parameters
   * @param options per-call settings
   * @return the role holders
   */
  public RoleHolders listHolders(
      String organizationId, String roleId, RoleHoldersParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
                Paths.of(
                    "/v1/organizations/{organizationId}/roles/{roleId}/holders",
                    organizationId,
                    roleId))
            .query(params),
        options,
        RoleHolders.class);
  }

  /**
   * Assigns a role to a user in the organization. Assigning it twice changes nothing.
   *
   * @param organizationId the organization's id
   * @param userId the user's id
   * @param roleId the role's id
   */
  public void assign(String organizationId, String userId, String roleId) {
    assign(organizationId, userId, roleId, RequestOptions.none());
  }

  /**
   * Assigns a role to a user in the organization. With per-call settings: see {@link
   * #assign(String, String, String)}.
   *
   * @param organizationId the organization's id
   * @param userId the user's id
   * @param roleId the role's id
   * @param options per-call settings
   */
  public void assign(String organizationId, String userId, String roleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/users/{userId}/organizations/{organizationId}/roles/{roleId}",
                userId,
                organizationId,
                roleId)),
        options);
  }

  /**
   * Removes a directly assigned role from a user. A group may still grant it.
   *
   * @param organizationId the organization's id
   * @param userId the user's id
   * @param roleId the role's id
   */
  public void unassign(String organizationId, String userId, String roleId) {
    unassign(organizationId, userId, roleId, RequestOptions.none());
  }

  /**
   * Removes a directly assigned role from a user. With per-call settings: see {@link
   * #unassign(String, String, String)}.
   *
   * @param organizationId the organization's id
   * @param userId the user's id
   * @param roleId the role's id
   * @param options per-call settings
   */
  public void unassign(
      String organizationId, String userId, String roleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/users/{userId}/organizations/{organizationId}/roles/{roleId}",
                userId,
                organizationId,
                roleId)),
        options);
  }
}
