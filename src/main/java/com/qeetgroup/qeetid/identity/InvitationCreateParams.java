package com.qeetgroup.qeetid.identity;

import com.qeetgroup.qeetid.Params;
import org.jspecify.annotations.Nullable;

/** A new invitation. */
public final class InvitationCreateParams extends Params {
  private InvitationCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link InvitationCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link InvitationCreateParams}. */
  public static final class Builder extends Params.Builder<InvitationCreateParams, Builder> {
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
     * The email address. Required.
     *
     * @param email the value
     * @return this builder
     */
    public Builder email(String email) {
      return set("email", nonNull(email, "email"));
    }

    /**
     * The role's id. Null sends an explicit null, which clears it.
     *
     * @param roleId the value
     * @return this builder
     */
    public Builder roleId(@Nullable String roleId) {
      return set("role_id", roleId);
    }

    @Override
    public InvitationCreateParams build() {
      require("tenant_id", "tenantId");
      require("email", "email");
      return new InvitationCreateParams(this);
    }
  }
}
