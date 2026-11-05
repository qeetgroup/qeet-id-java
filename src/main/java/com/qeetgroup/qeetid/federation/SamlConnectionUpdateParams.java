package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** The changes to a SAML connection: only the fields set are sent. */
public final class SamlConnectionUpdateParams extends Params {
  private SamlConnectionUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SamlConnectionUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SamlConnectionUpdateParams}. */
  public static final class Builder extends Params.Builder<SamlConnectionUpdateParams, Builder> {
    private Builder() {}

    /**
     * Its name.
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
    public SamlConnectionUpdateParams build() {
      return new SamlConnectionUpdateParams(this);
    }
  }
}
