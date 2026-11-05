package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;

/** A third-party OAuth provider to register, or whose settings to replace. */
public final class TokenVaultProviderCreateParams extends Params {
  private TokenVaultProviderCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link TokenVaultProviderCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link TokenVaultProviderCreateParams}. */
  public static final class Builder
      extends Params.Builder<TokenVaultProviderCreateParams, Builder> {
    private Builder() {}

    /**
     * The provider. Required.
     *
     * @param provider the value
     * @return this builder
     */
    public Builder provider(String provider) {
      return set("provider", nonNull(provider, "provider"));
    }

    /**
     * The client's id. Required.
     *
     * @param clientId the value
     * @return this builder
     */
    public Builder clientId(String clientId) {
      return set("client_id", nonNull(clientId, "clientId"));
    }

    /**
     * The client secret; none for a public client. Never printed. Required.
     *
     * @param clientSecret the value
     * @return this builder
     */
    public Builder clientSecret(String clientSecret) {
      return setSecret("client_secret", nonNull(clientSecret, "clientSecret"));
    }

    /**
     * The authorize URL. Required.
     *
     * @param authorizeUrl the value
     * @return this builder
     */
    public Builder authorizeUrl(String authorizeUrl) {
      return set("authorize_url", nonNull(authorizeUrl, "authorizeUrl"));
    }

    /**
     * The token URL. Required.
     *
     * @param tokenUrl the value
     * @return this builder
     */
    public Builder tokenUrl(String tokenUrl) {
      return set("token_url", nonNull(tokenUrl, "tokenUrl"));
    }

    /**
     * The scopes.
     *
     * @param scopes the value
     * @return this builder
     */
    public Builder scopes(String scopes) {
      return set("scopes", nonNull(scopes, "scopes"));
    }

    @Override
    public TokenVaultProviderCreateParams build() {
      require("provider", "provider");
      require("client_id", "clientId");
      require("client_secret", "clientSecret");
      require("authorize_url", "authorizeUrl");
      require("token_url", "tokenUrl");
      return new TokenVaultProviderCreateParams(this);
    }
  }
}
