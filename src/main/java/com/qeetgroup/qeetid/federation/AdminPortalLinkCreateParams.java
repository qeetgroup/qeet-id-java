package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;
import java.util.List;

/** A new admin portal link. */
public final class AdminPortalLinkCreateParams extends Params {
  private AdminPortalLinkCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AdminPortalLinkCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AdminPortalLinkCreateParams}. */
  public static final class Builder extends Params.Builder<AdminPortalLinkCreateParams, Builder> {
    private Builder() {}

    /**
     * One of {@code "saml"}, {@code "scim"}, {@code "domain_verification"}. Required.
     *
     * @param capabilities the value
     * @return this builder
     */
    public Builder capabilities(List<String> capabilities) {
      return set("capabilities", listOf(capabilities));
    }

    /**
     * Clamped to 15 minutes to 24 hours; 1 hour by default.
     *
     * @param ttlSeconds the value
     * @return this builder
     */
    public Builder ttlSeconds(int ttlSeconds) {
      return set("ttl_seconds", ttlSeconds);
    }

    @Override
    public AdminPortalLinkCreateParams build() {
      require("capabilities", "capabilities");
      return new AdminPortalLinkCreateParams(this);
    }
  }
}
