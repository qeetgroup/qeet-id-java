package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

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
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the agent
   */
  public Agent create(String organizationId, AgentCreateParams params) {
    return create(organizationId, params, RequestOptions.none());
  }

  /**
   * Creates an agent. With per-call settings: see {@link #create(String, AgentCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the agent
   */
  public Agent create(String organizationId, AgentCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/agents", organizationId))
            .json(params)
            .idempotent(),
        options,
        Agent.class);
  }

  /**
   * Lists the agents.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<Agent> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the agents. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Agent> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/agents", organizationId)),
        Agent.class,
        options);
  }

  /**
   * Deletes an agent.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   */
  public void delete(String organizationId, String agentId) {
    delete(organizationId, agentId, RequestOptions.none());
  }

  /**
   * Deletes an agent. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @param options per-call settings
   */
  public void delete(String organizationId, String agentId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/agents/{agentId}", organizationId, agentId)),
        options);
  }

  /**
   * Suspends ({@code true}) or resumes ({@code false}) the agent. Prefer {@code suspend} and {@code
   * resume}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @param disabled the disabled
   */
  public void setDisabled(String organizationId, String agentId, boolean disabled) {
    setDisabled(organizationId, agentId, disabled, RequestOptions.none());
  }

  /**
   * Suspends ({@code true}) or resumes ({@code false}) the agent. With per-call settings: see
   * {@link #setDisabled(String, String, boolean)}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @param disabled the disabled
   * @param options per-call settings
   */
  public void setDisabled(
      String organizationId, String agentId, boolean disabled, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/organizations/{organizationId}/agents/{agentId}", organizationId, agentId))
            .json(Bodies.of("disabled", disabled)),
        options);
  }

  /**
   * Suspends the agent: its tokens stop working at once. Reversible with {@code resume}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @return the agent's new state, {@code suspended}
   */
  public String suspend(String organizationId, String agentId) {
    return suspend(organizationId, agentId, RequestOptions.none());
  }

  /**
   * Suspends the agent: its tokens stop working at once. With per-call settings: see {@link
   * #suspend(String, String)}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @param options per-call settings
   * @return the agent's new state, {@code suspended}
   */
  public String suspend(String organizationId, String agentId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/agents/{agentId}/suspend",
                    organizationId,
                    agentId)),
            options)
        .path("status")
        .asText();
  }

  /**
   * Resume.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @return the agent's new state, {@code active}
   */
  public String resume(String organizationId, String agentId) {
    return resume(organizationId, agentId, RequestOptions.none());
  }

  /**
   * Resume. With per-call settings: see {@link #resume(String, String)}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @param options per-call settings
   * @return the agent's new state, {@code active}
   */
  public String resume(String organizationId, String agentId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/agents/{agentId}/resume",
                    organizationId,
                    agentId)),
            options)
        .path("status")
        .asText();
  }

  /**
   * Retires the agent for good.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @return the agent's new state, {@code decommissioned}
   */
  public String decommission(String organizationId, String agentId) {
    return decommission(organizationId, agentId, RequestOptions.none());
  }

  /**
   * Retires the agent for good. With per-call settings: see {@link #decommission(String, String)}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @param options per-call settings
   * @return the agent's new state, {@code decommissioned}
   */
  public String decommission(String organizationId, String agentId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/agents/{agentId}/decommission",
                    organizationId,
                    agentId)),
            options)
        .path("status")
        .asText();
  }

  /**
   * Incident response: suspends every active agent of the organization at once.
   *
   * @param organizationId the organization's id
   * @return how many agents were suspended
   */
  public int suspendAll(String organizationId) {
    return suspendAll(organizationId, RequestOptions.none());
  }

  /**
   * Incident response: suspends every active agent of the organization at once. With per-call
   * settings: see {@link #suspendAll(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return how many agents were suspended
   */
  public int suspendAll(String organizationId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/agents/kill-all", organizationId)),
            options)
        .path("suspended")
        .asInt();
  }

  /**
   * Resumes every suspended agent of the organization at once.
   *
   * @param organizationId the organization's id
   * @return how many agents were resumed
   */
  public int resumeAll(String organizationId) {
    return resumeAll(organizationId, RequestOptions.none());
  }

  /**
   * Resumes every suspended agent of the organization at once. With per-call settings: see {@link
   * #resumeAll(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return how many agents were resumed
   */
  public int resumeAll(String organizationId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/agents/resume-all", organizationId)),
            options)
        .path("resumed")
        .asInt();
  }

  /**
   * Issues a new secret, shown once; the old one keeps working for {@code overlap_hours}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @return the agent secret
   */
  public AgentSecret rotateSecret(String organizationId, String agentId) {
    return rotateSecret(
        organizationId, agentId, AgentRotateSecretParams.builder().build(), RequestOptions.none());
  }

  /**
   * Issues a new secret, shown once; the old one keeps working for {@code overlap_hours}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @param params the parameters
   * @return the agent secret
   */
  public AgentSecret rotateSecret(
      String organizationId, String agentId, AgentRotateSecretParams params) {
    return rotateSecret(organizationId, agentId, params, RequestOptions.none());
  }

  /**
   * Issues a new secret, shown once; the old one keeps working for {@code overlap_hours}. With
   * per-call settings: see {@link #rotateSecret(String, String, AgentRotateSecretParams)}.
   *
   * @param organizationId the organization's id
   * @param agentId the agent's id
   * @param params the parameters
   * @param options per-call settings
   * @return the agent secret
   */
  public AgentSecret rotateSecret(
      String organizationId,
      String agentId,
      AgentRotateSecretParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/agents/{agentId}/rotate-secret",
                    organizationId,
                    agentId))
            .json(params),
        options,
        AgentSecret.class);
  }

  /**
   * Lists the agents a person sponsors.
   *
   * @param organizationId the organization's id
   * @param userId the user's id
   * @return the list
   */
  public ListResult<Agent> listSponsoredBy(String organizationId, String userId) {
    return listSponsoredBy(organizationId, userId, RequestOptions.none());
  }

  /**
   * Lists the agents a person sponsors. With per-call settings: see {@link #listSponsoredBy(String,
   * String)}.
   *
   * @param organizationId the organization's id
   * @param userId the user's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<Agent> listSponsoredBy(
      String organizationId, String userId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of(
                "/v1/organizations/{organizationId}/agents/sponsored-by/{userId}",
                organizationId,
                userId)),
        Agent.class,
        options);
  }

  /**
   * Gives every agent a person sponsors to another sponsor, as when the person leaves.
   *
   * @param organizationId the organization's id
   * @param fromUserId the from user's id
   * @param toUserId the to user id
   * @return how many agents changed sponsor
   */
  public int transferSponsorship(String organizationId, String fromUserId, String toUserId) {
    return transferSponsorship(organizationId, fromUserId, toUserId, RequestOptions.none());
  }

  /**
   * Gives every agent a person sponsors to another sponsor, as when the person leaves. With
   * per-call settings: see {@link #transferSponsorship(String, String, String)}.
   *
   * @param organizationId the organization's id
   * @param fromUserId the from user's id
   * @param toUserId the to user id
   * @param options per-call settings
   * @return how many agents changed sponsor
   */
  public int transferSponsorship(
      String organizationId, String fromUserId, String toUserId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                    Paths.of(
                        "/v1/organizations/{organizationId}/agents/sponsored-by/{fromUserId}/transfer",
                        organizationId,
                        fromUserId))
                .json(Bodies.of("to_user_id", toUserId)),
            options)
        .path("transferred")
        .asInt();
  }

  /**
   * Gets a short-lived access token for an agent from its id and secret: the OAuth {@code
   * client_credentials} grant at {@code POST /v1/oauth/token}, with the agent's id as {@code
   * client_id} and its secret as {@code client_secret}. The token carries the agent's scopes. Sends
   * no API key. A wrong secret throws an {@link com.qeetgroup.qeetid.exception.OAuthException} with
   * code {@code invalid_client}, an agent that isn't active one with {@code unauthorized_client}.
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
    Map<String, @Nullable String> form = new LinkedHashMap<>();
    form.put("grant_type", "client_credentials");
    form.put("client_id", Objects.requireNonNull(agentId, "agentId"));
    form.put("client_secret", Objects.requireNonNull(secret, "secret"));
    // An OAuth grant, so never retried, as every grant is.
    return http.execute(
        ApiRequest.post("/v1/oauth/token").form(form).unauthenticated().neverRetry(),
        options,
        AgentToken.class);
  }
}
