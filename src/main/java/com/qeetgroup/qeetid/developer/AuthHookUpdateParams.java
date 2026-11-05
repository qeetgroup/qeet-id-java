package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;

/** The changes to an authentication hook: only the fields set are sent. */
public final class AuthHookUpdateParams extends Params {
  private AuthHookUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AuthHookUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AuthHookUpdateParams}. */
  public static final class Builder extends Params.Builder<AuthHookUpdateParams, Builder> {
    private Builder() {}

    /**
     * The URL.
     *
     * @param url the value
     * @return this builder
     */
    public Builder url(String url) {
      return set("url", nonNull(url, "url"));
    }

    /**
     * The secret. Never printed.
     *
     * @param secret the value
     * @return this builder
     */
    public Builder secret(String secret) {
      return setSecret("secret", nonNull(secret, "secret"));
    }

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
     * The fail open.
     *
     * @param failOpen the value
     * @return this builder
     */
    public Builder failOpen(boolean failOpen) {
      return set("fail_open", failOpen);
    }

    @Override
    public AuthHookUpdateParams build() {
      return new AuthHookUpdateParams(this);
    }
  }
}
