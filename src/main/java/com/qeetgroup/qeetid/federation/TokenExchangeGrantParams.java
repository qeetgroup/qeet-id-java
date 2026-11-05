package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** RFC 8693 token exchange: trade a token for a narrower one, or one that acts for its subject. */
public final class TokenExchangeGrantParams extends Params {
  private TokenExchangeGrantParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link TokenExchangeGrantParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link TokenExchangeGrantParams}. */
  public static final class Builder extends Params.Builder<TokenExchangeGrantParams, Builder> {
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
     * The subject token. Required.
     *
     * @param subjectToken the value
     * @return this builder
     */
    public Builder subjectToken(String subjectToken) {
      return setSecret("subject_token", nonNull(subjectToken, "subjectToken"));
    }

    /**
     * Only {@code urn:ietf:params:oauth:token-type:access_token} is supported (the default).
     *
     * @param subjectTokenType the value
     * @return this builder
     */
    public Builder subjectTokenType(String subjectTokenType) {
      return set("subject_token_type", nonNull(subjectTokenType, "subjectTokenType"));
    }

    /**
     * The delegate acting for the subject, such as an AI agent's token; the result carries an
     * {@code act} claim.
     *
     * @param actorToken the value
     * @return this builder
     */
    public Builder actorToken(String actorToken) {
      return setSecret("actor_token", nonNull(actorToken, "actorToken"));
    }

    /**
     * The actor token type.
     *
     * @param actorTokenType the value
     * @return this builder
     */
    public Builder actorTokenType(String actorTokenType) {
      return set("actor_token_type", nonNull(actorTokenType, "actorTokenType"));
    }

    /**
     * The requested token type.
     *
     * @param requestedTokenType the value
     * @return this builder
     */
    public Builder requestedTokenType(String requestedTokenType) {
      return set("requested_token_type", nonNull(requestedTokenType, "requestedTokenType"));
    }

    /**
     * A subset of the subject token's scopes.
     *
     * @param scope the value
     * @return this builder
     */
    public Builder scope(String scope) {
      return set("scope", nonNull(scope, "scope"));
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
    public TokenExchangeGrantParams build() {
      require("client_id", "clientId");
      require("subject_token", "subjectToken");
      return new TokenExchangeGrantParams(this);
    }
  }
}
