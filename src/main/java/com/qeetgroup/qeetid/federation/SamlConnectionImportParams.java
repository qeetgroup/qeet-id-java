package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** Creates a connection from the identity provider's metadata (a URL or the XML). */
public final class SamlConnectionImportParams extends Params {
  private SamlConnectionImportParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SamlConnectionImportParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SamlConnectionImportParams}. */
  public static final class Builder extends Params.Builder<SamlConnectionImportParams, Builder> {
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
     * A URL to fetch the identity provider's metadata from.
     *
     * @param metadataUrl the value
     * @return this builder
     */
    public Builder metadataUrl(String metadataUrl) {
      return set("metadata_url", nonNull(metadataUrl, "metadataUrl"));
    }

    /**
     * The identity provider's metadata XML.
     *
     * @param metadataXml the value
     * @return this builder
     */
    public Builder metadataXml(String metadataXml) {
      return set("metadata_xml", nonNull(metadataXml, "metadataXml"));
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
    public SamlConnectionImportParams build() {
      require("name", "name");
      return new SamlConnectionImportParams(this);
    }
  }
}
