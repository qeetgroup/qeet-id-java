package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Behavioural anomalies found in the audit log.
 *
 * <p>Get it from its parent resource. Safe for concurrent use.
 */
public final class AuditAnomalies {
  private final Transport http;

  /**
   * Used by the client; get this from its parent resource.
   *
   * @param http the client's transport
   */
  public AuditAnomalies(Transport http) {
    this.http = http;
  }

  /**
   * Lists the audit anomalies.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<AuditAnomaly> list(String tenantId) {
    return list(tenantId, AuditAnomalyListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists the audit anomalies.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the list
   */
  public ListResult<AuditAnomaly> list(String tenantId, AuditAnomalyListParams params) {
    return list(tenantId, params, RequestOptions.none());
  }

  /**
   * Lists the audit anomalies. With per-call settings: see {@link #list(String,
   * AuditAnomalyListParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the list
   */
  public ListResult<AuditAnomaly> list(
      String tenantId, AuditAnomalyListParams params, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/audit/anomalies", tenantId)).query(params),
        AuditAnomaly.class,
        options);
  }

  /**
   * Get summary.
   *
   * @param tenantId the organisation's id
   * @return the audit anomaly summary
   */
  public AuditAnomalySummary getSummary(String tenantId) {
    return getSummary(tenantId, RequestOptions.none());
  }

  /**
   * Get summary. With per-call settings: see {@link #getSummary(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the audit anomaly summary
   */
  public AuditAnomalySummary getSummary(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/audit/anomalies/summary", tenantId)),
        options,
        AuditAnomalySummary.class);
  }

  /**
   * Resolve.
   *
   * @param tenantId the organisation's id
   * @param anomalyId the anomaly's id
   */
  public void resolve(String tenantId, String anomalyId) {
    resolve(tenantId, anomalyId, RequestOptions.none());
  }

  /**
   * Resolve. With per-call settings: see {@link #resolve(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param anomalyId the anomaly's id
   * @param options per-call settings
   */
  public void resolve(String tenantId, String anomalyId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/tenants/{tenantId}/audit/anomalies/{anomalyId}/resolve", tenantId, anomalyId)),
        options);
  }

  /**
   * Get settings.
   *
   * @param tenantId the organisation's id
   * @return the audit anomaly settings
   */
  public AuditAnomalySettings getSettings(String tenantId) {
    return getSettings(tenantId, RequestOptions.none());
  }

  /**
   * Get settings. With per-call settings: see {@link #getSettings(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the audit anomaly settings
   */
  public AuditAnomalySettings getSettings(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/audit/anomaly-settings", tenantId)),
        options,
        AuditAnomalySettings.class);
  }

  /**
   * Update settings.
   *
   * @param tenantId the organisation's id
   * @param settings the settings
   * @return the audit anomaly settings
   */
  public AuditAnomalySettings updateSettings(String tenantId, AuditAnomalySettings settings) {
    return updateSettings(tenantId, settings, RequestOptions.none());
  }

  /**
   * Update settings. With per-call settings: see {@link #updateSettings(String,
   * AuditAnomalySettings)}.
   *
   * @param tenantId the organisation's id
   * @param settings the settings
   * @param options per-call settings
   * @return the audit anomaly settings
   */
  public AuditAnomalySettings updateSettings(
      String tenantId, AuditAnomalySettings settings, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/audit/anomaly-settings", tenantId))
            .json(settings),
        options,
        AuditAnomalySettings.class);
  }
}
