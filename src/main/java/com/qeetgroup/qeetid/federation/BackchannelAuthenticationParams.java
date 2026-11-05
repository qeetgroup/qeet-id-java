package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** An OpenID CIBA request, in poll mode. CIBA refuses public clients. */
public final class BackchannelAuthenticationParams extends Params {
  private BackchannelAuthenticationParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link BackchannelAuthenticationParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link BackchannelAuthenticationParams}. */
  public static final class Builder
      extends Params.Builder<BackchannelAuthenticationParams, Builder> {
    private Builder() {}

    /**
     * A confidential client: CIBA refuses public clients. Required.
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
     * The person's email address in the client's organisation. Required.
     *
     * @param loginHint the value
     * @return this builder
     */
    public Builder loginHint(String loginHint) {
      return set("login_hint", nonNull(loginHint, "loginHint"));
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
     * Shown to the person in the approval request, such as a transaction reference.
     *
     * @param bindingMessage the value
     * @return this builder
     */
    public Builder bindingMessage(String bindingMessage) {
      return set("binding_message", nonNull(bindingMessage, "bindingMessage"));
    }

    @Override
    public BackchannelAuthenticationParams build() {
      require("client_id", "clientId");
      require("client_secret", "clientSecret");
      require("login_hint", "loginHint");
      return new BackchannelAuthenticationParams(this);
    }
  }
}
