package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/** How to replace a SCIM token: the overlap, and the new token's expiry. */
public final class ScimTokenRotateParams extends Params {
  private ScimTokenRotateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ScimTokenRotateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ScimTokenRotateParams}. */
  public static final class Builder extends Params.Builder<ScimTokenRotateParams, Builder> {
    private Builder() {}

    /**
     * How many hours the replaced credential keeps working: 0 stops it at once. The server bounds
     * it.
     *
     * @param overlapHours the value
     * @return this builder
     */
    public Builder overlapHours(int overlapHours) {
      return set("overlap_hours", overlapHours);
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
    public ScimTokenRotateParams build() {
      return new ScimTokenRotateParams(this);
    }
  }
}
