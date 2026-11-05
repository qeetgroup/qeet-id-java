package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * The fields an update may change. Changing the email address is the user's own email-change flow.
 */
public final class UserUpdateParams extends Params {
  private UserUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link UserUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link UserUpdateParams}. */
  public static final class Builder extends Params.Builder<UserUpdateParams, Builder> {
    private Builder() {}

    /**
     * The display name, or null. Null sends an explicit null, which clears it.
     *
     * @param displayName the value
     * @return this builder
     */
    public Builder displayName(@Nullable String displayName) {
      return set("display_name", displayName);
    }

    /**
     * A small {@code data:image/…} URL; an empty string clears it.
     *
     * @param avatarUrl the value
     * @return this builder
     */
    public Builder avatarUrl(String avatarUrl) {
      return set("avatar_url", nonNull(avatarUrl, "avatarUrl"));
    }

    /**
     * The phone. Null sends an explicit null, which clears it.
     *
     * @param phone the value
     * @return this builder
     */
    public Builder phone(@Nullable String phone) {
      return set("phone", phone);
    }

    /**
     * One of {@code "active"}, {@code "suspended"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    /**
     * Free-form data.
     *
     * @param metadata the value
     * @return this builder
     */
    public Builder metadata(Map<String, ?> metadata) {
      return set("metadata", nonNull(metadata, "metadata"));
    }

    @Override
    public UserUpdateParams build() {
      return new UserUpdateParams(this);
    }
  }
}
