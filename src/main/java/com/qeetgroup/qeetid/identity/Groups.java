package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages groups, their members and the roles they grant.
 *
 * <p>Get it from {@code qeetId.groups()}. Safe for concurrent use.
 */
public final class Groups {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.groups()}.
   *
   * @param http the client's transport
   */
  public Groups(Transport http) {
    this.http = http;
  }

  /**
   * Creates a group. The API makes it idempotent: an {@code Idempotency-Key} is sent with it,
   * generated per call unless you give one in {@link RequestOptions}, so a retry never acts twice.
   *
   * @param params the parameters
   * @return the group
   */
  public Group create(GroupCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /**
   * Creates a group. With per-call settings: see {@link #create(GroupCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the group
   */
  public Group create(GroupCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/groups").json(params).idempotent(), options, Group.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param groupId the group's id
   * @param params the parameters
   * @return the group
   */
  public Group update(String groupId, GroupUpdateParams params) {
    return update(groupId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String,
   * GroupUpdateParams)}.
   *
   * @param groupId the group's id
   * @param params the parameters
   * @param options per-call settings
   * @return the group
   */
  public Group update(String groupId, GroupUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PATCH", Paths.of("/v1/groups/{groupId}", groupId)).json(params),
        options,
        Group.class);
  }

  /**
   * Deletes the group and its subgroups. Members keep their accounts.
   *
   * @param groupId the group's id
   */
  public void delete(String groupId) {
    delete(groupId, RequestOptions.none());
  }

  /**
   * Deletes the group and its subgroups. With per-call settings: see {@link #delete(String)}.
   *
   * @param groupId the group's id
   * @param options per-call settings
   */
  public void delete(String groupId, RequestOptions options) {
    http.executeVoid(ApiRequest.of("DELETE", Paths.of("/v1/groups/{groupId}", groupId)), options);
  }

  /**
   * Lists the groups.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<Group> list(String tenantId) {
    return list(tenantId, GroupListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists the groups.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the list
   */
  public ListResult<Group> list(String tenantId, GroupListParams params) {
    return list(tenantId, params, RequestOptions.none());
  }

  /**
   * Lists the groups. With per-call settings: see {@link #list(String, GroupListParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Group> list(String tenantId, GroupListParams params, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/groups", tenantId)).query(params),
        Group.class,
        options);
  }

  /**
   * List members.
   *
   * @param groupId the group's id
   * @return the list
   */
  public ListResult<GroupMember> listMembers(String groupId) {
    return listMembers(groupId, RequestOptions.none());
  }

  /**
   * List members. With per-call settings: see {@link #listMembers(String)}.
   *
   * @param groupId the group's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<GroupMember> listMembers(String groupId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/groups/{groupId}/members", groupId)),
        GroupMember.class,
        options);
  }

  /**
   * Adds a user to the group, giving them its roles. Adding a member twice changes nothing.
   *
   * @param groupId the group's id
   * @param userId the user's id
   */
  public void addMember(String groupId, String userId) {
    addMember(groupId, userId, RequestOptions.none());
  }

  /**
   * Adds a user to the group, giving them its roles. With per-call settings: see {@link
   * #addMember(String, String)}.
   *
   * @param groupId the group's id
   * @param userId the user's id
   * @param options per-call settings
   */
  public void addMember(String groupId, String userId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(Paths.of("/v1/groups/{groupId}/members/{userId}", groupId, userId)),
        options);
  }

  /**
   * Remove member.
   *
   * @param groupId the group's id
   * @param userId the user's id
   */
  public void removeMember(String groupId, String userId) {
    removeMember(groupId, userId, RequestOptions.none());
  }

  /**
   * Remove member. With per-call settings: see {@link #removeMember(String, String)}.
   *
   * @param groupId the group's id
   * @param userId the user's id
   * @param options per-call settings
   */
  public void removeMember(String groupId, String userId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/groups/{groupId}/members/{userId}", groupId, userId)),
        options);
  }

  /**
   * Lists the roles granted to the group.
   *
   * @param tenantId the organisation's id
   * @param groupId the group's id
   * @return the list
   */
  public ListResult<GroupRole> listRoles(String tenantId, String groupId) {
    return listRoles(tenantId, groupId, RequestOptions.none());
  }

  /**
   * Lists the roles granted to the group. With per-call settings: see {@link #listRoles(String,
   * String)}.
   *
   * @param tenantId the organisation's id
   * @param groupId the group's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<GroupRole> listRoles(String tenantId, String groupId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of("/v1/tenants/{tenantId}/groups/{groupId}/roles", tenantId, groupId)),
        GroupRole.class,
        options);
  }

  /**
   * Grants a role to the group; every member inherits it.
   *
   * @param tenantId the organisation's id
   * @param groupId the group's id
   * @param roleId the role's id
   */
  public void grantRole(String tenantId, String groupId, String roleId) {
    grantRole(tenantId, groupId, roleId, RequestOptions.none());
  }

  /**
   * Grants a role to the group; every member inherits it. With per-call settings: see {@link
   * #grantRole(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param groupId the group's id
   * @param roleId the role's id
   * @param options per-call settings
   */
  public void grantRole(String tenantId, String groupId, String roleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/tenants/{tenantId}/groups/{groupId}/roles/{roleId}",
                tenantId,
                groupId,
                roleId)),
        options);
  }

  /**
   * Revoke role.
   *
   * @param tenantId the organisation's id
   * @param groupId the group's id
   * @param roleId the role's id
   */
  public void revokeRole(String tenantId, String groupId, String roleId) {
    revokeRole(tenantId, groupId, roleId, RequestOptions.none());
  }

  /**
   * Revoke role. With per-call settings: see {@link #revokeRole(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param groupId the group's id
   * @param roleId the role's id
   * @param options per-call settings
   */
  public void revokeRole(String tenantId, String groupId, String roleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/groups/{groupId}/roles/{roleId}",
                tenantId,
                groupId,
                roleId)),
        options);
  }
}
