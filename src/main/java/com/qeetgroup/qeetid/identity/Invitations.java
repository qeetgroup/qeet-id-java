package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages invitations. Accepting one is the invitee's own action and isn't wrapped here.
 *
 * <p>Get it from {@code qeetId.invitations()}. Safe for concurrent use.
 */
public final class Invitations {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.invitations()}.
   *
   * @param http the client's transport
   */
  public Invitations(Transport http) {
    this.http = http;
  }

  /**
   * Creates an invitation and emails its link to the invited address. The API makes it idempotent:
   * an {@code Idempotency-Key} is sent with it, generated per call unless you give one in {@link
   * RequestOptions}, so a retry never acts twice.
   *
   * @param params the parameters
   * @return the invitation result
   */
  public InvitationResult create(InvitationCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /**
   * Creates an invitation and emails its link to the invited address. With per-call settings: see
   * {@link #create(InvitationCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the invitation result
   */
  public InvitationResult create(InvitationCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/invites").json(params).idempotent(), options, InvitationResult.class);
  }

  /**
   * Lists the invitations.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<Invitation> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the invitations. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Invitation> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/invites", tenantId)),
        Invitation.class,
        options);
  }

  /**
   * Issues a new link, extends the expiry and emails the invitation again.
   *
   * @param invitationId the invitation's id
   * @return the invitation result
   */
  public InvitationResult resend(String invitationId) {
    return resend(invitationId, RequestOptions.none());
  }

  /**
   * Issues a new link, extends the expiry and emails the invitation again. With per-call settings:
   * see {@link #resend(String)}.
   *
   * @param invitationId the invitation's id
   * @param options per-call settings
   * @return the invitation result
   */
  public InvitationResult resend(String invitationId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/invites/{invitationId}/resend", invitationId)),
        options,
        InvitationResult.class);
  }

  /**
   * Withdraws a pending invitation; its link stops working.
   *
   * @param invitationId the invitation's id
   */
  public void delete(String invitationId) {
    delete(invitationId, RequestOptions.none());
  }

  /**
   * Withdraws a pending invitation; its link stops working. With per-call settings: see {@link
   * #delete(String)}.
   *
   * @param invitationId the invitation's id
   * @param options per-call settings
   */
  public void delete(String invitationId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/invites/{invitationId}", invitationId)), options);
  }
}
