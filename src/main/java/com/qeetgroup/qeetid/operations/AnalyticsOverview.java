package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * The sign-in dashboard: key figures and trends.
 *
 * @param generatedAt the generated time
 * @param kpis the kpis
 * @param weeklyActivity8w the weekly activity 8w
 * @param userTrend14d the user trend 14d
 * @param loginTrend14d the login trend 14d
 * @param mfaTrend14d whether MFA trend 14d
 * @param failedTrend14d the failed trend 14d
 * @param loginActivity14d the login activity 14d
 * @param loginMethodsMix the login methods mix
 * @param mfaMethodsAdoption whether MFA methods adoption
 * @param failedLoginsHourly24h the failed logins hourly 24h
 * @param range Present when a range was asked for.
 */
public record AnalyticsOverview(
    @JsonProperty("generated_at") Instant generatedAt,
    Kpis kpis,
    @JsonProperty("weekly_activity_8w") List<WeeklyActivity8w> weeklyActivity8w,
    @JsonProperty("user_trend_14d") List<AnalyticsTrendPoint> userTrend14d,
    @JsonProperty("login_trend_14d") List<AnalyticsTrendPoint> loginTrend14d,
    @JsonProperty("mfa_trend_14d") List<AnalyticsTrendPoint> mfaTrend14d,
    @JsonProperty("failed_trend_14d") List<AnalyticsTrendPoint> failedTrend14d,
    @JsonProperty("login_activity_14d") List<AnalyticsActivityPoint> loginActivity14d,
    @JsonProperty("login_methods_mix") List<LoginMethodsMix> loginMethodsMix,
    @JsonProperty("mfa_methods_adoption") List<MfaMethodsAdoption> mfaMethodsAdoption,
    @JsonProperty("failed_logins_hourly_24h") List<FailedLoginsHourly24h> failedLoginsHourly24h,
    @Nullable Range range) {

  /**
   * Kpis.
   *
   * @param mau the mau
   * @param dau the dau
   * @param loginsToday the logins today
   * @param failedLogins24h the failed logins 24h
   * @param mfaAdoptionPct whether MFA adoption pct
   * @param totalUsers the total users
   * @param avgSessionsPerUser the avg sessions per user
   * @param stickinessPct the stickiness pct
   */
  public record Kpis(
      AnalyticsMetric mau,
      AnalyticsMetric dau,
      @JsonProperty("logins_today") AnalyticsMetric loginsToday,
      @JsonProperty("failed_logins_24h") AnalyticsMetric failedLogins24h,
      @JsonProperty("mfa_adoption_pct") AnalyticsMetric mfaAdoptionPct,
      @JsonProperty("total_users") AnalyticsMetric totalUsers,
      @JsonProperty("avg_sessions_per_user") AnalyticsMetric avgSessionsPerUser,
      @JsonProperty("stickiness_pct") AnalyticsMetric stickinessPct) {}

  /**
   * Weekly activity8w.
   *
   * @param week the week
   * @param wau the wau
   * @param dau the dau
   */
  public record WeeklyActivity8w(String week, int wau, int dau) {}

  /**
   * Login methods mix.
   *
   * @param method the method
   * @param value the value
   */
  public record LoginMethodsMix(String method, double value) {}

  /**
   * MFA methods adoption.
   *
   * @param method the method
   * @param users the users
   */
  public record MfaMethodsAdoption(String method, int users) {}

  /**
   * Failed logins hourly24h.
   *
   * @param hour the hour
   * @param attempts the attempts
   */
  public record FailedLoginsHourly24h(String hour, int attempts) {}

  /**
   * Present when a range was asked for.
   *
   * @param from the from
   * @param to the to
   * @param method the method
   * @param days the days
   */
  public record Range(
      @Nullable String from, @Nullable String to, @Nullable String method, List<Day> days) {

    /**
     * Day.
     *
     * @param date the date
     * @param signIns the sign ins
     * @param people the people
     * @param failed the failed
     */
    public record Day(
        String date, @JsonProperty("sign_ins") int signIns, long people, long failed) {}
  }
}
