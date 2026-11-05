package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** The identity provider's metadata: a URL to fetch it from, or the XML itself. */
public final class SamlMetadataParams extends Params {
  private SamlMetadataParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SamlMetadataParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SamlMetadataParams}. */
  public static final class Builder extends Params.Builder<SamlMetadataParams, Builder> {
    private Builder() {}

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

    @Override
    public SamlMetadataParams build() {
      return new SamlMetadataParams(this);
    }
  }
}
