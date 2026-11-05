package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/**
 * Adds a provider, or replaces one's settings. An empty {@code client_secret} keeps the stored one.
 * For Apple, {@code client_id} is the Services ID and {@code client_secret} the {@code .p8} key,
 * with {@code apple_team_id} and {@code apple_key_id}.
 */
public final class SocialProviderUpsertParams extends Params {
  private SocialProviderUpsertParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SocialProviderUpsertParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SocialProviderUpsertParams}. */
  public static final class Builder extends Params.Builder<SocialProviderUpsertParams, Builder> {
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
     * The client secret; none for a public client. Never printed.
     *
     * @param clientSecret the value
     * @return this builder
     */
    public Builder clientSecret(String clientSecret) {
      return setSecret("client_secret", nonNull(clientSecret, "clientSecret"));
    }

    /**
     * Required for an OpenID Connect provider.
     *
     * @param discoveryUrl the value
     * @return this builder
     */
    public Builder discoveryUrl(String discoveryUrl) {
      return set("discovery_url", nonNull(discoveryUrl, "discoveryUrl"));
    }

    /**
     * The apple team's id.
     *
     * @param appleTeamId the value
     * @return this builder
     */
    public Builder appleTeamId(String appleTeamId) {
      return set("apple_team_id", nonNull(appleTeamId, "appleTeamId"));
    }

    /**
     * The apple key's id.
     *
     * @param appleKeyId the value
     * @return this builder
     */
    public Builder appleKeyId(String appleKeyId) {
      return set("apple_key_id", nonNull(appleKeyId, "appleKeyId"));
    }

    @Override
    public SocialProviderUpsertParams build() {
      require("provider", "provider");
      require("client_id", "clientId");
      return new SocialProviderUpsertParams(this);
    }
  }
}
