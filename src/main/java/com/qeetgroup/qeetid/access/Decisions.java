package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * OpenID AuthZEN evaluations over every authorization model, and the log of decisions made.
 *
 * <p>Get it from {@code qeetId.decisions()}. Safe for concurrent use.
 */
public final class Decisions {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.decisions()}.
   *
   * @param http the client's transport
   */
  public Decisions(Transport http) {
    this.http = http;
  }

  /**
   * Evaluates one AuthZEN request.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the evaluation
   */
  public Evaluation evaluate(String organizationId, EvaluationParams params) {
    return evaluate(organizationId, params, RequestOptions.none());
  }

  /**
   * Evaluates one AuthZEN request. With per-call settings: see {@link #evaluate(String,
   * EvaluationParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the evaluation
   */
  public Evaluation evaluate(
      String organizationId, EvaluationParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/access/v1/evaluation", organizationId))
            .json(params)
            .retrySafe(),
        options,
        Evaluation.class);
  }

  /**
   * Evaluates several AuthZEN requests; decisions come back in request order.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the evaluations
   */
  public Evaluations evaluateMany(String organizationId, EvaluationsParams params) {
    return evaluateMany(organizationId, params, RequestOptions.none());
  }

  /**
   * Evaluates several AuthZEN requests; decisions come back in request order. With per-call
   * settings: see {@link #evaluateMany(String, EvaluationsParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the evaluations
   */
  public Evaluations evaluateMany(
      String organizationId, EvaluationsParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/access/v1/evaluations", organizationId))
            .json(params)
            .retrySafe(),
        options,
        Evaluations.class);
  }

  /**
   * The access decisions the organization asked for, newest first.
   *
   * @param organizationId the organization's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<DecisionLogEntry> list(String organizationId) {
    return list(organizationId, DecisionListParams.builder().build(), RequestOptions.none());
  }

  /**
   * The access decisions the organization asked for, newest first.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<DecisionLogEntry> list(String organizationId, DecisionListParams params) {
    return list(organizationId, params, RequestOptions.none());
  }

  /**
   * The access decisions the organization asked for, newest first. With per-call settings: see
   * {@link #list(String, DecisionListParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<DecisionLogEntry> list(
      String organizationId, DecisionListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/organizations/{organizationId}/authorization/decisions", organizationId),
        params,
        DecisionLogEntry.class,
        options);
  }
}
