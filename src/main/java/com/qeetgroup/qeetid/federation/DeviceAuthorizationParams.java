package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** An RFC 8628 device authorization request. */
public final class DeviceAuthorizationParams extends Params {
  private DeviceAuthorizationParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link DeviceAuthorizationParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link DeviceAuthorizationParams}. */
  public static final class Builder extends Params.Builder<DeviceAuthorizationParams, Builder> {
    private Builder() {}

    /**
     * The application's client ID. Required.
     *
     * @param clientId the value
     * @return this builder
     */
    public Builder clientId(String clientId) {
      return set("client_id", nonNull(clientId, "clientId"));
    }

    /**
     * Its secret; none for a public client.
     *
     * @param clientSecret the value
     * @return this builder
     */
    public Builder clientSecret(String clientSecret) {
      return setSecret("client_secret", nonNull(clientSecret, "clientSecret"));
    }

    /**
     * The scope.
     *
     * @param scope the value
     * @return this builder
     */
    public Builder scope(String scope) {
      return set("scope", nonNull(scope, "scope"));
    }

    @Override
    public DeviceAuthorizationParams build() {
      require("client_id", "clientId");
      return new DeviceAuthorizationParams(this);
    }
  }
}
