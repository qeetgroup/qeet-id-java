package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import java.util.List;

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
     * A role name in the organization.
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
     * Whether the user has a confirmed second factor. One of {@code "on"}, {@code "off"}.
     *
     * @param mfa the value
     * @return this builder
     */
    public Builder mfa(String mfa) {
      return set("mfa", nonNull(mfa, "mfa"));
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

    /**
     * True: only the owners and admins with no second factor; false: everyone else.
     *
     * @param adminsWithoutMfa the value
     * @return this builder
     */
    public Builder adminsWithoutMfa(boolean adminsWithoutMfa) {
      return set("admins_without_mfa", adminsWithoutMfa);
    }

    /**
     * True: only the members in one of the organization's groups; false: only those in none.
     *
     * @param inGroup the value
     * @return this builder
     */
    public Builder inGroup(boolean inGroup) {
      return set("in_group", inGroup);
    }

    /**
     * Only these members, by id, up to 200.
     *
     * @param ids the value
     * @return this builder
     */
    public Builder ids(List<String> ids) {
      return set("id", listOf(ids));
    }

    /**
     * Only these members, by email, exactly but in any case, up to 200.
     *
     * @param emails the value
     * @return this builder
     */
    public Builder emails(List<String> emails) {
      return set("email", listOf(emails));
    }

    /**
     * Adds the members since deleted, each with {@code deletedAt} and status {@code "deleted"}.
     *
     * @param includeDeleted the value
     * @return this builder
     */
    public Builder includeDeleted(boolean includeDeleted) {
      return set("include_deleted", includeDeleted);
    }

    /**
     * The order. One of {@code "created"}, {@code "name"}, {@code "last_seen"}; {@code "created"}
     * by default. A cursor works only with the sort and order of the page that gave it.
     *
     * @param sort the value
     * @return this builder
     */
    public Builder sort(String sort) {
      return set("sort", nonNull(sort, "sort"));
    }

    /**
     * One of {@code "asc"}, {@code "desc"}; newest or last seen first by default, and A to Z by
     * name.
     *
     * @param order the value
     * @return this builder
     */
    public Builder order(String order) {
      return set("order", nonNull(order, "order"));
    }

    @Override
    public UserListParams build() {
      return new UserListParams(this);
    }
  }
}
