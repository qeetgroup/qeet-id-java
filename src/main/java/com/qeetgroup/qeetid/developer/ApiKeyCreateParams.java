package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

/** A new API key. */
public final class ApiKeyCreateParams extends Params {
  private ApiKeyCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link ApiKeyCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link ApiKeyCreateParams}. */
  public static final class Builder extends Params.Builder<ApiKeyCreateParams, Builder> {
    private Builder() {}

    /**
     * The organisation's id. Required.
     *
     * @param tenantId the value
     * @return this builder
     */
    public Builder tenantId(String tenantId) {
      return set("tenant_id", nonNull(tenantId, "tenantId"));
    }

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
     * Makes a personal key that acts for this user. Null sends an explicit null, which clears it.
     *
     * @param userId the value
     * @return this builder
     */
    public Builder userId(@Nullable String userId) {
      return set("user_id", userId);
    }

    /**
     * The scopes.
     *
     * @param scopes the value
     * @return this builder
     */
    public Builder scopes(List<String> scopes) {
      return set("scopes", listOf(scopes));
    }

    /**
     * An RFC 3339 time; null or omitted for a key that doesn't expire. Null sends an explicit null,
     * which clears it.
     *
     * @param expiresAt the value
     * @return this builder
     */
    public Builder expiresAt(@Nullable Instant expiresAt) {
      return set("expires_at", expiresAt);
    }

    @Override
    public ApiKeyCreateParams build() {
      require("tenant_id", "tenantId");
      require("name", "name");
      return new ApiKeyCreateParams(this);
    }
  }
}
