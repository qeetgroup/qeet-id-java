package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import java.util.Map;

/**
 * The fields an update may change. Changing {@code status} is an owner's; the region and the plan
 * can't change here.
 */
public final class OrganizationUpdateParams extends Params {
  private OrganizationUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link OrganizationUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link OrganizationUpdateParams}. */
  public static final class Builder extends Params.Builder<OrganizationUpdateParams, Builder> {
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
     * One of {@code "active"}, {@code "suspended"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    /**
     * The logo, a hosted URL or a small inline data URL, up to 3,000,000 characters; empty removes
     * it.
     *
     * @param logoUrl the value
     * @return this builder
     */
    public Builder logoUrl(String logoUrl) {
      return set("logo_url", nonNull(logoUrl, "logoUrl"));
    }

    /**
     * Free-form data.
     *
     * @param metadata the value
     * @return this builder
     */
    public Builder metadata(Map<String, ?> metadata) {
      return set("metadata", nonNull(metadata, "metadata"));
    }

    @Override
    public OrganizationUpdateParams build() {
      return new OrganizationUpdateParams(this);
    }
  }
}
