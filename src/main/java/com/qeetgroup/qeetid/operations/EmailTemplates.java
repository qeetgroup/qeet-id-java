package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;

/**
 * Manages the organization's email templates. The set of templates is fixed; each can be overridden
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
   * @param organizationId the organization's id
   * @return the list
   */
  public ListResult<EmailTemplate> list(String organizationId) {
    return list(organizationId, RequestOptions.none());
  }

  /**
   * Lists the email templates. With per-call settings: see {@link #list(String)}.
   *
   * @param organizationId the organization's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<EmailTemplate> list(String organizationId, RequestOptions options) {
    return http.list(
        ApiRequest.get(
            Paths.of("/v1/organizations/{organizationId}/email-templates", organizationId)),
        EmailTemplate.class,
        options);
  }

  /**
   * Fetches an email template.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @return the email template
   */
  public EmailTemplate get(String organizationId, String key) {
    return get(organizationId, key, RequestOptions.none());
  }

  /**
   * Fetches an email template. With per-call settings: see {@link #get(String, String)}.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @param options per-call settings
   * @return the email template
   */
  public EmailTemplate get(String organizationId, String key, RequestOptions options) {
    return http.execute(
        ApiRequest.get(
            Paths.of(
                "/v1/organizations/{organizationId}/email-templates/{key}", organizationId, key)),
        options,
        EmailTemplate.class);
  }

  /**
   * Saves the organization's override of a template.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @param content the content
   * @return the email template
   */
  public EmailTemplate update(String organizationId, String key, EmailTemplateContent content) {
    return update(organizationId, key, content, RequestOptions.none());
  }

  /**
   * Saves the organization's override of a template. With per-call settings: see {@link
   * #update(String, String, EmailTemplateContent)}.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @param content the content
   * @param options per-call settings
   * @return the email template
   */
  public EmailTemplate update(
      String organizationId, String key, EmailTemplateContent content, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
                "PUT",
                Paths.of(
                    "/v1/organizations/{organizationId}/email-templates/{key}",
                    organizationId,
                    key))
            .json(content),
        options,
        EmailTemplate.class);
  }

  /**
   * Discards the override, going back to the platform's default.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @return the email template
   */
  public EmailTemplate reset(String organizationId, String key) {
    return reset(organizationId, key, RequestOptions.none());
  }

  /**
   * Discards the override, going back to the platform's default. With per-call settings: see {@link
   * #reset(String, String)}.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @param options per-call settings
   * @return the email template
   */
  public EmailTemplate reset(String organizationId, String key, RequestOptions options) {
    return http.execute(
        ApiRequest.of(
            "DELETE",
            Paths.of(
                "/v1/organizations/{organizationId}/email-templates/{key}", organizationId, key)),
        options,
        EmailTemplate.class);
  }

  /**
   * Renders a template with {@code vars}. Sends nothing.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @return the email template preview
   */
  public EmailTemplatePreview preview(String organizationId, String key) {
    return preview(
        organizationId, key, EmailTemplatePreviewParams.builder().build(), RequestOptions.none());
  }

  /**
   * Renders a template with {@code vars}. Sends nothing.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @param params the parameters
   * @return the email template preview
   */
  public EmailTemplatePreview preview(
      String organizationId, String key, EmailTemplatePreviewParams params) {
    return preview(organizationId, key, params, RequestOptions.none());
  }

  /**
   * Renders a template with {@code vars}. With per-call settings: see {@link #preview(String,
   * String, EmailTemplatePreviewParams)}.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @param params the parameters
   * @param options per-call settings
   * @return the email template preview
   */
  public EmailTemplatePreview preview(
      String organizationId,
      String key,
      EmailTemplatePreviewParams params,
      RequestOptions options) {
    return http.execute(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/email-templates/{key}/preview",
                    organizationId,
                    key))
            .json(params)
            .retrySafe(),
        options,
        EmailTemplatePreview.class);
  }

  /**
   * Sends a draft to the caller's own address.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @param params the parameters
   */
  public void sendTest(String organizationId, String key, EmailTemplateTestParams params) {
    sendTest(organizationId, key, params, RequestOptions.none());
  }

  /**
   * Sends a draft to the caller's own address. With per-call settings: see {@link #sendTest(String,
   * String, EmailTemplateTestParams)}.
   *
   * @param organizationId the organization's id
   * @param key the template's key
   * @param params the parameters
   * @param options per-call settings
   */
  public void sendTest(
      String organizationId, String key, EmailTemplateTestParams params, RequestOptions options) {
    http.executeVoid(
        ApiRequest.post(
                Paths.of(
                    "/v1/organizations/{organizationId}/email-templates/{key}/test",
                    organizationId,
                    key))
            .json(params),
        options);
  }
}
