package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;

/** The changes to a domain's sign-in routing: only the fields set are sent. */
public final class DomainUpdateParams extends Params {
  private DomainUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link DomainUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link DomainUpdateParams}. */
  public static final class Builder extends Params.Builder<DomainUpdateParams, Builder> {
    private Builder() {}

    /**
     * Whether SSO enabled.
     *
     * @param ssoEnabled the value
     * @return this builder
     */
    public Builder ssoEnabled(boolean ssoEnabled) {
      return set("sso_enabled", ssoEnabled);
    }

    /**
     * Whether jit enabled.
     *
     * @param jitEnabled the value
     * @return this builder
     */
    public Builder jitEnabled(boolean jitEnabled) {
      return set("jit_enabled", jitEnabled);
    }

    @Override
    public DomainUpdateParams build() {
      return new DomainUpdateParams(this);
    }
  }
}
