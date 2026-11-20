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
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<AuditAnomaly> list(String organizationId) {
    return list(organizationId, AuditAnomalyListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists the audit anomalies.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the list
   */
  public ListResult<AuditAnomaly> list(String organizationId, AuditAnomalyListParams params) {
    return list(organizationId, params, RequestOptions.none());
  }

  /**
   * Lists the audit anomalies. With per-call settings: see {@link #list(String,
   * AuditAnomalyListParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the list
   */
  public ListResult<AuditAnomaly> list(
      String organizationId, AuditAnomalyListParams params, RequestOptions options) {
    return http.list(
        ApiRequest.get(
                Paths.of("/v1/organizations/{organizationId}/audit/anomalies", organizationId))
            .query(params),
        AuditAnomaly.class,
        options);
  }

  /**
   * Get summary.
   *
   * @param organizationId the organization's id
   * @return the audit anomaly summary
   */
  public AuditAnomalySummary getSummary(String organizationId) {
    return getSummary(organizationId, RequestOptions.none());
  }

  /**
   * Get summary. With per-call settings: see {@link #getSummary(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the audit anomaly summary
   */
  public AuditAnomalySummary getSummary(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/audit/anomalies/summary", organizationId)),
        options,
        AuditAnomalySummary.class);
  }

  /**
   * Resolve.
   *
   * @param organizationId the organization's id
   * @param anomalyId the anomaly's id
   */
  public void resolve(String organizationId, String anomalyId) {
    resolve(organizationId, anomalyId, RequestOptions.none());
  }

  /**
   * Resolve. With per-call settings: see {@link #resolve(String, String)}.
   *
   * @param organizationId the organization's id
   * @param anomalyId the anomaly's id
   * @param options per-call settings
   */
  public void resolve(String organizationId, String anomalyId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/organizations/{organizationId}/audit/anomalies/{anomalyId}/resolve",
                organizationId,
                anomalyId)),
        options);
  }

  /**
   * Get settings.
   *
   * @param organizationId the organization's id
   * @return the audit anomaly settings
   */
  public AuditAnomalySettings getSettings(String organizationId) {
    return getSettings(organizationId, RequestOptions.none());
  }

  /**
   * Get settings. With per-call settings: see {@link #getSettings(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the audit anomaly settings
   */
  public AuditAnomalySettings getSettings(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/audit/anomaly-settings", organizationId)),
        options,
        AuditAnomalySettings.class);
  }

  /**
   * Update settings.
   *
   * @param organizationId the organization's id
   * @param settings the settings
   * @return the audit anomaly settings
   */
  public AuditAnomalySettings updateSettings(String organizationId, AuditAnomalySettings settings) {
    return updateSettings(organizationId, settings, RequestOptions.none());
  }

  /**
   * Update settings. With per-call settings: see {@link #updateSettings(String,
   * AuditAnomalySettings)}.
   *
   * @param organizationId the organization's id
   * @param settings the settings
   * @param options per-call settings
   * @return the audit anomaly settings
   */
  public AuditAnomalySettings updateSettings(
      String organizationId, AuditAnomalySettings settings, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT",
                Paths.of(
                    "/v1/organizations/{organizationId}/audit/anomaly-settings", organizationId))
            .json(settings),
        options,
        AuditAnomalySettings.class);
  }
}
