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
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the SAML connection
   */
  public SamlConnection create(String tenantId, SamlConnectionCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates a SAML connection. With per-call settings: see {@link #create(String,
   * SamlConnectionCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection create(
      String tenantId, SamlConnectionCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/saml", tenantId)).json(params),
        options,
        SamlConnection.class);
  }

  /**
   * Creates a connection from the identity provider's metadata.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the SAML connection
   */
  public SamlConnection createFromMetadata(String tenantId, SamlConnectionImportParams params) {
    return createFromMetadata(tenantId, params, RequestOptions.none());
  }

  /**
   * Creates a connection from the identity provider's metadata. With per-call settings: see {@link
   * #createFromMetadata(String, SamlConnectionImportParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection createFromMetadata(
      String tenantId, SamlConnectionImportParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/saml/import", tenantId)).json(params),
        options,
        SamlConnection.class);
  }

  /**
   * Fetches a SAML connection.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @return the SAML connection
   */
  public SamlConnection get(String tenantId, String connectionId) {
    return get(tenantId, connectionId, RequestOptions.none());
  }

  /**
   * Fetches a SAML connection. With per-call settings: see {@link #get(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection get(String tenantId, String connectionId, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of("/v1/tenants/{tenantId}/saml/{connectionId}", tenantId, connectionId)),
        options,
        SamlConnection.class);
  }

  /**
   * Lists the SAML connections.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<SamlConnection> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the SAML connections. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<SamlConnection> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/saml", tenantId)),
        SamlConnection.class,
        options);
  }

  /**
   * Changes only the fields given.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @return the SAML connection
   */
  public SamlConnection update(
      String tenantId, String connectionId, SamlConnectionUpdateParams params) {
    return update(tenantId, connectionId, params, RequestOptions.none());
  }

  /**
   * Changes only the fields given. With per-call settings: see {@link #update(String, String,
   * SamlConnectionUpdateParams)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection update(
      String tenantId,
      String connectionId,
      SamlConnectionUpdateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PATCH",
                Paths.of("/v1/tenants/{tenantId}/saml/{connectionId}", tenantId, connectionId))
            .json(params),
        options,
        SamlConnection.class);
  }

  /**
   * Deletes a SAML connection.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   */
  public void delete(String tenantId, String connectionId) {
    delete(tenantId, connectionId, RequestOptions.none());
  }

  /**
   * Deletes a SAML connection. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String connectionId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of("/v1/tenants/{tenantId}/saml/{connectionId}", tenantId, connectionId)),
        options);
  }

  /**
   * Updates the connection from the identity provider's metadata.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @return the SAML connection
   */
  public SamlConnection updateMetadata(
      String tenantId, String connectionId, SamlMetadataParams params) {
    return updateMetadata(tenantId, connectionId, params, RequestOptions.none());
  }

  /**
   * Updates the connection from the identity provider's metadata. With per-call settings: see
   * {@link #updateMetadata(String, String, SamlMetadataParams)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SAML connection
   */
  public SamlConnection updateMetadata(
      String tenantId, String connectionId, SamlMetadataParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/saml/{connectionId}/metadata", tenantId, connectionId))
            .json(params),
        options,
        SamlConnection.class);
  }

  /**
   * Adds a signing certificate, for a rotation without downtime.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param certificate the certificate
   * @return the SAML certificate
   */
  public SamlCertificate addCertificate(String tenantId, String connectionId, String certificate) {
    return addCertificate(tenantId, connectionId, certificate, RequestOptions.none());
  }

  /**
   * Adds a signing certificate, for a rotation without downtime. With per-call settings: see {@link
   * #addCertificate(String, String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param certificate the certificate
   * @param options per-call settings
   * @return the SAML certificate
   */
  public SamlCertificate addCertificate(
      String tenantId, String connectionId, String certificate, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/saml/{connectionId}/certificates",
                    tenantId,
                    connectionId))
            .json(Bodies.of("certificate", certificate)),
        options,
        SamlCertificate.class);
  }

  /**
   * Remove certificate.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param certificateId the certificate's id
   */
  public void removeCertificate(String tenantId, String connectionId, String certificateId) {
    removeCertificate(tenantId, connectionId, certificateId, RequestOptions.none());
  }

  /**
   * Remove certificate. With per-call settings: see {@link #removeCertificate(String, String,
   * String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param certificateId the certificate's id
   * @param options per-call settings
   */
  public void removeCertificate(
      String tenantId, String connectionId, String certificateId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/saml/{connectionId}/certificates/{certificateId}",
                tenantId,
                connectionId,
                certificateId)),
        options);
  }

  /**
   * Checks the configuration.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @return the connection test result
   */
  public ConnectionTestResult test(String tenantId, String connectionId) {
    return test(tenantId, connectionId, RequestOptions.none());
  }

  /**
   * Checks the configuration. With per-call settings: see {@link #test(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return the connection test result
   */
  public ConnectionTestResult test(String tenantId, String connectionId, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/saml/{connectionId}/test", tenantId, connectionId))
            .retrySafe(),
        options,
        ConnectionTestResult.class);
  }

  /**
   * List group mappings.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @return the list
   */
  public ListResult<SsoGroupMapping> listGroupMappings(String tenantId, String connectionId) {
    return listGroupMappings(tenantId, connectionId, RequestOptions.none());
  }

  /**
   * List group mappings. With per-call settings: see {@link #listGroupMappings(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<SsoGroupMapping> listGroupMappings(
      String tenantId, String connectionId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of(
                "/v1/tenants/{tenantId}/saml/{connectionId}/group-mappings",
                tenantId,
                connectionId)),
        SsoGroupMapping.class,
        options);
  }

  /**
   * Create group mapping.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @return the SSO group mapping
   */
  public SsoGroupMapping createGroupMapping(
      String tenantId, String connectionId, SsoGroupMappingCreateParams params) {
    return createGroupMapping(tenantId, connectionId, params, RequestOptions.none());
  }

  /**
   * Create group mapping. With per-call settings: see {@link #createGroupMapping(String, String,
   * SsoGroupMappingCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param params the parameters
   * @param options per-call settings
   * @return the SSO group mapping
   */
  public SsoGroupMapping createGroupMapping(
      String tenantId,
      String connectionId,
      SsoGroupMappingCreateParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/tenants/{tenantId}/saml/{connectionId}/group-mappings",
                    tenantId,
                    connectionId))
            .json(params),
        options,
        SsoGroupMapping.class);
  }

  /**
   * Delete group mapping.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param mappingId the mapping's id
   */
  public void deleteGroupMapping(String tenantId, String connectionId, String mappingId) {
    deleteGroupMapping(tenantId, connectionId, mappingId, RequestOptions.none());
  }

  /**
   * Delete group mapping. With per-call settings: see {@link #deleteGroupMapping(String, String,
   * String)}.
   *
   * @param tenantId the organisation's id
   * @param connectionId the connection's id
   * @param mappingId the mapping's id
   * @param options per-call settings
   */
  public void deleteGroupMapping(
      String tenantId, String connectionId, String mappingId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/tenants/{tenantId}/saml/{connectionId}/group-mappings/{mappingId}",
                tenantId,
                connectionId,
                mappingId)),
        options);
  }
}
