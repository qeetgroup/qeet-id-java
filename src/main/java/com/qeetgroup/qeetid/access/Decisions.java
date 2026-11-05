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
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the evaluation
   */
  public Evaluation evaluate(String tenantId, EvaluationParams params) {
    return evaluate(tenantId, params, RequestOptions.none());
  }

  /**
   * Evaluates one AuthZEN request. With per-call settings: see {@link #evaluate(String,
   * EvaluationParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the evaluation
   */
  public Evaluation evaluate(String tenantId, EvaluationParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/access/v1/evaluation", tenantId))
            .json(params)
            .retrySafe(),
        options,
        Evaluation.class);
  }

  /**
   * Evaluates several AuthZEN requests; decisions come back in request order.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the evaluations
   */
  public Evaluations evaluateMany(String tenantId, EvaluationsParams params) {
    return evaluateMany(tenantId, params, RequestOptions.none());
  }

  /**
   * Evaluates several AuthZEN requests; decisions come back in request order. With per-call
   * settings: see {@link #evaluateMany(String, EvaluationsParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the evaluations
   */
  public Evaluations evaluateMany(
      String tenantId, EvaluationsParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/access/v1/evaluations", tenantId))
            .json(params)
            .retrySafe(),
        options,
        Evaluations.class);
  }

  /**
   * The access decisions the organisation asked for, newest first.
   *
   * @param tenantId the organisation's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<DecisionLogEntry> list(String tenantId) {
    return list(tenantId, DecisionListParams.builder().build(), RequestOptions.none());
  }

  /**
   * The access decisions the organisation asked for, newest first.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<DecisionLogEntry> list(String tenantId, DecisionListParams params) {
    return list(tenantId, params, RequestOptions.none());
  }

  /**
   * The access decisions the organisation asked for, newest first. With per-call settings: see
   * {@link #list(String, DecisionListParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<DecisionLogEntry> list(
      String tenantId, DecisionListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/tenants/{tenantId}/authorization/decisions", tenantId),
        params,
        DecisionLogEntry.class,
        options);
  }
}
