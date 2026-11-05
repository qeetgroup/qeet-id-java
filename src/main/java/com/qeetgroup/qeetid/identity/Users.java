package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's users.
 *
 * <p>Get it from {@code qeetId.users()}. Safe for concurrent use.
 */
public final class Users {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.users()}.
   *
   * @param http the client's transport
   */
  public Users(Transport http) {
    this.http = http;
  }

  /**
   * Creates a user in the API key's organisation. The API makes it idempotent: an {@code
   * Idempotency-Key} is sent with it, generated per call unless you give one in {@link
   * RequestOptions}, so a retry never acts twice.
   *
   * @param params the parameters
   * @return the user
   */
  public User create(UserCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /**
   * Creates a user in the API key's organisation. With per-call settings: see {@link
   * #create(UserCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the user
   */
  public User create(UserCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/users").json(params).idempotent(), options, User.class);
  }

  /**
   * Fetches a user.
   *
   * @param userId the user's id
   * @return the user
   */
  public User get(String userId) {
    return get(userId, RequestOptions.none());
  }

  /**
   * Fetches a user. With per-call settings: see {@link #get(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the user
   */
  public User get(String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/users/{userId}", userId)), options, User.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param userId the user's id
   * @param params the parameters
   * @return the user
   */
  public User update(String userId, UserUpdateParams params) {
    return update(userId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String,
   * UserUpdateParams)}.
   *
   * @param userId the user's id
   * @param params the parameters
   * @param options per-call settings
   * @return the user
   */
  public User update(String userId, UserUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PATCH", Paths.of("/v1/users/{userId}", userId)).json(params),
        options,
        User.class);
  }

  /**
   * Lists the API key's organisation's users, newest first. The answer is the first page; iterate
   * it, or {@code stream()} it, to walk them all, page by page.
   *
   * @return the first page; iterate it for every item, page by page
   */
  public Page<User> list() {
    return list(UserListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists the API key's organisation's users, newest first. The answer is the first page; iterate
   * it, or {@code stream()} it, to walk them all, page by page.
   *
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<User> list(UserListParams params) {
    return list(params, RequestOptions.none());
  }

  /**
   * Lists the API key's organisation's users, newest first. With per-call settings: see {@link
   * #list(UserListParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<User> list(UserListParams params, RequestOptions options) {
    return http.page("/v1/users", params, User.class, options);
  }

  /**
   * Suspends the user: they can't sign in, and every session of theirs ends.
   *
   * @param userId the user's id
   * @return the user
   */
  public User suspend(String userId) {
    return suspend(userId, RequestOptions.none());
  }

  /**
   * Suspends the user, with per-call settings. See {@link #suspend(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the user
   */
  public User suspend(String userId, RequestOptions options) {
    return update(userId, UserUpdateParams.builder().status("suspended").build(), options);
  }

  /**
   * Reactivates a suspended user. Their ended sessions don't come back.
   *
   * @param userId the user's id
   * @return the user
   */
  public User reactivate(String userId) {
    return reactivate(userId, RequestOptions.none());
  }

  /**
   * Reactivates a suspended user. With per-call settings: see {@link #reactivate(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the user
   */
  public User reactivate(String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/users/{userId}/reactivate", userId)), options, User.class);
  }

  /**
   * Soft-deletes the user into the recycle bin and ends every way they are signed in. Undo with
   * {@code restore}.
   *
   * @param userId the user's id
   * @return the user deletion
   */
  public UserDeletion delete(String userId) {
    return delete(userId, RequestOptions.none());
  }

  /**
   * Soft-deletes the user into the recycle bin and ends every way they are signed in. With per-call
   * settings: see {@link #delete(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the user deletion
   */
  public UserDeletion delete(String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.of("DELETE", Paths.of("/v1/users/{userId}", userId)),
        options,
        UserDeletion.class);
  }

  /**
   * Lists the recycle bin.
   *
   * @return the list
   */
  public ListResult<DeletedUser> listDeleted() {
    return listDeleted(DeletedUserListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists the recycle bin.
   *
   * @param params the parameters
   * @return the list
   */
  public ListResult<DeletedUser> listDeleted(DeletedUserListParams params) {
    return listDeleted(params, RequestOptions.none());
  }

  /**
   * Lists the recycle bin. With per-call settings: see {@link #listDeleted(DeletedUserListParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the list
   */
  public ListResult<DeletedUser> listDeleted(DeletedUserListParams params, RequestOptions options) {
    return http.list(ApiRequest.get("/v1/users/deleted").query(params), DeletedUser.class, options);
  }

  /**
   * Brings a soft-deleted user back.
   *
   * @param userId the user's id
   * @return the user
   */
  public User restore(String userId) {
    return restore(userId, RequestOptions.none());
  }

  /**
   * Brings a soft-deleted user back. With per-call settings: see {@link #restore(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the user
   */
  public User restore(String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/users/{userId}/restore", userId)), options, User.class);
  }

  /**
   * Permanently deletes a soft-deleted user. Irreversible.
   *
   * @param userId the user's id
   */
  public void purge(String userId) {
    purge(userId, RequestOptions.none());
  }

  /**
   * Permanently deletes a soft-deleted user. With per-call settings: see {@link #purge(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   */
  public void purge(String userId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/users/{userId}/purge", userId)), options);
  }

  /**
   * Sets or replaces the user's password, ending every way they are signed in.
   *
   * @param userId the user's id
   * @param password the password
   */
  public void setPassword(String userId, String password) {
    setPassword(userId, password, RequestOptions.none());
  }

  /**
   * Sets or replaces the user's password, ending every way they are signed in. With per-call
   * settings: see {@link #setPassword(String, String)}.
   *
   * @param userId the user's id
   * @param password the password
   * @param options per-call settings
   */
  public void setPassword(String userId, String password, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(Paths.of("/v1/users/{userId}/password", userId))
            .json(Bodies.of("password", password)),
        options);
  }

  /**
   * Clears every second factor of a locked-out user (account recovery).
   *
   * @param userId the user's id
   */
  public void resetMfa(String userId) {
    resetMfa(userId, RequestOptions.none());
  }

  /**
   * Clears every second factor of a locked-out user (account recovery). With per-call settings: see
   * {@link #resetMfa(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   */
  public void resetMfa(String userId, RequestOptions options) {
    http.executeVoid(ApiRequest.of("DELETE", Paths.of("/v1/users/{userId}/mfa", userId)), options);
  }

  /**
   * Requires (or stops requiring) a second factor of this user.
   *
   * @param userId the user's id
   * @param required the required
   * @return whether a second factor is now required
   */
  public boolean setMfaRequired(String userId, boolean required) {
    return setMfaRequired(userId, required, RequestOptions.none());
  }

  /**
   * Requires (or stops requiring) a second factor of this user. With per-call settings: see {@link
   * #setMfaRequired(String, boolean)}.
   *
   * @param userId the user's id
   * @param required the required
   * @param options per-call settings
   * @return whether a second factor is now required
   */
  public boolean setMfaRequired(String userId, boolean required, RequestOptions options) {
    return http.tree(
            ApiRequest.of("PUT", Paths.of("/v1/users/{userId}/mfa-required", userId))
                .json(Bodies.of("required", required)),
            options)
        .path("mfa_required")
        .asBoolean();
  }

  /**
   * Creates up to 1000 users in one call; each row succeeds or fails on its own.
   *
   * @param params the parameters
   * @return the bulk import result
   */
  public BulkImportResult bulkCreate(UserBulkCreateParams params) {
    return bulkCreate(params, RequestOptions.none());
  }

  /**
   * Creates up to 1000 users in one call; each row succeeds or fails on its own. With per-call
   * settings: see {@link #bulkCreate(UserBulkCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the bulk import result
   */
  public BulkImportResult bulkCreate(UserBulkCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/users/bulk").json(params), options, BulkImportResult.class);
  }

  /**
   * Imports another identity provider's user-export file as it is: NDJSON for Auth0, CSV for
   * Cognito, Microsoft Graph JSON for Azure AD B2C. Imported users have no password.
   *
   * @param source the provider the file comes from
   * @param file the file's bytes
   * @return each row's outcome
   */
  public BulkImportResult bulkImport(BulkImportSource source, byte[] file) {
    return bulkImport(source, file, RequestOptions.none());
  }

  /**
   * Imports a user-export file, with per-call settings. See {@link #bulkImport(BulkImportSource,
   * byte[])}.
   *
   * @param source the provider the file comes from
   * @param file the file's bytes
   * @param options per-call settings
   * @return each row's outcome
   */
  public BulkImportResult bulkImport(BulkImportSource source, byte[] file, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post("/v1/users/bulk/import")
            .query("source", source.wireName())
            .raw(file, source.contentType());
    return http.execute(request, options, BulkImportResult.class);
  }

  /**
   * Lists the user's live sessions in the organisation, most recently seen first.
   *
   * @param userId the user's id
   * @return the list
   */
  public ListResult<UserSession> listSessions(String userId) {
    return listSessions(userId, RequestOptions.none());
  }

  /**
   * Lists the user's live sessions in the organisation, most recently seen first. With per-call
   * settings: see {@link #listSessions(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<UserSession> listSessions(String userId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/users/{userId}/sessions", userId)),
        UserSession.class,
        options);
  }

  /**
   * Ends one of the user's sessions.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @param sessionId the session's id
   */
  public void revokeSession(String tenantId, String userId, String sessionId) {
    revokeSession(tenantId, userId, sessionId, RequestOptions.none());
  }

  /**
   * Ends one of the user's sessions. With per-call settings: see {@link #revokeSession(String,
   * String, String)}.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @param sessionId the session's id
   * @param options per-call settings
   */
  public void revokeSession(
      String tenantId, String userId, String sessionId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/users/{userId}/sessions/{sessionId}",
                tenantId,
                userId,
                sessionId)),
        options);
  }

  /**
   * Signs the user out everywhere: sessions, hosted sign-in sessions and application refresh
   * grants.
   *
   * @param userId the user's id
   * @return the user sessions revocation
   */
  public UserSessionsRevocation revokeAllSessions(String userId) {
    return revokeAllSessions(userId, RequestOptions.none());
  }

  /**
   * Signs the user out everywhere: sessions, hosted sign-in sessions and application refresh
   * grants. With per-call settings: see {@link #revokeAllSessions(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the user sessions revocation
   */
  public UserSessionsRevocation revokeAllSessions(String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/users/{userId}/sessions/revoke-all", userId)),
        options,
        UserSessionsRevocation.class);
  }

  /**
   * The user's authentication posture: factors, passkeys, password and sessions.
   *
   * @param userId the user's id
   * @return the user security
   */
  public UserSecurity getSecurity(String userId) {
    return getSecurity(userId, RequestOptions.none());
  }

  /**
   * The user's authentication posture: factors, passkeys, password and sessions. With per-call
   * settings: see {@link #getSecurity(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the user security
   */
  public UserSecurity getSecurity(String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/users/{userId}/security", userId)),
        options,
        UserSecurity.class);
  }

  /**
   * The user's roles, groups and permission counts in the organisation.
   *
   * @param userId the user's id
   * @return the user access
   */
  public UserAccess getAccess(String userId) {
    return getAccess(userId, RequestOptions.none());
  }

  /**
   * The user's roles, groups and permission counts in the organisation. With per-call settings: see
   * {@link #getAccess(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the user access
   */
  public UserAccess getAccess(String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/users/{userId}/access", userId)), options, UserAccess.class);
  }

  /**
   * Removes a member whose account belongs to another organisation from this one. Their account is
   * left alone.
   *
   * @param userId the user's id
   * @return the membership removal
   */
  public MembershipRemoval removeMembership(String userId) {
    return removeMembership(userId, RequestOptions.none());
  }

  /**
   * Removes a member whose account belongs to another organisation from this one. With per-call
   * settings: see {@link #removeMembership(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the membership removal
   */
  public MembershipRemoval removeMembership(String userId, RequestOptions options) {
    return http.execute(
        ApiRequest.of("DELETE", Paths.of("/v1/users/{userId}/membership", userId)),
        options,
        MembershipRemoval.class);
  }

  /**
   * Lists the social accounts linked to the user.
   *
   * @param userId the user's id
   * @return the list
   */
  public ListResult<LinkedIdentity> listLinkedIdentities(String userId) {
    return listLinkedIdentities(userId, RequestOptions.none());
  }

  /**
   * Lists the social accounts linked to the user. With per-call settings: see {@link
   * #listLinkedIdentities(String)}.
   *
   * @param userId the user's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<LinkedIdentity> listLinkedIdentities(String userId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/users/{userId}/social/identities", userId)),
        LinkedIdentity.class,
        options);
  }
}
