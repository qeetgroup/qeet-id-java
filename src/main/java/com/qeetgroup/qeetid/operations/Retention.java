package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's data-retention policy.
 *
 * <p>Get it from {@code qeetId.retention()}. Safe for concurrent use.
 */
public final class Retention {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.retention()}.
   *
   * @param http the client's transport
   */
  public Retention(Transport http) {
    this.http = http;
  }

  /**
   * Fetches a retention.
   *
   * @param tenantId the organisation's id
   * @return the retention policy
   */
  public RetentionPolicy get(String tenantId) {
    return get(tenantId, RequestOptions.none());
  }

  /**
   * Fetches a retention. With per-call settings: see {@link #get(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the retention policy
   */
  public RetentionPolicy get(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/retention", tenantId)),
        options,
        RetentionPolicy.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param policy the policy
   * @return the retention policy
   */
  public RetentionPolicy update(String tenantId, RetentionPolicyUpdateParams policy) {
    return update(tenantId, policy, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String,
   * RetentionPolicyUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param policy the policy
   * @param options per-call settings
   * @return the retention policy
   */
  public RetentionPolicy update(
      String tenantId, RetentionPolicyUpdateParams policy, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PUT", Paths.of("/v1/tenants/{tenantId}/retention", tenantId)).json(policy),
        options,
        RetentionPolicy.class);
  }

  /**
   * Counts what the policy would purge now. Deletes nothing.
   *
   * @param tenantId the organisation's id
   * @return the retention preview
   */
  public RetentionPreview preview(String tenantId) {
    return preview(tenantId, RequestOptions.none());
  }

  /**
   * Counts what the policy would purge now. With per-call settings: see {@link #preview(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the retention preview
   */
  public RetentionPreview preview(String tenantId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/retention/preview", tenantId)).retrySafe(),
        options,
        RetentionPreview.class);
  }

  /**
   * Purges what is due now, ahead of the scheduled run.
   *
   * @param tenantId the organisation's id
   * @return how many users were purged
   */
  public int run(String tenantId) {
    return run(tenantId, RequestOptions.none());
  }

  /**
   * Purges what is due now, ahead of the scheduled run. With per-call settings: see {@link
   * #run(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return how many users were purged
   */
  public int run(String tenantId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/retention/run", tenantId)), options)
        .path("purged")
        .asInt();
  }
}
