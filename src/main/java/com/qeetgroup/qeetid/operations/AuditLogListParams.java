package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** Filters for {@code list}. They combine. */
public final class AuditLogListParams extends Params {
  private AuditLogListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AuditLogListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AuditLogListParams}. */
  public static final class Builder extends Params.Builder<AuditLogListParams, Builder> {
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
     * Free text over action, resource type, actor type, user agent and metadata; supports "quoted
     * phrases", -exclusions and OR.
     *
     * @param q the value
     * @return this builder
     */
    public Builder q(String q) {
      return set("q", nonNull(q, "q"));
    }

    /**
     * The action.
     *
     * @param action the value
     * @return this builder
     */
    public Builder action(String action) {
      return set("action", nonNull(action, "action"));
    }

    /**
     * The resource type.
     *
     * @param resourceType the value
     * @return this builder
     */
    public Builder resourceType(String resourceType) {
      return set("resource_type", nonNull(resourceType, "resourceType"));
    }

    /**
     * The resource's id.
     *
     * @param resourceId the value
     * @return this builder
     */
    public Builder resourceId(String resourceId) {
      return set("resource_id", nonNull(resourceId, "resourceId"));
    }

    /**
     * The actor user's id.
     *
     * @param actorUserId the value
     * @return this builder
     */
    public Builder actorUserId(String actorUserId) {
      return set("actor_user_id", nonNull(actorUserId, "actorUserId"));
    }

    /**
     * The IP.
     *
     * @param ip the value
     * @return this builder
     */
    public Builder ip(String ip) {
      return set("ip", nonNull(ip, "ip"));
    }

    /**
     * RFC 3339 times bounding {@code created_at}.
     *
     * @param from the value
     * @return this builder
     */
    public Builder from(String from) {
      return set("from", nonNull(from, "from"));
    }

    /**
     * The to.
     *
     * @param to the value
     * @return this builder
     */
    public Builder to(String to) {
      return set("to", nonNull(to, "to"));
    }

    @Override
    public AuditLogListParams build() {
      return new AuditLogListParams(this);
    }
  }
}
