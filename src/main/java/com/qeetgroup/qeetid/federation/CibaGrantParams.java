package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** The OpenID CIBA grant: one poll for a backchannel request's tokens. */
public final class CibaGrantParams extends Params {
  private CibaGrantParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link CibaGrantParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link CibaGrantParams}. */
  public static final class Builder extends Params.Builder<CibaGrantParams, Builder> {
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
     * The auth req's id. Required.
     *
     * @param authReqId the value
     * @return this builder
     */
    public Builder authReqId(String authReqId) {
      return setSecret("auth_req_id", nonNull(authReqId, "authReqId"));
    }

    @Override
    public CibaGrantParams build() {
      require("client_id", "clientId");
      require("auth_req_id", "authReqId");
      return new CibaGrantParams(this);
    }
  }
}
