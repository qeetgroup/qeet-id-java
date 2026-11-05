package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;

/** Filters for {@code list}. They combine. */
public final class UserListParams extends Params {
  private UserListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link UserListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link UserListParams}. */
  public static final class Builder extends Params.Builder<UserListParams, Builder> {
    private Builder() {}

    /**
     * How many items per page. Each list documents its own default and maximum.
     *
     * @param limit the value
     * @return this builder
     */
    public Builder limit(int limit) {
      return set("limit", limit);
    }

    /**
     * Where to start: a page's {@code nextCursor}. Omit for the first page.
     *
     * @param cursor the value
     * @return this builder
     */
    public Builder cursor(String cursor) {
      return set("cursor", nonNull(cursor, "cursor"));
    }

    /**
     * Matches the display name, email or phone, case-insensitively.
     *
     * @param q the value
     * @return this builder
     */
    public Builder q(String q) {
      return set("q", nonNull(q, "q"));
    }

    /**
     * A role name in the organisation.
     *
     * @param role the value
     * @return this builder
     */
    public Builder role(String role) {
      return set("role", nonNull(role, "role"));
    }

    /**
     * Matches {@code metadata.department}.
     *
     * @param department the value
     * @return this builder
     */
    public Builder department(String department) {
      return set("department", nonNull(department, "department"));
    }

    /**
     * Whether the user has a confirmed second factor.
     *
     * @param mfa the value
     * @return this builder
     */
    public Builder mfa(boolean mfa) {
      return set("mfa", mfa);
    }

    /**
     * The email verified.
     *
     * @param emailVerified the value
     * @return this builder
     */
    public Builder emailVerified(boolean emailVerified) {
      return set("email_verified", emailVerified);
    }

    /**
     * One of {@code "active"}, {@code "invited"}, {@code "suspended"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    /**
     * One of {@code "today"}, {@code "week"}, {@code "month"}, {@code "older"}, {@code "never"}.
     *
     * @param lastSeen the value
     * @return this builder
     */
    public Builder lastSeen(String lastSeen) {
      return set("last_seen", nonNull(lastSeen, "lastSeen"));
    }

    @Override
    public UserListParams build() {
      return new UserListParams(this);
    }
  }
}
