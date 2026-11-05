package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** A new RFC 8707 resource. */
public final class OAuthResourceCreateParams extends Params {
  private OAuthResourceCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link OAuthResourceCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link OAuthResourceCreateParams}. */
  public static final class Builder extends Params.Builder<OAuthResourceCreateParams, Builder> {
    private Builder() {}

    /**
     * The identifier. Required.
     *
     * @param identifier the value
     * @return this builder
     */
    public Builder identifier(String identifier) {
      return set("identifier", nonNull(identifier, "identifier"));
    }

    /**
     * Its name.
     *
     * @param name the value
     * @return this builder
     */
    public Builder name(String name) {
      return set("name", nonNull(name, "name"));
    }

    /**
     * The server client's id.
     *
     * @param serverClientId the value
     * @return this builder
     */
    public Builder serverClientId(String serverClientId) {
      return set("server_client_id", nonNull(serverClientId, "serverClientId"));
    }

    @Override
    public OAuthResourceCreateParams build() {
      require("identifier", "identifier");
      return new OAuthResourceCreateParams(this);
    }
  }
}
