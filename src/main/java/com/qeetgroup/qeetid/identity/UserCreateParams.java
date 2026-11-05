package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import java.util.Map;

/** A new user in the API key's organisation. */
public final class UserCreateParams extends Params {
  private UserCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link UserCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link UserCreateParams}. */
  public static final class Builder extends Params.Builder<UserCreateParams, Builder> {
    private Builder() {}

    /**
     * The organisation; must be the API key's own. Required.
     *
     * @param tenantId the value
     * @return this builder
     */
    public Builder tenantId(String tenantId) {
      return set("tenant_id", nonNull(tenantId, "tenantId"));
    }

    /**
     * The email address. Required.
     *
     * @param email the value
     * @return this builder
     */
    public Builder email(String email) {
      return set("email", nonNull(email, "email"));
    }

    /**
     * An initial password. It must meet the organisation's password rules.
     *
     * @param password the value
     * @return this builder
     */
    public Builder password(String password) {
      return setSecret("password", nonNull(password, "password"));
    }

    /**
     * The display name, or null.
     *
     * @param displayName the value
     * @return this builder
     */
    public Builder displayName(String displayName) {
      return set("display_name", nonNull(displayName, "displayName"));
    }

    /**
     * E.164, for example {@code +14155550100}.
     *
     * @param phone the value
     * @return this builder
     */
    public Builder phone(String phone) {
      return set("phone", nonNull(phone, "phone"));
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
    public UserCreateParams build() {
      require("tenant_id", "tenantId");
      require("email", "email");
      return new UserCreateParams(this);
    }
  }
}
