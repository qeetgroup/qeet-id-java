package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** The changes to a social provider: only the fields set are sent. */
public final class SocialProviderUpdateParams extends Params {
  private SocialProviderUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SocialProviderUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SocialProviderUpdateParams}. */
  public static final class Builder extends Params.Builder<SocialProviderUpdateParams, Builder> {
    private Builder() {}

    /**
     * Whether it is on.
     *
     * @param enabled the value
     * @return this builder
     */
    public Builder enabled(boolean enabled) {
      return set("enabled", enabled);
    }

    /**
     * The client's id.
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
     * The discovery URL.
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
    public SocialProviderUpdateParams build() {
      return new SocialProviderUpdateParams(this);
    }
  }
}
