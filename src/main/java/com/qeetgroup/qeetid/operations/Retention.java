package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organization's data-retention policy.
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
   * @param organizationId the organization's id
   * @return the retention policy
   */
  public RetentionPolicy get(String organizationId) {
    return get(organizationId, RequestOptions.none());
  }

  /**
   * Fetches a retention. With per-call settings: see {@link #get(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the retention policy
   */
  public RetentionPolicy get(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/retention", organizationId)),
        options,
        RetentionPolicy.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param organizationId the organization's id
   * @param policy the policy
   * @return the retention policy
   */
  public RetentionPolicy update(String organizationId, RetentionPolicyUpdateParams policy) {
    return update(organizationId, policy, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String,
   * RetentionPolicyUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param policy the policy
   * @param options per-call settings
   * @return the retention policy
   */
  public RetentionPolicy update(
      String organizationId, RetentionPolicyUpdateParams policy, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT", Paths.of("/v1/organizations/{organizationId}/retention", organizationId))
            .json(policy),
        options,
        RetentionPolicy.class);
  }

  /**
   * Counts what the policy would purge now. Deletes nothing.
   *
   * @param organizationId the organization's id
   * @return the retention preview
   */
  public RetentionPreview preview(String organizationId) {
    return preview(organizationId, RequestOptions.none());
  }

  /**
   * Counts what the policy would purge now. With per-call settings: see {@link #preview(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the retention preview
   */
  public RetentionPreview preview(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/retention/preview", organizationId))
            .retrySafe(),
        options,
        RetentionPreview.class);
  }

  /**
   * Purges what is due now, ahead of the scheduled run.
   *
   * @param organizationId the organization's id
   * @return how many users were purged
   */
  public int run(String organizationId) {
    return run(organizationId, RequestOptions.none());
  }

  /**
   * Purges what is due now, ahead of the scheduled run. With per-call settings: see {@link
   * #run(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return how many users were purged
   */
  public int run(String organizationId, RequestOptions options) {
    return http.tree(
            ApiRequest.post(
                Paths.of("/v1/organizations/{organizationId}/retention/run", organizationId)),
            options)
        .path("purged")
        .asInt();
  }
}
