package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/** The changes to a directory connection: only the fields set are sent. */
public final class LdapConnectionUpdateParams extends Params {
  private LdapConnectionUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link LdapConnectionUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link LdapConnectionUpdateParams}. */
  public static final class Builder extends Params.Builder<LdapConnectionUpdateParams, Builder> {
    private Builder() {}

    /**
     * Its name.
     *
     * @param name the value
     * @return this builder
     */
    public Builder name(String name) {
      return set("name", nonNull(name, "name"));
    }

    /**
     * The server URL.
     *
     * @param serverUrl the value
     * @return this builder
     */
    public Builder serverUrl(String serverUrl) {
      return set("server_url", nonNull(serverUrl, "serverUrl"));
    }

    /**
     * The bind dn.
     *
     * @param bindDn the value
     * @return this builder
     */
    public Builder bindDn(String bindDn) {
      return set("bind_dn", nonNull(bindDn, "bindDn"));
    }

    /**
     * The bind password.
     *
     * @param bindPassword the value
     * @return this builder
     */
    public Builder bindPassword(String bindPassword) {
      return setSecret("bind_password", nonNull(bindPassword, "bindPassword"));
    }

    /**
     * The base dn.
     *
     * @param baseDn the value
     * @return this builder
     */
    public Builder baseDn(String baseDn) {
      return set("base_dn", nonNull(baseDn, "baseDn"));
    }

    /**
     * The start tls.
     *
     * @param startTls the value
     * @return this builder
     */
    public Builder startTls(boolean startTls) {
      return set("start_tls", startTls);
    }

    /**
     * Sign-in refuses a connection that skips certificate verification.
     *
     * @param skipTlsVerify the value
     * @return this builder
     */
    public Builder skipTlsVerify(boolean skipTlsVerify) {
      return set("skip_tls_verify", skipTlsVerify);
    }

    /**
     * The user filter.
     *
     * @param userFilter the value
     * @return this builder
     */
    public Builder userFilter(String userFilter) {
      return set("user_filter", nonNull(userFilter, "userFilter"));
    }

    /**
     * The email attribute.
     *
     * @param emailAttribute the value
     * @return this builder
     */
    public Builder emailAttribute(String emailAttribute) {
      return set("email_attribute", nonNull(emailAttribute, "emailAttribute"));
    }

    /**
     * The name attribute.
     *
     * @param nameAttribute the value
     * @return this builder
     */
    public Builder nameAttribute(String nameAttribute) {
      return set("name_attribute", nonNull(nameAttribute, "nameAttribute"));
    }

    /**
     * One of {@code "draft"}, {@code "active"}, {@code "disabled"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    @Override
    public LdapConnectionUpdateParams build() {
      return new LdapConnectionUpdateParams(this);
    }
  }
}
