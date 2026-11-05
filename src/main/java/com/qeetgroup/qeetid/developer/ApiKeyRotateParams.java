package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;

/** How long the replaced API key keeps working. */
public final class ApiKeyRotateParams extends Params {
  private ApiKeyRotateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ApiKeyRotateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ApiKeyRotateParams}. */
  public static final class Builder extends Params.Builder<ApiKeyRotateParams, Builder> {
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

    @Override
    public ApiKeyRotateParams build() {
      return new ApiKeyRotateParams(this);
    }
  }
}
