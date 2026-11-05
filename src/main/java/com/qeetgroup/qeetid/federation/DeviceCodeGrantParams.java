package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** The RFC 8628 device-code grant: one poll for the device flow's tokens. */
public final class DeviceCodeGrantParams extends Params {
  private DeviceCodeGrantParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link DeviceCodeGrantParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link DeviceCodeGrantParams}. */
  public static final class Builder extends Params.Builder<DeviceCodeGrantParams, Builder> {
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
     * The device code. Required.
     *
     * @param deviceCode the value
     * @return this builder
     */
    public Builder deviceCode(String deviceCode) {
      return setSecret("device_code", nonNull(deviceCode, "deviceCode"));
    }

    @Override
    public DeviceCodeGrantParams build() {
      require("client_id", "clientId");
      require("device_code", "deviceCode");
      return new DeviceCodeGrantParams(this);
    }
  }
}
