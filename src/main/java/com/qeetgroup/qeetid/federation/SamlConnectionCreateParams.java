package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** A name alone creates a draft; the identity provider's details come together or not at all. */
public final class SamlConnectionCreateParams extends Params {
  private SamlConnectionCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SamlConnectionCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SamlConnectionCreateParams}. */
  public static final class Builder extends Params.Builder<SamlConnectionCreateParams, Builder> {
    private Builder() {}

    /**
     * Its name. Required.
     *
     * @param name the value
     * @return this builder
     */
    public Builder name(String name) {
      return set("name", nonNull(name, "name"));
    }

    /**
     * The idp entity's id.
     *
     * @param idpEntityId the value
     * @return this builder
     */
    public Builder idpEntityId(String idpEntityId) {
      return set("idp_entity_id", nonNull(idpEntityId, "idpEntityId"));
    }

    /**
     * The idp SSO URL.
     *
     * @param idpSsoUrl the value
     * @return this builder
     */
    public Builder idpSsoUrl(String idpSsoUrl) {
      return set("idp_sso_url", nonNull(idpSsoUrl, "idpSsoUrl"));
    }

    /**
     * The idp certificate.
     *
     * @param idpCertificate the value
     * @return this builder
     */
    public Builder idpCertificate(String idpCertificate) {
      return set("idp_certificate", nonNull(idpCertificate, "idpCertificate"));
    }

    /**
     * The email attribute.
     *
     * @param emailAttribute the value
     * @return this builder
     */
    public Builder emailAttribute(String emailAttribute) {
      return set("email_attribute", nonNull(emailAttribute, "emailAttribute"));
    }

    /**
     * The name attribute.
     *
     * @param nameAttribute the value
     * @return this builder
     */
    public Builder nameAttribute(String nameAttribute) {
      return set("name_attribute", nonNull(nameAttribute, "nameAttribute"));
    }

    /**
     * The groups attribute.
     *
     * @param groupsAttribute the value
     * @return this builder
     */
    public Builder groupsAttribute(String groupsAttribute) {
      return set("groups_attribute", nonNull(groupsAttribute, "groupsAttribute"));
    }

    /**
     * One of {@code "draft"}, {@code "active"}, {@code "disabled"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    @Override
    public SamlConnectionCreateParams build() {
      require("name", "name");
      return new SamlConnectionCreateParams(this);
    }
  }
}
