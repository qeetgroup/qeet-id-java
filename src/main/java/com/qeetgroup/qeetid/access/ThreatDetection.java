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
   * @param organizationId the organization's id
   * @return the bot activity
   */
  public BotActivity getBotActivity(String organizationId) {
    return getBotActivity(
        organizationId, BotActivityParams.builder().build(), RequestOptions.none());
  }

  /**
   * Recent bot-detection verdicts and statistics.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the bot activity
   */
  public BotActivity getBotActivity(String organizationId, BotActivityParams params) {
    return getBotActivity(organizationId, params, RequestOptions.none());
  }

  /**
   * Recent bot-detection verdicts and statistics. With per-call settings: see {@link
   * #getBotActivity(String, BotActivityParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the bot activity
   */
  public BotActivity getBotActivity(
      String organizationId, BotActivityParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/security/bots", organizationId))
            .query(params),
        options,
        BotActivity.class);
  }

  /**
   * Get bot settings.
   *
   * @param organizationId the organization's id
   * @return the bot settings
   */
  public BotSettings getBotSettings(String organizationId) {
    return getBotSettings(organizationId, RequestOptions.none());
  }

  /**
   * Get bot settings. With per-call settings: see {@link #getBotSettings(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the bot settings
   */
  public BotSettings getBotSettings(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/security/bots/settings", organizationId)),
        options,
        BotSettings.class);
  }

  /**
   * Replaces the bot-detection settings.
   *
   * @param organizationId the organization's id
   * @param settings the settings
   * @return the bot settings
   */
  public BotSettings updateBotSettings(String organizationId, BotSettings settings) {
    return updateBotSettings(organizationId, settings, RequestOptions.none());
  }

  /**
   * Replaces the bot-detection settings. With per-call settings: see {@link
   * #updateBotSettings(String, BotSettings)}.
   *
   * @param organizationId the organization's id
   * @param settings the settings
   * @param options per-call settings
   * @return the bot settings
   */
  public BotSettings updateBotSettings(
      String organizationId, BotSettings settings, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT",
                Paths.of(
                    "/v1/organizations/{organizationId}/security/bots/settings", organizationId))
            .json(settings),
        options,
        BotSettings.class);
  }

  /**
   * Get risk settings.
   *
   * @param organizationId the organization's id
   * @return the risk settings
   */
  public RiskSettings getRiskSettings(String organizationId) {
    return getRiskSettings(organizationId, RequestOptions.none());
  }

  /**
   * Get risk settings. With per-call settings: see {@link #getRiskSettings(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the risk settings
   */
  public RiskSettings getRiskSettings(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/security/risk-settings", organizationId)),
        options,
        RiskSettings.class);
  }

  /**
   * Replaces the risk settings.
   *
   * @param organizationId the organization's id
   * @param settings the settings
   * @return the risk settings
   */
  public RiskSettings updateRiskSettings(String organizationId, RiskSettingsUpdateParams settings) {
    return updateRiskSettings(organizationId, settings, RequestOptions.none());
  }

  /**
   * Replaces the risk settings. With per-call settings: see {@link #updateRiskSettings(String,
   * RiskSettingsUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param settings the settings
   * @param options per-call settings
   * @return the risk settings
   */
  public RiskSettings updateRiskSettings(
      String organizationId, RiskSettingsUpdateParams settings, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT",
                Paths.of(
                    "/v1/organizations/{organizationId}/security/risk-settings", organizationId))
            .json(settings),
        options,
        RiskSettings.class);
  }

  /**
   * Lists sign-in anomalies, newest first.
   *
   * @param organizationId the organization's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<Anomaly> listAnomalies(String organizationId) {
    return listAnomalies(
        organizationId, AnomalyListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists sign-in anomalies, newest first.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<Anomaly> listAnomalies(String organizationId, AnomalyListParams params) {
    return listAnomalies(organizationId, params, RequestOptions.none());
  }

  /**
   * Lists sign-in anomalies, newest first. With per-call settings: see {@link
   * #listAnomalies(String, AnomalyListParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<Anomaly> listAnomalies(
      String organizationId, AnomalyListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/organizations/{organizationId}/security/anomalies", organizationId),
        params,
        Anomaly.class,
        options);
  }

  /**
   * Get anomaly summary.
   *
   * @param organizationId the organization's id
   * @return the anomaly summary
   */
  public AnomalySummary getAnomalySummary(String organizationId) {
    return getAnomalySummary(organizationId, RequestOptions.none());
  }

  /**
   * Get anomaly summary. With per-call settings: see {@link #getAnomalySummary(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the anomaly summary
   */
  public AnomalySummary getAnomalySummary(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/organizations/{organizationId}/security/anomalies/summary", organizationId)),
        options,
        AnomalySummary.class);
  }

  /**
   * Marks an anomaly reviewed. It blocks or revokes nothing by itself.
   *
   * @param organizationId the organization's id
   * @param anomalyId the anomaly's id
   */
  public void resolveAnomaly(String organizationId, String anomalyId) {
    resolveAnomaly(organizationId, anomalyId, RequestOptions.none());
  }

  /**
   * Marks an anomaly reviewed. With per-call settings: see {@link #resolveAnomaly(String, String)}.
   *
   * @param organizationId the organization's id
   * @param anomalyId the anomaly's id
   * @param options per-call settings
   */
  public void resolveAnomaly(String organizationId, String anomalyId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/organizations/{organizationId}/security/anomalies/{anomalyId}/resolve",
                organizationId,
                anomalyId)),
        options);
  }
}
