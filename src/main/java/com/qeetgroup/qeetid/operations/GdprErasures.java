package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Erasure requests (the right to be forgotten).
 *
 * <p>Get it from its parent resource. Safe for concurrent use.
 */
public final class GdprErasures {
  private final Transport http;

  /**
   * Used by the client; get this from its parent resource.
   *
   * @param http the client's transport
   */
  public GdprErasures(Transport http) {
    this.http = http;
  }

  /**
   * Requests a user's erasure, after a grace period.
   *
   * @param params the parameters
   * @return the erasure request
   */
  public ErasureRequest create(ErasureCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /**
   * Requests a user's erasure, after a grace period. With per-call settings: see {@link
   * #create(ErasureCreateParams)}.
   *
   * @param params the parameters
   * @param options per-call settings
   * @return the erasure request
   */
  public ErasureRequest create(ErasureCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post("/v1/gdpr/purge").json(params), options, ErasureRequest.class);
  }

  /**
   * Lists the GDPR erasures.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<ErasureRequest> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the GDPR erasures. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<ErasureRequest> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/gdpr/purge", tenantId)),
        ErasureRequest.class,
        options);
  }

  /**
   * Cancels a request still in its grace period.
   *
   * @param requestId the request's id
   */
  public void cancel(String requestId) {
    cancel(requestId, RequestOptions.none());
  }

  /**
   * Cancels a request still in its grace period. With per-call settings: see {@link
   * #cancel(String)}.
   *
   * @param requestId the request's id
   * @param options per-call settings
   */
  public void cancel(String requestId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of("DELETE", Paths.of("/v1/gdpr/purge/{requestId}", requestId)), options);
  }
}
