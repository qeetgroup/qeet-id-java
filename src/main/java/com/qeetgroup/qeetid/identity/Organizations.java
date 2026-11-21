package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Reads and manages the API key's organization. Listing and creating organizations are a signed-in
 * person's actions and aren't open to API keys.
 *
 * <p>Get it from {@code qeetId.organizations()}. Safe for concurrent use.
 */
public final class Organizations {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.organizations()}.
   *
   * @param http the client's transport
   */
  public Organizations(Transport http) {
    this.http = http;
  }

  /**
   * Fetches an organization.
   *
   * @param organizationId the organization's id
   * @return the organization
   */
  public Organization get(String organizationId) {
    return get(organizationId, RequestOptions.none());
  }

  /**
   * Fetches an organization. With per-call settings: see {@link #get(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the organization
   */
  public Organization get(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}", organizationId)),
        options,
        Organization.class);
  }

  /**
   * Changes only the fields given.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the organization
   */
  public Organization update(String organizationId, OrganizationUpdateParams params) {
    return update(organizationId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String,
   * OrganizationUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the organization
   */
  public Organization update(
      String organizationId, OrganizationUpdateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.of("PATCH", Paths.of("/v1/organizations/{organizationId}", organizationId))
            .json(params),
        options,
        Organization.class);
  }

  /**
   * Schedules the organization's deletion seven days from now. An owner may call it off with {@code
   * cancelDeletion}.
   *
   * @param organizationId the organization's id
   * @return the organization deletion
   */
  public OrganizationDeletion delete(String organizationId) {
    return delete(organizationId, RequestOptions.none());
  }

  /**
   * Schedules the organization's deletion seven days from now. With per-call settings: see {@link
   * #delete(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the organization deletion
   */
  public OrganizationDeletion delete(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.of("DELETE", Paths.of("/v1/organizations/{organizationId}", organizationId)),
        options,
        OrganizationDeletion.class);
  }

  /**
   * Calls off a scheduled deletion.
   *
   * @param organizationId the organization's id
   * @return the organization
   */
  public Organization cancelDeletion(String organizationId) {
    return cancelDeletion(organizationId, RequestOptions.none());
  }

  /**
   * Calls off a scheduled deletion. With per-call settings: see {@link #cancelDeletion(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the organization
   */
  public Organization cancelDeletion(String organizationId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
            Paths.of("/v1/organizations/{organizationId}/deletion/cancel", organizationId)),
        options,
        Organization.class);
  }
}
