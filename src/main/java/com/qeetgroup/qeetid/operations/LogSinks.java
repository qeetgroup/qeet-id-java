package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages audit-log streaming to SIEMs.
 *
 * <p>Get it from {@code qeetId.logSinks()}. Safe for concurrent use.
 */
public final class LogSinks {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.logSinks()}.
   *
   * @param http the client's transport
   */
  public LogSinks(Transport http) {
    this.http = http;
  }

  /**
   * Creates a log sink.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the log sink
   */
  public LogSink create(String organizationId, LogSinkCreateParams params) {
    return create(organizationId, params, RequestOptions.none());
  }

  /**
   * Creates a log sink. With per-call settings: see {@link #create(String, LogSinkCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the log sink
   */
  public LogSink create(String organizationId, LogSinkCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/log-sinks", organizationId))
            .json(params),
        options,
        LogSink.class);
  }

  /**
   * Lists the log sinks.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<LogSink> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the log sinks. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<LogSink> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/log-sinks", organizationId)),
        LogSink.class,
        options);
  }

  /**
   * Switches streaming to the sink on or off.
   *
   * @param organizationId the organization's id
   * @param sinkId the sink's id
   * @param enabled the enabled
   */
  public void setEnabled(String organizationId, String sinkId, boolean enabled) {
    setEnabled(organizationId, sinkId, enabled, RequestOptions.none());
  }

  /**
   * Switches streaming to the sink on or off. With per-call settings: see {@link
   * #setEnabled(String, String, boolean)}.
   *
   * @param organizationId the organization's id
   * @param sinkId the sink's id
   * @param enabled the enabled
   * @param options per-call settings
   */
  public void setEnabled(
      String organizationId, String sinkId, boolean enabled, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/organizations/{organizationId}/log-sinks/{sinkId}",
                    organizationId,
                    sinkId))
            .json(Bodies.of("enabled", enabled)),
        options);
  }

  /**
   * Deletes a log sink.
   *
   * @param organizationId the organization's id
   * @param sinkId the sink's id
   */
  public void delete(String organizationId, String sinkId) {
    delete(organizationId, sinkId, RequestOptions.none());
  }

  /**
   * Deletes a log sink. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param organizationId the organization's id
   * @param sinkId the sink's id
   * @param options per-call settings
   */
  public void delete(String organizationId, String sinkId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/log-sinks/{sinkId}", organizationId, sinkId)),
        options);
  }

  /**
   * Sends a test event to a saved sink.
   *
   * @param organizationId the organization's id
   * @param sinkId the sink's id
   * @return the log sink test result
   */
  public LogSinkTestResult test(String organizationId, String sinkId) {
    return test(organizationId, sinkId, RequestOptions.none());
  }

  /**
   * Sends a test event to a saved sink. With per-call settings: see {@link #test(String, String)}.
   *
   * @param organizationId the organization's id
   * @param sinkId the sink's id
   * @param options per-call settings
   * @return the log sink test result
   */
  public LogSinkTestResult test(String organizationId, String sinkId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of(
                "/v1/organizations/{organizationId}/log-sinks/{sinkId}/test",
                organizationId,
                sinkId)),
        options,
        LogSinkTestResult.class);
  }

  /**
   * Sends a test event to a destination before saving it.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the log sink test result
   */
  public LogSinkTestResult testDestination(String organizationId, LogSinkCreateParams params) {
    return testDestination(organizationId, params, RequestOptions.none());
  }

  /**
   * Sends a test event to a destination before saving it. With per-call settings: see {@link
   * #testDestination(String, LogSinkCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the log sink test result
   */
  public LogSinkTestResult testDestination(
      String organizationId, LogSinkCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/log-sinks/test", organizationId))
            .json(params),
        options,
        LogSinkTestResult.class);
  }
}
