package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Bodies;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages inbound SAML connections (Qeet ID as the service provider).
 *
 * <p>Get it from {@code qeetId.samlConnections()}. Safe for concurrent use.
 */
public final class SamlConnections {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.samlConnections()}.
   *
   * @param http the client's transport
   */
  public SamlConnections(Transport http) {
    this.http = http;
  }

  /**
   * Creates a SAML connection.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the SAML connection
   */
  public SamlConnection create(String organizationId, SamlConnectionCreateParams params) {
    return create(organizationId, params, RequestOptions.none());
  }

  /**
   * Creates a SAML connection. With per-call settings: see {@link #create(String,
   * SamlConnectionCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection create(
      String organizationId, SamlConnectionCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/saml", organizationId))
            .json(params),
        options,
        SamlConnection.class);
  }

  /**
   * Creates a connection from the identity provider's metadata.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @return the SAML connection
   */
  public SamlConnection createFromMetadata(
      String organizationId, SamlConnectionImportParams params) {
    return createFromMetadata(organizationId, params, RequestOptions.none());
  }

  /**
   * Creates a connection from the identity provider's metadata. With per-call settings: see {@link
   * #createFromMetadata(String, SamlConnectionImportParams)}.
   *
   * @param organizationId the organization's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection createFromMetadata(
      String organizationId, SamlConnectionImportParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/organizations/{organizationId}/saml/import", organizationId))
            .json(params),
        options,
        SamlConnection.class);
  }

  /**
   * Fetches a SAML connection.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @return the SAML connection
   */
  public SamlConnection get(String organizationId, String connectionId) {
    return get(organizationId, connectionId, RequestOptions.none());
  }

  /**
   * Fetches a SAML connection. With per-call settings: see {@link #get(String, String)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection get(String organizationId, String connectionId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/organizations/{organizationId}/saml/{connectionId}",
                organizationId,
                connectionId)),
        options,
        SamlConnection.class);
  }

  /**
   * Lists the SAML connections.
   *
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<SamlConnection> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the SAML connections. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<SamlConnection> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/organizations/{organizationId}/saml", organizationId)),
        SamlConnection.class,
        options);
  }

  /**
   * Changes only the fields given.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @return the SAML connection
   */
  public SamlConnection update(
      String organizationId, String connectionId, SamlConnectionUpdateParams params) {
    return update(organizationId, connectionId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * SamlConnectionUpdateParams)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection update(
      String organizationId,
      String connectionId,
      SamlConnectionUpdateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of(
                    "/v1/organizations/{organizationId}/saml/{connectionId}",
                    organizationId,
                    connectionId))
            .json(params),
        options,
        SamlConnection.class);
  }

  /**
   * Deletes a SAML connection.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   */
  public void delete(String organizationId, String connectionId) {
    delete(organizationId, connectionId, RequestOptions.none());
  }

  /**
   * Deletes a SAML connection. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param options per-call settings
   */
  public void delete(String organizationId, String connectionId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/saml/{connectionId}",
                organizationId,
                connectionId)),
        options);
  }

  /**
   * Updates the connection from the identity provider's metadata.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @return the SAML connection
   */
  public SamlConnection updateMetadata(
      String organizationId, String connectionId, SamlMetadataParams params) {
    return updateMetadata(organizationId, connectionId, params, RequestOptions.none());
  }

  /**
   * Updates the connection from the identity provider's metadata. With per-call settings: see
   * {@link #updateMetadata(String, String, SamlMetadataParams)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection updateMetadata(
      String organizationId,
      String connectionId,
      SamlMetadataParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/saml/{connectionId}/metadata",
                    organizationId,
                    connectionId))
            .json(params),
        options,
        SamlConnection.class);
  }

  /**
   * Adds a signing certificate, for a rotation without downtime.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param certificate the certificate
   * @return the SAML certificate
   */
  public SamlCertificate addCertificate(
      String organizationId, String connectionId, String certificate) {
    return addCertificate(organizationId, connectionId, certificate, RequestOptions.none());
  }

  /**
   * Adds a signing certificate, for a rotation without downtime. With per-call settings: see {@link
   * #addCertificate(String, String, String)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param certificate the certificate
   * @param options per-call settings
   * @return the SAML certificate
   */
  public SamlCertificate addCertificate(
      String organizationId, String connectionId, String certificate, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/saml/{connectionId}/certificates",
                    organizationId,
                    connectionId))
            .json(Bodies.of("certificate", certificate)),
        options,
        SamlCertificate.class);
  }

  /**
   * Remove certificate.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param certificateId the certificate's id
   */
  public void removeCertificate(String organizationId, String connectionId, String certificateId) {
    removeCertificate(organizationId, connectionId, certificateId, RequestOptions.none());
  }

  /**
   * Remove certificate. With per-call settings: see {@link #removeCertificate(String, String,
   * String)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param certificateId the certificate's id
   * @param options per-call settings
   */
  public void removeCertificate(
      String organizationId, String connectionId, String certificateId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/saml/{connectionId}/certificates/{certificateId}",
                organizationId,
                connectionId,
                certificateId)),
        options);
  }

  /**
   * Checks the configuration.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @return the connection test result
   */
  public ConnectionTestResult test(String organizationId, String connectionId) {
    return test(organizationId, connectionId, RequestOptions.none());
  }

  /**
   * Checks the configuration. With per-call settings: see {@link #test(String, String)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return the connection test result
   */
  public ConnectionTestResult test(
      String organizationId, String connectionId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/saml/{connectionId}/test",
                    organizationId,
                    connectionId))
            .retrySafe(),
        options,
        ConnectionTestResult.class);
  }

  /**
   * List group mappings.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @return the list
   */
  public ListResult<SsoGroupMapping> listGroupMappings(String organizationId, String connectionId) {
    return listGroupMappings(organizationId, connectionId, RequestOptions.none());
  }

  /**
   * List group mappings. With per-call settings: see {@link #listGroupMappings(String, String)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<SsoGroupMapping> listGroupMappings(
      String organizationId, String connectionId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of(
                "/v1/organizations/{organizationId}/saml/{connectionId}/group-mappings",
                organizationId,
                connectionId)),
        SsoGroupMapping.class,
        options);
  }

  /**
   * Create group mapping.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @return the SSO group mapping
   */
  public SsoGroupMapping createGroupMapping(
      String organizationId, String connectionId, SsoGroupMappingCreateParams params) {
    return createGroupMapping(organizationId, connectionId, params, RequestOptions.none());
  }

  /**
   * Create group mapping. With per-call settings: see {@link #createGroupMapping(String, String,
   * SsoGroupMappingCreateParams)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SSO group mapping
   */
  public SsoGroupMapping createGroupMapping(
      String organizationId,
      String connectionId,
      SsoGroupMappingCreateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/saml/{connectionId}/group-mappings",
                    organizationId,
                    connectionId))
            .json(params),
        options,
        SsoGroupMapping.class);
  }

  /**
   * Delete group mapping.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param mappingId the mapping's id
   */
  public void deleteGroupMapping(String organizationId, String connectionId, String mappingId) {
    deleteGroupMapping(organizationId, connectionId, mappingId, RequestOptions.none());
  }

  /**
   * Delete group mapping. With per-call settings: see {@link #deleteGroupMapping(String, String,
   * String)}.
   *
   * @param organizationId the organization's id
   * @param connectionId the connection's id
   * @param mappingId the mapping's id
   * @param options per-call settings
   */
  public void deleteGroupMapping(
      String organizationId, String connectionId, String mappingId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/saml/{connectionId}/group-mappings/{mappingId}",
                organizationId,
                connectionId,
                mappingId)),
        options);
  }
}
