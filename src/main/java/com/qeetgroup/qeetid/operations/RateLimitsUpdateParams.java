package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** The rate-limit buckets to set; the others keep their values. */
public final class RateLimitsUpdateParams extends Params {
  private RateLimitsUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RateLimitsUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RateLimitsUpdateParams}. */
  public static final class Builder extends Params.Builder<RateLimitsUpdateParams, Builder> {
    private Builder() {}

    /**
     * The tenant.
     *
     * @param tenant the value
     * @return this builder
     */
    public Builder tenant(RateLimitBucket tenant) {
      return set("tenant", nonNull(tenant, "tenant"));
    }

    /**
     * The user.
     *
     * @param user the value
     * @return this builder
     */
    public Builder user(RateLimitBucket user) {
      return set("user", nonNull(user, "user"));
    }

    /**
     * The API key.
     *
     * @param apiKey the value
     * @return this builder
     */
    public Builder apiKey(RateLimitBucket apiKey) {
      return set("api_key", nonNull(apiKey, "apiKey"));
    }

    @Override
    public RateLimitsUpdateParams build() {
      return new RateLimitsUpdateParams(this);
    }
  }
}
