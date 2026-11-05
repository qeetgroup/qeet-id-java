package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organisation's email templates. The set of templates is fixed; each can be overridden
 * and reset.
 *
 * <p>Get it from {@code qeetId.emailTemplates()}. Safe for concurrent use.
 */
public final class EmailTemplates {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.emailTemplates()}.
   *
   * @param http the client's transport
   */
  public EmailTemplates(Transport http) {
    this.http = http;
  }

  /**
   * Lists the email templates.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<EmailTemplate> list(String tenantId) {
    return list(tenantId, RequestOptions.none());
  }

  /**
   * Lists the email templates. With per-call settings: see {@link #list(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<EmailTemplate> list(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/email-templates", tenantId)),
        EmailTemplate.class,
        options);
  }

  /**
   * Fetches an email template.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @return the email template
   */
  public EmailTemplate get(String tenantId, String key) {
    return get(tenantId, key, RequestOptions.none());
  }

  /**
   * Fetches an email template. With per-call settings: see {@link #get(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @param options per-call settings
   * @return the email template
   */
  public EmailTemplate get(String tenantId, String key, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/email-templates/{key}", tenantId, key)),
        options,
        EmailTemplate.class);
  }

  /**
   * Saves the organisation's override of a template.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @param content the content
   * @return the email template
   */
  public EmailTemplate update(String tenantId, String key, EmailTemplateContent content) {
    return update(tenantId, key, content, RequestOptions.none());
  }

  /**
   * Saves the organisation's override of a template. With per-call settings: see {@link
   * #update(String, String, EmailTemplateContent)}.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @param content the content
   * @param options per-call settings
   * @return the email template
   */
  public EmailTemplate update(
      String tenantId, String key, EmailTemplateContent content, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT", Paths.of("/v1/tenants/{tenantId}/email-templates/{key}", tenantId, key))
            .json(content),
        options,
        EmailTemplate.class);
  }

  /**
   * Discards the override, going back to the platform's default.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @return the email template
   */
  public EmailTemplate reset(String tenantId, String key) {
    return reset(tenantId, key, RequestOptions.none());
  }

  /**
   * Discards the override, going back to the platform's default. With per-call settings: see {@link
   * #reset(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @param options per-call settings
   * @return the email template
   */
  public EmailTemplate reset(String tenantId, String key, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
            "DELETE", Paths.of("/v1/tenants/{tenantId}/email-templates/{key}", tenantId, key)),
        options,
        EmailTemplate.class);
  }

  /**
   * Renders a template with {@code vars}. Sends nothing.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @return the email template preview
   */
  public EmailTemplatePreview preview(String tenantId, String key) {
    return preview(
        tenantId, key, EmailTemplatePreviewParams.builder().build(), RequestOptions.none());
  }

  /**
   * Renders a template with {@code vars}. Sends nothing.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @param params the parameters
   * @return the email template preview
   */
  public EmailTemplatePreview preview(
      String tenantId, String key, EmailTemplatePreviewParams params) {
    return preview(tenantId, key, params, RequestOptions.none());
  }

  /**
   * Renders a template with {@code vars}. With per-call settings: see {@link #preview(String,
   * String, EmailTemplatePreviewParams)}.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @param params the parameters
   * @param options per-call settings
   * @return the email template preview
   */
  public EmailTemplatePreview preview(
      String tenantId, String key, EmailTemplatePreviewParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/email-templates/{key}/preview", tenantId, key))
            .json(params)
            .retrySafe(),
        options,
        EmailTemplatePreview.class);
  }

  /**
   * Sends a draft to the caller's own address.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @param params the parameters
   */
  public void sendTest(String tenantId, String key, EmailTemplateTestParams params) {
    sendTest(tenantId, key, params, RequestOptions.none());
  }

  /**
   * Sends a draft to the caller's own address. With per-call settings: see {@link #sendTest(String,
   * String, EmailTemplateTestParams)}.
   *
   * @param tenantId the organisation's id
   * @param key the template's key
   * @param params the parameters
   * @param options per-call settings
   */
  public void sendTest(
      String tenantId, String key, EmailTemplateTestParams params, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
                Paths.of("/v1/tenants/{tenantId}/email-templates/{key}/test", tenantId, key))
            .json(params),
        options);
  }
}
