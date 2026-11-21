package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;
import java.util.List;

/** A new service principal. */
public final class ServicePrincipalCreateParams extends Params {
  private ServicePrincipalCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ServicePrincipalCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ServicePrincipalCreateParams}. */
  public static final class Builder extends Params.Builder<ServicePrincipalCreateParams, Builder> {
    private Builder() {}

    /**
     * The organization's id. Required.
     *
     * @param organizationId the value
     * @return this builder
     */
    public Builder organizationId(String organizationId) {
      return set("organization_id", nonNull(organizationId, "organizationId"));
    }

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
     * Its description.
     *
     * @param description the value
     * @return this builder
     */
    public Builder description(String description) {
      return set("description", nonNull(description, "description"));
    }

    /**
     * The scopes.
     *
     * @param scopes the value
     * @return this builder
     */
    public Builder scopes(List<String> scopes) {
      return set("scopes", listOf(scopes));
    }

    @Override
    public ServicePrincipalCreateParams build() {
      require("organization_id", "organizationId");
      require("name", "name");
      return new ServicePrincipalCreateParams(this);
    }
  }
}
