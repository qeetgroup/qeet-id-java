package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the service providers that use Qeet ID as their SAML identity provider.
 *
 * <p>Get it from {@code qeetId.samlServiceProviders()}. Safe for concurrent use.
 */
public final class SamlServiceProviders {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.samlServiceProviders()}.
   *
   * @param http the client's transport
   */
  public SamlServiceProviders(Transport http) {
    this.http = http;
  }

  /**
   * Creates a SAML service provider.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the SAML service provider
   */
  public SamlServiceProvider create(String tenantId, SamlServiceProviderCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates a SAML service provider. With per-call settings: see {@link #create(String,
   * SamlServiceProviderCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML service provider
   */
  public SamlServiceProvider create(
      String tenantId, SamlServiceProviderCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/saml-providers", tenantId)).json(params),
        options,
        SamlServiceProvider.class);
  }

  /**
   * Fetches a SAML service provider.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @return the SAML service provider
   */
  public SamlServiceProvider get(String tenantId, String serviceProviderId) {
    return get(tenantId, serviceProviderId, RequestOptions.none());
  }

  /**
   * Fetches a SAML service provider. With per-call settings: see {@link #get(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param options per-call settings
   * @return the SAML service provider
   */
  public SamlServiceProvider get(
      String tenantId, String serviceProviderId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/tenants/{tenantId}/saml-providers/{serviceProviderId}",
                tenantId,
                serviceProviderId)),
        options,
        SamlServiceProvider.class);
  }

  /**
   * Lists the SAML service providers.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<SamlServiceProvider> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the SAML service providers. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<SamlServiceProvider> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/saml-providers", tenantId)),
        SamlServiceProvider.class,
        options);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param params the parameters
   * @return the SAML service provider
   */
  public SamlServiceProvider update(
      String tenantId, String serviceProviderId, SamlServiceProviderUpdateParams params) {
    return update(tenantId, serviceProviderId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * SamlServiceProviderUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML service provider
   */
  public SamlServiceProvider update(
      String tenantId,
      String serviceProviderId,
      SamlServiceProviderUpdateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/tenants/{tenantId}/saml-providers/{serviceProviderId}",
                    tenantId,
                    serviceProviderId))
            .json(params),
        options,
        SamlServiceProvider.class);
  }

  /**
   * Deletes a SAML service provider.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   */
  public void delete(String tenantId, String serviceProviderId) {
    delete(tenantId, serviceProviderId, RequestOptions.none());
  }

  /**
   * Deletes a SAML service provider. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String serviceProviderId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/saml-providers/{serviceProviderId}",
                tenantId,
                serviceProviderId)),
        options);
  }

  /**
   * List assignments.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @return the SAML service provider assignments
   */
  public SamlServiceProviderAssignments listAssignments(String tenantId, String serviceProviderId) {
    return listAssignments(tenantId, serviceProviderId, RequestOptions.none());
  }

  /**
   * List assignments. With per-call settings: see {@link #listAssignments(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param options per-call settings
   * @return the SAML service provider assignments
   */
  public SamlServiceProviderAssignments listAssignments(
      String tenantId, String serviceProviderId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/tenants/{tenantId}/saml-providers/{serviceProviderId}/assignments",
                tenantId,
                serviceProviderId)),
        options,
        SamlServiceProviderAssignments.class);
  }

  /**
   * Assign user.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param userId the user's id
   */
  public void assignUser(String tenantId, String serviceProviderId, String userId) {
    assignUser(tenantId, serviceProviderId, userId, RequestOptions.none());
  }

  /**
   * Assign user. With per-call settings: see {@link #assignUser(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param userId the user's id
   * @param options per-call settings
   */
  public void assignUser(
      String tenantId, String serviceProviderId, String userId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/tenants/{tenantId}/saml-providers/{serviceProviderId}/assignments/users/{userId}",
                tenantId,
                serviceProviderId,
                userId)),
        options);
  }

  /**
   * Unassign user.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param userId the user's id
   */
  public void unassignUser(String tenantId, String serviceProviderId, String userId) {
    unassignUser(tenantId, serviceProviderId, userId, RequestOptions.none());
  }

  /**
   * Unassign user. With per-call settings: see {@link #unassignUser(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param userId the user's id
   * @param options per-call settings
   */
  public void unassignUser(
      String tenantId, String serviceProviderId, String userId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/saml-providers/{serviceProviderId}/assignments/users/{userId}",
                tenantId,
                serviceProviderId,
                userId)),
        options);
  }

  /**
   * Assign group.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param groupId the group's id
   */
  public void assignGroup(String tenantId, String serviceProviderId, String groupId) {
    assignGroup(tenantId, serviceProviderId, groupId, RequestOptions.none());
  }

  /**
   * Assign group. With per-call settings: see {@link #assignGroup(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param groupId the group's id
   * @param options per-call settings
   */
  public void assignGroup(
      String tenantId, String serviceProviderId, String groupId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
            Paths.of(
                "/v1/tenants/{tenantId}/saml-providers/{serviceProviderId}/assignments/groups/{groupId}",
                tenantId,
                serviceProviderId,
                groupId)),
        options);
  }

  /**
   * Unassign group.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param groupId the group's id
   */
  public void unassignGroup(String tenantId, String serviceProviderId, String groupId) {
    unassignGroup(tenantId, serviceProviderId, groupId, RequestOptions.none());
  }

  /**
   * Unassign group. With per-call settings: see {@link #unassignGroup(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param serviceProviderId the service provider's id
   * @param groupId the group's id
   * @param options per-call settings
   */
  public void unassignGroup(
      String tenantId, String serviceProviderId, String groupId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/saml-providers/{serviceProviderId}/assignments/groups/{groupId}",
                tenantId,
                serviceProviderId,
                groupId)),
        options);
  }
}
