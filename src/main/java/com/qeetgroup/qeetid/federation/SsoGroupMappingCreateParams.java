package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** A new mapping from an identity provider's group to one of the organisation's groups. */
public final class SsoGroupMappingCreateParams extends Params {
  private SsoGroupMappingCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SsoGroupMappingCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SsoGroupMappingCreateParams}. */
  public static final class Builder extends Params.Builder<SsoGroupMappingCreateParams, Builder> {
    private Builder() {}

    /**
     * The idp group. Required.
     *
     * @param idpGroup the value
     * @return this builder
     */
    public Builder idpGroup(String idpGroup) {
      return set("idp_group", nonNull(idpGroup, "idpGroup"));
    }

    /**
     * The group's id. Required.
     *
     * @param groupId the value
     * @return this builder
     */
    public Builder groupId(String groupId) {
      return set("group_id", nonNull(groupId, "groupId"));
    }

    @Override
    public SsoGroupMappingCreateParams build() {
      require("idp_group", "idpGroup");
      require("group_id", "groupId");
      return new SsoGroupMappingCreateParams(this);
    }
  }
}
