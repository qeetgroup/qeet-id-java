package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Data export requests (the right of access).
 *
 * <p>Get it from its parent resource. Safe for concurrent use.
 */
public final class GdprExports {
  private final Transport http;

  /**
   * Used by the client; get this from its parent resource.
   *
   * @param http the client's transport
   */
  public GdprExports(Transport http) {
    this.http = http;
  }

  /**
   * Starts an export. It runs in the background: poll {@code get} until {@code status} is {@code
   * ready} or {@code failed}.
   *
   * @param params the parameters
   * @return the export request
   */
  public ExportRequest create(ExportCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /**
   * Starts an export. With per-call settings: see {@link #create(ExportCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the export request
   */
  public ExportRequest create(ExportCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/gdpr/export").json(params), options, ExportRequest.class);
  }

  /**
   * Fetches a GDPR export.
   *
   * @param tenantId the organisation's id
   * @param exportId the export's id
   * @return the export request
   */
  public ExportRequest get(String tenantId, String exportId) {
    return get(tenantId, exportId, RequestOptions.none());
  }

  /**
   * Fetches a GDPR export. With per-call settings: see {@link #get(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param exportId the export's id
   * @param options per-call settings
   * @return the export request
   */
  public ExportRequest get(String tenantId, String exportId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/tenants/{tenantId}/gdpr/export/{exportId}", tenantId, exportId)),
        options,
        ExportRequest.class);
  }

  /**
   * Lists the GDPR exports.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<ExportRequest> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the GDPR exports. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<ExportRequest> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/gdpr/export", tenantId)),
        ExportRequest.class,
        options);
  }
}
