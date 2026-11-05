package com.qeetgroup.qeetid.federation;

import com.qeetgroup.qeetid.Params;

/**
 * {@code server_url} starts {@code ldap://} or {@code ldaps://}. Unset optional fields get the
 * server's defaults ({@code user_filter} {@code (uid=%s)}, {@code email_attribute} {@code mail},
 * {@code name_attribute} {@code cn}, {@code status} {@code draft}).
 */
public final class LdapConnectionCreateParams extends Params {
  private LdapConnectionCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link LdapConnectionCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link LdapConnectionCreateParams}. */
  public static final class Builder extends Params.Builder<LdapConnectionCreateParams, Builder> {
    private Builder() {}

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
     * The server URL. Required.
     *
     * @param serverUrl the value
     * @return this builder
     */
    public Builder serverUrl(String serverUrl) {
      return set("server_url", nonNull(serverUrl, "serverUrl"));
    }

    /**
     * The bind dn. Required.
     *
     * @param bindDn the value
     * @return this builder
     */
    public Builder bindDn(String bindDn) {
      return set("bind_dn", nonNull(bindDn, "bindDn"));
    }

    /**
     * The bind password. Required.
     *
     * @param bindPassword the value
     * @return this builder
     */
    public Builder bindPassword(String bindPassword) {
      return setSecret("bind_password", nonNull(bindPassword, "bindPassword"));
    }

    /**
     * The base dn. Required.
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
    public LdapConnectionCreateParams build() {
      require("name", "name");
      require("server_url", "serverUrl");
      require("bind_dn", "bindDn");
      require("bind_password", "bindPassword");
      require("base_dn", "baseDn");
      return new LdapConnectionCreateParams(this);
    }
  }
}
