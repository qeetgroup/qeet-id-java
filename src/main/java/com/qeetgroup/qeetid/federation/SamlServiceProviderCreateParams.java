package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;
import java.util.List;

/** A new SAML service provider. */
public final class SamlServiceProviderCreateParams extends Params {
  private SamlServiceProviderCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SamlServiceProviderCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SamlServiceProviderCreateParams}. */
  public static final class Builder
      extends Params.Builder<SamlServiceProviderCreateParams, Builder> {
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
     * The entity's id. Required.
     *
     * @param entityId the value
     * @return this builder
     */
    public Builder entityId(String entityId) {
      return set("entity_id", nonNull(entityId, "entityId"));
    }

    /**
     * The acs URL. Required.
     *
     * @param acsUrl the value
     * @return this builder
     */
    public Builder acsUrl(String acsUrl) {
      return set("acs_url", nonNull(acsUrl, "acsUrl"));
    }

    /**
     * The name ID format.
     *
     * @param nameIdFormat the value
     * @return this builder
     */
    public Builder nameIdFormat(String nameIdFormat) {
      return set("name_id_format", nonNull(nameIdFormat, "nameIdFormat"));
    }

    /**
     * The name ID attribute.
     *
     * @param nameIdAttribute the value
     * @return this builder
     */
    public Builder nameIdAttribute(String nameIdAttribute) {
      return set("name_id_attribute", nonNull(nameIdAttribute, "nameIdAttribute"));
    }

    /**
     * The certificate.
     *
     * @param certificate the value
     * @return this builder
     */
    public Builder certificate(String certificate) {
      return set("certificate", nonNull(certificate, "certificate"));
    }

    /**
     * Whether require signed requests.
     *
     * @param requireSignedRequests the value
     * @return this builder
     */
    public Builder requireSignedRequests(boolean requireSignedRequests) {
      return set("require_signed_requests", requireSignedRequests);
    }

    /**
     * The encrypt assertions.
     *
     * @param encryptAssertions the value
     * @return this builder
     */
    public Builder encryptAssertions(boolean encryptAssertions) {
      return set("encrypt_assertions", encryptAssertions);
    }

    /**
     * The attributes.
     *
     * @param attributes the value
     * @return this builder
     */
    public Builder attributes(List<SamlAttributeMapping> attributes) {
      return set("attributes", listOf(attributes));
    }

    /**
     * Whether assignment required.
     *
     * @param assignmentRequired the value
     * @return this builder
     */
    public Builder assignmentRequired(boolean assignmentRequired) {
      return set("assignment_required", assignmentRequired);
    }

    @Override
    public SamlServiceProviderCreateParams build() {
      require("name", "name");
      require("entity_id", "entityId");
      require("acs_url", "acsUrl");
      return new SamlServiceProviderCreateParams(this);
    }
  }
}
