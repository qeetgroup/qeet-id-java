package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** The {@code refresh_token} grant. */
public final class RefreshTokenGrantParams extends Params {
  private RefreshTokenGrantParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RefreshTokenGrantParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RefreshTokenGrantParams}. */
  public static final class Builder extends Params.Builder<RefreshTokenGrantParams, Builder> {
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
     * The refresh token. Required.
     *
     * @param refreshToken the value
     * @return this builder
     */
    public Builder refreshToken(String refreshToken) {
      return setSecret("refresh_token", nonNull(refreshToken, "refreshToken"));
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

    /**
     * RFC 8707: switches the bound resource; omit to keep it.
     *
     * @param resource the value
     * @return this builder
     */
    public Builder resource(String resource) {
      return set("resource", nonNull(resource, "resource"));
    }

    @Override
    public RefreshTokenGrantParams build() {
      require("client_id", "clientId");
      require("refresh_token", "refreshToken");
      return new RefreshTokenGrantParams(this);
    }
  }
}
