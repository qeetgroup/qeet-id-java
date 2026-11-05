package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages AI agent identities: their tokens, secrets and lifecycle.
 *
 * <p>Get it from {@code qeetId.agents()}. Safe for concurrent use.
 */
public final class Agents {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.agents()}.
   *
   * @param http the client's transport
   */
  public Agents(Transport http) {
    this.http = http;
  }

  /**
   * Creates an agent. The API makes it idempotent: an {@code Idempotency-Key} is sent with it,
   * generated per call unless you give one in {@link RequestOptions}, so a retry never acts twice.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the agent
   */
  public Agent create(String tenantId, AgentCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates an agent. With per-call settings: see {@link #create(String, AgentCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the agent
   */
  public Agent create(String tenantId, AgentCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/agents", tenantId))
            .json(params)
            .idempotent(),
        options,
        Agent.class);
  }

  /**
   * Lists the agents.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<Agent> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the agents. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Agent> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/agents", tenantId)), Agent.class, options);
  }

  /**
   * Deletes an agent.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   */
  public void delete(String tenantId, String agentId) {
    delete(tenantId, agentId, RequestOptions.none());
  }

  /**
   * Deletes an agent. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String agentId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/agents/{agentId}", tenantId, agentId)),
        options);
  }

  /**
   * Suspends ({@code true}) or resumes ({@code false}) the agent. Prefer {@code suspend} and {@code
   * resume}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @param disabled the disabled
   */
  public void setDisabled(String tenantId, String agentId, boolean disabled) {
    setDisabled(tenantId, agentId, disabled, RequestOptions.none());
  }

  /**
   * Suspends ({@code true}) or resumes ({@code false}) the agent. With per-call settings: see
   * {@link #setDisabled(String, String, boolean)}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @param disabled the disabled
   * @param options per-call settings
   */
  public void setDisabled(
      String tenantId, String agentId, boolean disabled, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
                "PATCH", Paths.of("/v1/tenants/{tenantId}/agents/{agentId}", tenantId, agentId))
            .json(Bodies.of("disabled", disabled)),
        options);
  }

  /**
   * Suspends the agent: its tokens stop working at once. Reversible with {@code resume}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @return the agent's new state, {@code suspended}
   */
  public String suspend(String tenantId, String agentId) {
    return suspend(tenantId, agentId, RequestOptions.none());
  }

  /**
   * Suspends the agent: its tokens stop working at once. With per-call settings: see {@link
   * #suspend(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @param options per-call settings
   * @return the agent's new state, {@code suspended}
   */
  public String suspend(String tenantId, String agentId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/agents/{agentId}/suspend", tenantId, agentId)),
            options)
        .path("status")
        .asText();
  }

  /**
   * Resume.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @return the agent's new state, {@code active}
   */
  public String resume(String tenantId, String agentId) {
    return resume(tenantId, agentId, RequestOptions.none());
  }

  /**
   * Resume. With per-call settings: see {@link #resume(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @param options per-call settings
   * @return the agent's new state, {@code active}
   */
  public String resume(String tenantId, String agentId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/agents/{agentId}/resume", tenantId, agentId)),
            options)
        .path("status")
        .asText();
  }

  /**
   * Retires the agent for good.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @return the agent's new state, {@code decommissioned}
   */
  public String decommission(String tenantId, String agentId) {
    return decommission(tenantId, agentId, RequestOptions.none());
  }

  /**
   * Retires the agent for good. With per-call settings: see {@link #decommission(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @param options per-call settings
   * @return the agent's new state, {@code decommissioned}
   */
  public String decommission(String tenantId, String agentId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/agents/{agentId}/decommission", tenantId, agentId)),
            options)
        .path("status")
        .asText();
  }

  /**
   * Incident response: suspends every active agent of the organisation at once.
   *
   * @param tenantId the organisation's id
   * @return how many agents were suspended
   */
  public int suspendAll(String tenantId) {
    return suspendAll(tenantId, RequestOptions.none());
  }

  /**
   * Incident response: suspends every active agent of the organisation at once. With per-call
   * settings: see {@link #suspendAll(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return how many agents were suspended
   */
  public int suspendAll(String tenantId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/agents/kill-all", tenantId)), options)
        .path("suspended")
        .asInt();
  }

  /**
   * Resumes every suspended agent of the organisation at once.
   *
   * @param tenantId the organisation's id
   * @return how many agents were resumed
   */
  public int resumeAll(String tenantId) {
    return resumeAll(tenantId, RequestOptions.none());
  }

  /**
   * Resumes every suspended agent of the organisation at once. With per-call settings: see {@link
   * #resumeAll(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return how many agents were resumed
   */
  public int resumeAll(String tenantId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/agents/resume-all", tenantId)),
            options)
        .path("resumed")
        .asInt();
  }

  /**
   * Issues a new secret, shown once; the old one keeps working for {@code overlap_hours}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @return the agent secret
   */
  public AgentSecret rotateSecret(String tenantId, String agentId) {
    return rotateSecret(
        tenantId, agentId, AgentRotateSecretParams.builder().build(), RequestOptions.none());
  }

  /**
   * Issues a new secret, shown once; the old one keeps working for {@code overlap_hours}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @param params the parameters
   * @return the agent secret
   */
  public AgentSecret rotateSecret(String tenantId, String agentId, AgentRotateSecretParams params) {
    return rotateSecret(tenantId, agentId, params, RequestOptions.none());
  }

  /**
   * Issues a new secret, shown once; the old one keeps working for {@code overlap_hours}. With
   * per-call settings: see {@link #rotateSecret(String, String, AgentRotateSecretParams)}.
   *
   * @param tenantId the organisation's id
   * @param agentId the agent's id
   * @param params the parameters
   * @param options per-call settings
   * @return the agent secret
   */
  public AgentSecret rotateSecret(
      String tenantId, String agentId, AgentRotateSecretParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/agents/{agentId}/rotate-secret", tenantId, agentId))
            .json(params),
        options,
        AgentSecret.class);
  }

  /**
   * Lists the agents a person sponsors.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @return the list
   */
  public ListResult<Agent> listSponsoredBy(String tenantId, String userId) {
    return listSponsoredBy(tenantId, userId, RequestOptions.none());
  }

  /**
   * Lists the agents a person sponsors. With per-call settings: see {@link #listSponsoredBy(String,
   * String)}.
   *
   * @param tenantId the organisation's id
   * @param userId the user's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Agent> listSponsoredBy(String tenantId, String userId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of("/v1/tenants/{tenantId}/agents/sponsored-by/{userId}", tenantId, userId)),
        Agent.class,
        options);
  }

  /**
   * Gives every agent a person sponsors to another sponsor, as when the person leaves.
   *
   * @param tenantId the organisation's id
   * @param fromUserId the from user's id
   * @param toUserId the to user id
   * @return how many agents changed sponsor
   */
  public int transferSponsorship(String tenantId, String fromUserId, String toUserId) {
    return transferSponsorship(tenantId, fromUserId, toUserId, RequestOptions.none());
  }

  /**
   * Gives every agent a person sponsors to another sponsor, as when the person leaves. With
   * per-call settings: see {@link #transferSponsorship(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param fromUserId the from user's id
   * @param toUserId the to user id
   * @param options per-call settings
   * @return how many agents changed sponsor
   */
  public int transferSponsorship(
      String tenantId, String fromUserId, String toUserId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                    Paths.of(
                        "/v1/tenants/{tenantId}/agents/sponsored-by/{fromUserId}/transfer",
                        tenantId,
                        fromUserId))
                .json(Bodies.of("to_user_id", toUserId)),
            options)
        .path("transferred")
        .asInt();
  }

  /**
   * Gets a short-lived access token for an agent from its id and secret. The token carries the
   * agent's scopes. Sends no API key.
   *
   * @param agentId the agent id
   * @param secret the secret
   * @return the agent token
   */
  public AgentToken createToken(String agentId, String secret) {
    return createToken(agentId, secret, RequestOptions.none());
  }

  /**
   * Gets a short-lived access token for an agent from its id and secret. With per-call settings:
   * see {@link #createToken(String, String)}.
   *
   * @param agentId the agent id
   * @param secret the secret
   * @param options per-call settings
   * @return the agent token
   */
  public AgentToken createToken(String agentId, String secret, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/agents/token")
            .json(Bodies.of("agent_id", agentId, "secret", secret))
            .unauthenticated(),
        options,
        AgentToken.class);
  }
}
