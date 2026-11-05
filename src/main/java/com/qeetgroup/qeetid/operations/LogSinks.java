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
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the log sink
   */
  public LogSink create(String tenantId, LogSinkCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates a log sink. With per-call settings: see {@link #create(String, LogSinkCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the log sink
   */
  public LogSink create(String tenantId, LogSinkCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/log-sinks", tenantId)).json(params),
        options,
        LogSink.class);
  }

  /**
   * Lists the log sinks.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<LogSink> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the log sinks. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<LogSink> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/log-sinks", tenantId)),
        LogSink.class,
        options);
  }

  /**
   * Switches streaming to the sink on or off.
   *
   * @param tenantId the organisation's id
   * @param sinkId the sink's id
   * @param enabled the enabled
   */
  public void setEnabled(String tenantId, String sinkId, boolean enabled) {
    setEnabled(tenantId, sinkId, enabled, RequestOptions.none());
  }

  /**
   * Switches streaming to the sink on or off. With per-call settings: see {@link
   * #setEnabled(String, String, boolean)}.
   *
   * @param tenantId the organisation's id
   * @param sinkId the sink's id
   * @param enabled the enabled
   * @param options per-call settings
   */
  public void setEnabled(String tenantId, String sinkId, boolean enabled, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
                "PATCH", Paths.of("/v1/tenants/{tenantId}/log-sinks/{sinkId}", tenantId, sinkId))
            .json(Bodies.of("enabled", enabled)),
        options);
  }

  /**
   * Deletes a log sink.
   *
   * @param tenantId the organisation's id
   * @param sinkId the sink's id
   */
  public void delete(String tenantId, String sinkId) {
    delete(tenantId, sinkId, RequestOptions.none());
  }

  /**
   * Deletes a log sink. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param sinkId the sink's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String sinkId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/log-sinks/{sinkId}", tenantId, sinkId)),
        options);
  }

  /**
   * Sends a test event to a saved sink.
   *
   * @param tenantId the organisation's id
   * @param sinkId the sink's id
   * @return the log sink test result
   */
  public LogSinkTestResult test(String tenantId, String sinkId) {
    return test(tenantId, sinkId, RequestOptions.none());
  }

  /**
   * Sends a test event to a saved sink. With per-call settings: see {@link #test(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param sinkId the sink's id
   * @param options per-call settings
   * @return the log sink test result
   */
  public LogSinkTestResult test(String tenantId, String sinkId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of("/v1/tenants/{tenantId}/log-sinks/{sinkId}/test", tenantId, sinkId)),
        options,
        LogSinkTestResult.class);
  }

  /**
   * Sends a test event to a destination before saving it.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the log sink test result
   */
  public LogSinkTestResult testDestination(String tenantId, LogSinkCreateParams params) {
    return testDestination(tenantId, params, RequestOptions.none());
  }

  /**
   * Sends a test event to a destination before saving it. With per-call settings: see {@link
   * #testDestination(String, LogSinkCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the log sink test result
   */
  public LogSinkTestResult testDestination(
      String tenantId, LogSinkCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/log-sinks/test", tenantId)).json(params),
        options,
        LogSinkTestResult.class);
  }
}
