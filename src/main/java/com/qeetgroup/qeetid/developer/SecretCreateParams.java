package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;
import org.jspecify.annotations.Nullable;

/** A new secret. */
public final class SecretCreateParams extends Params {
  private SecretCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SecretCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SecretCreateParams}. */
  public static final class Builder extends Params.Builder<SecretCreateParams, Builder> {
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
     * The scope. Required.
     *
     * @param scope the value
     * @return this builder
     */
    public Builder scope(String scope) {
      return set("scope", nonNull(scope, "scope"));
    }

    /**
     * The value. Required.
     *
     * @param value the value
     * @return this builder
     */
    public Builder value(String value) {
      return setSecret("value", nonNull(value, "value"));
    }

    /**
     * Flags the secret overdue this many days after its value last changed; null for never. Null
     * sends an explicit null, which clears it.
     *
     * @param rotationIntervalDays the value
     * @return this builder
     */
    public Builder rotationIntervalDays(@Nullable Integer rotationIntervalDays) {
      return set("rotation_interval_days", rotationIntervalDays);
    }

    @Override
    public SecretCreateParams build() {
      require("name", "name");
      require("scope", "scope");
      require("value", "value");
      return new SecretCreateParams(this);
    }
  }
}
