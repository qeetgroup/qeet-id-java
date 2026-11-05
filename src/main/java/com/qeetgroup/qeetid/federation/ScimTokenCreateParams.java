package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/** A new SCIM token. */
public final class ScimTokenCreateParams extends Params {
  private ScimTokenCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ScimTokenCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ScimTokenCreateParams}. */
  public static final class Builder extends Params.Builder<ScimTokenCreateParams, Builder> {
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
     * When it expires. Null sends an explicit null, which clears it.
     *
     * @param expiresAt the value
     * @return this builder
     */
    public Builder expiresAt(@Nullable Instant expiresAt) {
      return set("expires_at", expiresAt);
    }

    @Override
    public ScimTokenCreateParams build() {
      require("name", "name");
      return new ScimTokenCreateParams(this);
    }
  }
}
