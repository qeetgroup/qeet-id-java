package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;
import java.util.List;
import org.jspecify.annotations.Nullable;

/** A new application (an OAuth / OpenID Connect client). */
public final class ApplicationCreateParams extends Params {
  private ApplicationCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ApplicationCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ApplicationCreateParams}. */
  public static final class Builder extends Params.Builder<ApplicationCreateParams, Builder> {
    private Builder() {}

    /**
     * Its name. Required.
     *
     * @param name the value
     * @return this builder
     */
    public Builder name(String name) {
      return set("name", nonNull(name, "name"));
    }

    /**
     * One of {@code "confidential"}, {@code "public"}. Required.
     *
     * @param type the value
     * @return this builder
     */
    public Builder type(String type) {
      return set("type", nonNull(type, "type"));
    }

    /**
     * The redirect uris.
     *
     * @param redirectUris the value
     * @return this builder
     */
    public Builder redirectUris(List<String> redirectUris) {
      return set("redirect_uris", listOf(redirectUris));
    }

    /**
     * The post logout uris.
     *
     * @param postLogoutUris the value
     * @return this builder
     */
    public Builder postLogoutUris(List<String> postLogoutUris) {
      return set("post_logout_uris", listOf(postLogoutUris));
    }

    /**
     * The grant types.
     *
     * @param grantTypes the value
     * @return this builder
     */
    public Builder grantTypes(List<String> grantTypes) {
      return set("grant_types", listOf(grantTypes));
    }

    /**
     * The scopes.
     *
     * @param scopes the value
     * @return this builder
     */
    public Builder scopes(List<String> scopes) {
      return set("scopes", listOf(scopes));
    }

    /**
     * The backchannel logout uri.
     *
     * @param backchannelLogoutUri the value
     * @return this builder
     */
    public Builder backchannelLogoutUri(String backchannelLogoutUri) {
      return set("backchannel_logout_uri", nonNull(backchannelLogoutUri, "backchannelLogoutUri"));
    }

    /**
     * The frontchannel logout uri.
     *
     * @param frontchannelLogoutUri the value
     * @return this builder
     */
    public Builder frontchannelLogoutUri(String frontchannelLogoutUri) {
      return set(
          "frontchannel_logout_uri", nonNull(frontchannelLogoutUri, "frontchannelLogoutUri"));
    }

    /**
     * The logo uri.
     *
     * @param logoUri the value
     * @return this builder
     */
    public Builder logoUri(String logoUri) {
      return set("logo_uri", nonNull(logoUri, "logoUri"));
    }

    /**
     * The client uri.
     *
     * @param clientUri the value
     * @return this builder
     */
    public Builder clientUri(String clientUri) {
      return set("client_uri", nonNull(clientUri, "clientUri"));
    }

    /**
     * Omit or null for the redirect URIs' origins. Null sends an explicit null, which clears it.
     *
     * @param webOrigins the value
     * @return this builder
     */
    public Builder webOrigins(@Nullable List<String> webOrigins) {
      return set("web_origins", webOrigins == null ? null : listOf(webOrigins));
    }

    @Override
    public ApplicationCreateParams build() {
      require("name", "name");
      require("type", "type");
      return new ApplicationCreateParams(this);
    }
  }
}
