package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Reads the hash-chained audit log and verifies its integrity.
 *
 * <p>Get it from {@code qeetId.auditLogs()}. Safe for concurrent use.
 */
public final class AuditLogs {
  private final Transport http;
  private final AuditAnomalies anomalies;

  /**
   * Used by the client; get this from {@code qeetId.auditLogs()}.
   *
   * @param http the client's transport
   */
  public AuditLogs(Transport http) {
    this.http = http;
    this.anomalies = new AuditAnomalies(http);
  }

  /**
   * Behavioural anomalies found in the audit log.
   *
   * @return the anomalies
   */
  public AuditAnomalies anomalies() {
    return anomalies;
  }

  /**
   * Lists entries, newest first.
   *
   * @param tenantId the organisation's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<AuditLogEntry> list(String tenantId) {
    return list(tenantId, AuditLogListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists entries, newest first.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<AuditLogEntry> list(String tenantId, AuditLogListParams params) {
    return list(tenantId, params, RequestOptions.none());
  }

  /**
   * Lists entries, newest first. With per-call settings: see {@link #list(String,
   * AuditLogListParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<AuditLogEntry> list(
      String tenantId, AuditLogListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/tenants/{tenantId}/audit", tenantId), params, AuditLogEntry.class, options);
  }

  /**
   * Walks the whole chain now and recomputes every row's hash. Changes nothing.
   *
   * @param tenantId the organisation's id
   * @return the audit chain verification
   */
  public AuditChainVerification verify(String tenantId) {
    return verify(tenantId, RequestOptions.none());
  }

  /**
   * Walks the whole chain now and recomputes every row's hash. With per-call settings: see {@link
   * #verify(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the audit chain verification
   */
  public AuditChainVerification verify(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/audit/verify", tenantId)),
        options,
        AuditChainVerification.class);
  }

  /**
   * The latest scheduled verification's outcome.
   *
   * @param tenantId the organisation's id
   * @return the audit verification status
   */
  public AuditVerificationStatus getVerificationStatus(String tenantId) {
    return getVerificationStatus(tenantId, RequestOptions.none());
  }

  /**
   * The latest scheduled verification's outcome. With per-call settings: see {@link
   * #getVerificationStatus(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the audit verification status
   */
  public AuditVerificationStatus getVerificationStatus(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/audit/verification", tenantId)),
        options,
        AuditVerificationStatus.class);
  }
}
