package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** How long a service principal's replaced secret keeps getting tokens. */
public final class ServicePrincipalRotateSecretParams extends Params {
  private ServicePrincipalRotateSecretParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ServicePrincipalRotateSecretParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ServicePrincipalRotateSecretParams}. */
  public static final class Builder
      extends Params.Builder<ServicePrincipalRotateSecretParams, Builder> {
    private Builder() {}

    /**
     * How many hours the replaced credential keeps working: 0 stops it at once. The server bounds
     * it.
     *
     * @param overlapHours the value
     * @return this builder
     */
    public Builder overlapHours(int overlapHours) {
      return set("overlap_hours", overlapHours);
    }

    @Override
    public ServicePrincipalRotateSecretParams build() {
      return new ServicePrincipalRotateSecretParams(this);
    }
  }
}
