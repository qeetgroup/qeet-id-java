package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;

/** A new authentication hook. */
public final class AuthHookCreateParams extends Params {
  private AuthHookCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AuthHookCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AuthHookCreateParams}. */
  public static final class Builder extends Params.Builder<AuthHookCreateParams, Builder> {
    private Builder() {}

    /**
     * The URL. Required.
     *
     * @param url the value
     * @return this builder
     */
    public Builder url(String url) {
      return set("url", nonNull(url, "url"));
    }

    /**
     * Signs every call; generated when omitted.
     *
     * @param secret the value
     * @return this builder
     */
    public Builder secret(String secret) {
      return setSecret("secret", nonNull(secret, "secret"));
    }

    /**
     * Default true: an unreachable hook doesn't block sign-in.
     *
     * @param failOpen the value
     * @return this builder
     */
    public Builder failOpen(boolean failOpen) {
      return set("fail_open", failOpen);
    }

    @Override
    public AuthHookCreateParams build() {
      require("url", "url");
      return new AuthHookCreateParams(this);
    }
  }
}
