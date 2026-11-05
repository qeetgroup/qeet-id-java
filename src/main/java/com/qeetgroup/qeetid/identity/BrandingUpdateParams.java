package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** The organisation's branding, replaced: what isn't set is cleared. */
public final class BrandingUpdateParams extends Params {
  private BrandingUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link BrandingUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link BrandingUpdateParams}. */
  public static final class Builder extends Params.Builder<BrandingUpdateParams, Builder> {
    private Builder() {}

    /**
     * A URL or a base64 {@code data:} image (PNG, JPEG, WEBP or SVG, at most 2 MiB). Null sends an
     * explicit null, which clears it.
     *
     * @param logoUrl the value
     * @return this builder
     */
    public Builder logoUrl(@Nullable String logoUrl) {
      return set("logo_url", logoUrl);
    }

    /**
     * The primary color. Null sends an explicit null, which clears it.
     *
     * @param primaryColor the value
     * @return this builder
     */
    public Builder primaryColor(@Nullable String primaryColor) {
      return set("primary_color", primaryColor);
    }

    /**
     * The secondary color. Null sends an explicit null, which clears it.
     *
     * @param secondaryColor the value
     * @return this builder
     */
    public Builder secondaryColor(@Nullable String secondaryColor) {
      return set("secondary_color", secondaryColor);
    }

    /**
     * The custom domain. Null sends an explicit null, which clears it.
     *
     * @param customDomain the value
     * @return this builder
     */
    public Builder customDomain(@Nullable String customDomain) {
      return set("custom_domain", customDomain);
    }

    /**
     * The email from name. Null sends an explicit null, which clears it.
     *
     * @param emailFromName the value
     * @return this builder
     */
    public Builder emailFromName(@Nullable String emailFromName) {
      return set("email_from_name", emailFromName);
    }

    /**
     * The email from address. Null sends an explicit null, which clears it.
     *
     * @param emailFromAddress the value
     * @return this builder
     */
    public Builder emailFromAddress(@Nullable String emailFromAddress) {
      return set("email_from_address", emailFromAddress);
    }

    /**
     * {@code background_color} (six-digit hex) and {@code email_reply_to}; keep unknown keys when
     * writing back.
     *
     * @param settings the value
     * @return this builder
     */
    public Builder settings(Map<String, ?> settings) {
      return set("settings", nonNull(settings, "settings"));
    }

    @Override
    public BrandingUpdateParams build() {
      return new BrandingUpdateParams(this);
    }
  }
}
