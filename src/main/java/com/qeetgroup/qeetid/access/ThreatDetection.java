package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Bot detection, adaptive-risk settings and sign-in anomalies.
 *
 * <p>Get it from {@code qeetId.threatDetection()}. Safe for concurrent use.
 */
public final class ThreatDetection {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.threatDetection()}.
   *
   * @param http the client's transport
   */
  public ThreatDetection(Transport http) {
    this.http = http;
  }

  /**
   * Recent bot-detection verdicts and statistics.
   *
   * @param tenantId the organisation's id
   * @return the bot activity
   */
  public BotActivity getBotActivity(String tenantId) {
    return getBotActivity(tenantId, BotActivityParams.builder().build(), RequestOptions.none());
  }

  /**
   * Recent bot-detection verdicts and statistics.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the bot activity
   */
  public BotActivity getBotActivity(String tenantId, BotActivityParams params) {
    return getBotActivity(tenantId, params, RequestOptions.none());
  }

  /**
   * Recent bot-detection verdicts and statistics. With per-call settings: see {@link
   * #getBotActivity(String, BotActivityParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the bot activity
   */
  public BotActivity getBotActivity(
      String tenantId, BotActivityParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/security/bots", tenantId)).query(params),
        options,
        BotActivity.class);
  }

  /**
   * Get bot settings.
   *
   * @param tenantId the organisation's id
   * @return the bot settings
   */
  public BotSettings getBotSettings(String tenantId) {
    return getBotSettings(tenantId, RequestOptions.none());
  }

  /**
   * Get bot settings. With per-call settings: see {@link #getBotSettings(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the bot settings
   */
  public BotSettings getBotSettings(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/security/bots/settings", tenantId)),
        options,
        BotSettings.class);
  }

  /**
   * Replaces the bot-detection settings.
   *
   * @param tenantId the organisation's id
   * @param settings the settings
   * @return the bot settings
   */
  public BotSettings updateBotSettings(String tenantId, BotSettings settings) {
    return updateBotSettings(tenantId, settings, RequestOptions.none());
  }

  /**
   * Replaces the bot-detection settings. With per-call settings: see {@link
   * #updateBotSettings(String, BotSettings)}.
   *
   * @param tenantId the organisation's id
   * @param settings the settings
   * @param options per-call settings
   * @return the bot settings
   */
  public BotSettings updateBotSettings(
      String tenantId, BotSettings settings, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/security/bots/settings", tenantId))
            .json(settings),
        options,
        BotSettings.class);
  }

  /**
   * Get risk settings.
   *
   * @param tenantId the organisation's id
   * @return the risk settings
   */
  public RiskSettings getRiskSettings(String tenantId) {
    return getRiskSettings(tenantId, RequestOptions.none());
  }

  /**
   * Get risk settings. With per-call settings: see {@link #getRiskSettings(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the risk settings
   */
  public RiskSettings getRiskSettings(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/security/risk-settings", tenantId)),
        options,
        RiskSettings.class);
  }

  /**
   * Replaces the risk settings.
   *
   * @param tenantId the organisation's id
   * @param settings the settings
   * @return the risk settings
   */
  public RiskSettings updateRiskSettings(String tenantId, RiskSettingsUpdateParams settings) {
    return updateRiskSettings(tenantId, settings, RequestOptions.none());
  }

  /**
   * Replaces the risk settings. With per-call settings: see {@link #updateRiskSettings(String,
   * RiskSettingsUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param settings the settings
   * @param options per-call settings
   * @return the risk settings
   */
  public RiskSettings updateRiskSettings(
      String tenantId, RiskSettingsUpdateParams settings, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/security/risk-settings", tenantId))
            .json(settings),
        options,
        RiskSettings.class);
  }

  /**
   * Lists sign-in anomalies, newest first.
   *
   * @param tenantId the organisation's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<Anomaly> listAnomalies(String tenantId) {
    return listAnomalies(tenantId, AnomalyListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists sign-in anomalies, newest first.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<Anomaly> listAnomalies(String tenantId, AnomalyListParams params) {
    return listAnomalies(tenantId, params, RequestOptions.none());
  }

  /**
   * Lists sign-in anomalies, newest first. With per-call settings: see {@link
   * #listAnomalies(String, AnomalyListParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<Anomaly> listAnomalies(
      String tenantId, AnomalyListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/tenants/{tenantId}/security/anomalies", tenantId),
        params,
        Anomaly.class,
        options);
  }

  /**
   * Get anomaly summary.
   *
   * @param tenantId the organisation's id
   * @return the anomaly summary
   */
  public AnomalySummary getAnomalySummary(String tenantId) {
    return getAnomalySummary(tenantId, RequestOptions.none());
  }

  /**
   * Get anomaly summary. With per-call settings: see {@link #getAnomalySummary(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the anomaly summary
   */
  public AnomalySummary getAnomalySummary(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/security/anomalies/summary", tenantId)),
        options,
        AnomalySummary.class);
  }

  /**
   * Marks an anomaly reviewed. It blocks or revokes nothing by itself.
   *
   * @param tenantId the organisation's id
   * @param anomalyId the anomaly's id
   */
  public void resolveAnomaly(String tenantId, String anomalyId) {
    resolveAnomaly(tenantId, anomalyId, RequestOptions.none());
  }

  /**
   * Marks an anomaly reviewed. With per-call settings: see {@link #resolveAnomaly(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param anomalyId the anomaly's id
   * @param options per-call settings
   */
  public void resolveAnomaly(String tenantId, String anomalyId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/tenants/{tenantId}/security/anomalies/{anomalyId}/resolve",
                tenantId,
                anomalyId)),
        options);
  }
}
