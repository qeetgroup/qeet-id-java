package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** The {@code authorization_code} grant: a code to redeem, with its PKCE verifier. */
public final class AuthorizationCodeGrantParams extends Params {
  private AuthorizationCodeGrantParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AuthorizationCodeGrantParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AuthorizationCodeGrantParams}. */
  public static final class Builder extends Params.Builder<AuthorizationCodeGrantParams, Builder> {
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
     * The code. Required.
     *
     * @param code the value
     * @return this builder
     */
    public Builder code(String code) {
      return setSecret("code", nonNull(code, "code"));
    }

    /**
     * The redirect uri. Required.
     *
     * @param redirectUri the value
     * @return this builder
     */
    public Builder redirectUri(String redirectUri) {
      return set("redirect_uri", nonNull(redirectUri, "redirectUri"));
    }

    /**
     * The PKCE verifier of the challenge sent to the authorization endpoint.
     *
     * @param codeVerifier the value
     * @return this builder
     */
    public Builder codeVerifier(String codeVerifier) {
      return setSecret("code_verifier", nonNull(codeVerifier, "codeVerifier"));
    }

    /**
     * RFC 8707: the resource the token is for.
     *
     * @param resource the value
     * @return this builder
     */
    public Builder resource(String resource) {
      return set("resource", nonNull(resource, "resource"));
    }

    @Override
    public AuthorizationCodeGrantParams build() {
      require("client_id", "clientId");
      require("code", "code");
      require("redirect_uri", "redirectUri");
      return new AuthorizationCodeGrantParams(this);
    }
  }
}
