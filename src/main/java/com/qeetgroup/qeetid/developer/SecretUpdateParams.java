package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;
import org.jspecify.annotations.Nullable;

/** The changes to a secret: only the fields set are sent. */
public final class SecretUpdateParams extends Params {
  private SecretUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SecretUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SecretUpdateParams}. */
  public static final class Builder extends Params.Builder<SecretUpdateParams, Builder> {
    private Builder() {}

    /**
     * The scope. Null sends an explicit null, which clears it.
     *
     * @param scope the value
     * @return this builder
     */
    public Builder scope(@Nullable String scope) {
      return set("scope", scope);
    }

    /**
     * The value. Null sends an explicit null, which clears it.
     *
     * @param value the value
     * @return this builder
     */
    public Builder value(@Nullable String value) {
      return setSecret("value", value);
    }

    /**
     * The rotation interval days. Null sends an explicit null, which clears it.
     *
     * @param rotationIntervalDays the value
     * @return this builder
     */
    public Builder rotationIntervalDays(@Nullable Integer rotationIntervalDays) {
      return set("rotation_interval_days", rotationIntervalDays);
    }

    @Override
    public SecretUpdateParams build() {
      return new SecretUpdateParams(this);
    }
  }
}
