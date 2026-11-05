package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;

/** One user to create in a bulk create. */
public final class BulkUserRow extends Params {
  private BulkUserRow(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link BulkUserRow}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link BulkUserRow}. */
  public static final class Builder extends Params.Builder<BulkUserRow, Builder> {
    private Builder() {}

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
     * The password. Never printed.
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
     * The phone.
     *
     * @param phone the value
     * @return this builder
     */
    public Builder phone(String phone) {
      return set("phone", nonNull(phone, "phone"));
    }

    @Override
    public BulkUserRow build() {
      require("email", "email");
      return new BulkUserRow(this);
    }
  }
}
