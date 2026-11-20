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
   * @param organizationId the organization's id
   * @return the first page; iterate it for every item, page by page
   */
  public Page<AuditLogEntry> list(String organizationId) {
    return list(organizationId, AuditLogListParams.builder().build(), RequestOptions.none());
  }

  /**
   * Lists entries, newest first.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<AuditLogEntry> list(String organizationId, AuditLogListParams params) {
    return list(organizationId, params, RequestOptions.none());
  }

  /**
   * Lists entries, newest first. With per-call settings: see {@link #list(String,
   * AuditLogListParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<AuditLogEntry> list(
      String organizationId, AuditLogListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/organizations/{organizationId}/audit", organizationId),
        params,
        AuditLogEntry.class,
        options);
  }

  /**
   * Walks the whole chain now and recomputes every row's hash. Changes nothing.
   *
   * @param organizationId the organization's id
   * @return the audit chain verification
   */
  public AuditChainVerification verify(String organizationId) {
    return verify(organizationId, RequestOptions.none());
  }

  /**
   * Walks the whole chain now and recomputes every row's hash. With per-call settings: see {@link
   * #verify(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the audit chain verification
   */
  public AuditChainVerification verify(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/audit/verify", organizationId)),
        options,
        AuditChainVerification.class);
  }

  /**
   * The latest scheduled verification's outcome.
   *
   * @param organizationId the organization's id
   * @return the audit verification status
   */
  public AuditVerificationStatus getVerificationStatus(String organizationId) {
    return getVerificationStatus(organizationId, RequestOptions.none());
  }

  /**
   * The latest scheduled verification's outcome. With per-call settings: see {@link
   * #getVerificationStatus(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the audit verification status
   */
  public AuditVerificationStatus getVerificationStatus(
      String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/audit/verification", organizationId)),
        options,
        AuditVerificationStatus.class);
  }
}
