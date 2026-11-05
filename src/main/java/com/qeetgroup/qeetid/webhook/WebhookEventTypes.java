package com.qeetgroup.qeetid.webhook;

import java.util.List;

/**
 * The events a webhook subscription can receive, as the API's event catalogue lists them ({@code
 * webhooks().listEventTypes()} reads it live). An event's name is its version: a payload that
 * changes incompatibly gets a new name, while adding a field to one does not. A delivery may carry
 * an event newer than this SDK; handle unknown types gracefully.
 */
public final class WebhookEventTypes {
  /** A person was added to the organisation, by an administrator or over SCIM. */
  public static final String USER_CREATED = "user.created";

  /** SCIM changed a person's profile or status. */
  public static final String USER_UPDATED = "user.updated";

  /** A person's account was suspended. */
  public static final String USER_SUSPENDED = "user.suspended";

  /** A suspended account was reactivated. */
  public static final String USER_REACTIVATED = "user.reactivated";

  /** A person was deleted from the organisation. */
  public static final String USER_DELETED = "user.deleted";

  /** An administrator set a person's password, ending their sessions. */
  public static final String USER_PASSWORD_SET = "user.password_set";

  /** A bulk import of people finished, with how many rows succeeded and failed. */
  public static final String USER_BULK_IMPORTED = "user.bulk_imported";

  /** A person changed their own password. */
  public static final String AUTH_PASSWORD_CHANGED = "auth.password_changed";

  /** A session ended, with the reason; its tokens stop working. */
  public static final String SESSION_REVOKED = "session.revoked";

  /** A person's roles changed, so their next access token carries different claims. */
  public static final String TOKEN_CLAIMS_CHANGE = "token.claims_change";

  /** A group was created. */
  public static final String GROUP_CREATED = "group.created";

  /** A group was changed. */
  public static final String GROUP_UPDATED = "group.updated";

  /** A group was deleted. */
  public static final String GROUP_DELETED = "group.deleted";

  /** A person was added to a group. */
  public static final String GROUP_MEMBER_ADDED = "group.member_added";

  /** A person was removed from a group. */
  public static final String GROUP_MEMBER_REMOVED = "group.member_removed";

  /** A group template was applied, creating its groups. */
  public static final String GROUP_TEMPLATE_APPLIED = "group_template.applied";

  /** A domain was added for verification. */
  public static final String DOMAIN_CREATED = "domain.created";

  /** A domain was verified. */
  public static final String DOMAIN_VERIFIED = "domain.verified";

  /** A domain was removed. */
  public static final String DOMAIN_DELETED = "domain.deleted";

  /** An AI agent was registered. */
  public static final String AGENT_CREATED = "agent.created";

  /** An AI agent was suspended; its tokens stop working. */
  public static final String AGENT_SUSPENDED = "agent.suspended";

  /** A suspended AI agent was made active again. */
  public static final String AGENT_RESUMED = "agent.resumed";

  /** An AI agent was decommissioned for good. */
  public static final String AGENT_DECOMMISSIONED = "agent.decommissioned";

  /** An AI agent was deleted. */
  public static final String AGENT_DELETED = "agent.deleted";

  /** A person's AI agents were given to another sponsor. */
  public static final String AGENT_SPONSOR_TRANSFERRED = "agent.sponsor_transferred";

  /** Every active AI agent of the organisation was suspended at once. */
  public static final String AGENT_KILL_SWITCH = "agent.kill_switch";

  /** Every suspended AI agent of the organisation was made active again at once. */
  public static final String AGENT_RESUME_ALL = "agent.resume_all";

  /** A relationship was written. */
  public static final String REBAC_TUPLE_WRITTEN = "rebac.tuple_written";

  /** A relationship was deleted. */
  public static final String REBAC_TUPLE_DELETED = "rebac.tuple_deleted";

  /** An attribute-based access policy was created. */
  public static final String ABAC_POLICY_CREATED = "abac_policy.created";

  /** An attribute-based access policy was changed. */
  public static final String ABAC_POLICY_UPDATED = "abac_policy.updated";

  /** An attribute-based access policy was deleted. */
  public static final String ABAC_POLICY_DELETED = "abac_policy.deleted";

  /** An authentication hook was created. */
  public static final String AUTH_HOOK_CREATED = "auth_hook.created";

  /** An authentication hook was enabled, disabled or changed. */
  public static final String AUTH_HOOK_UPDATED = "auth_hook.updated";

  /** An authentication hook was deleted. */
  public static final String AUTH_HOOK_DELETED = "auth_hook.deleted";

  /** A log stream to a SIEM was created. */
  public static final String SIEM_SINK_CREATED = "siem.sink_created";

  /** A log stream to a SIEM was enabled or disabled. */
  public static final String SIEM_SINK_UPDATED = "siem.sink_updated";

  /** A log stream to a SIEM was deleted. */
  public static final String SIEM_SINK_DELETED = "siem.sink_deleted";

  /** The organisation's rate limits were changed. */
  public static final String RATE_LIMITS_UPDATED = "rate_limits.updated";

  /** The organisation's rate limits were reset to the defaults. */
  public static final String RATE_LIMITS_DELETED = "rate_limits.deleted";

  /** A verifiable credential was issued. */
  public static final String CREDENTIAL_ISSUED = "credential.issued";

  /** A verifiable credential was revoked. */
  public static final String CREDENTIAL_REVOKED = "credential.revoked";

  /** An application was locked out after too many wrong secrets. */
  public static final String OIDC_CLIENT_LOCKED_OUT = "oidc.client_locked_out";

  /** The organisation was suspended, ending its sessions. */
  public static final String TENANT_SUSPENDED = "tenant.suspended";

  /** The organisation was reactivated. */
  public static final String TENANT_REACTIVATED = "tenant.reactivated";

  /** The organisation was deleted, ending its sessions. */
  public static final String TENANT_DELETED = "tenant.deleted";

  /** The event the test call sends. No subscription names it. */
  public static final String TEST_PING = "test.ping";

  /** Every event a subscription can name, in the catalogue's order. */
  public static final List<String> ALL =
      List.of(
          USER_CREATED,
          USER_UPDATED,
          USER_SUSPENDED,
          USER_REACTIVATED,
          USER_DELETED,
          USER_PASSWORD_SET,
          USER_BULK_IMPORTED,
          AUTH_PASSWORD_CHANGED,
          SESSION_REVOKED,
          TOKEN_CLAIMS_CHANGE,
          GROUP_CREATED,
          GROUP_UPDATED,
          GROUP_DELETED,
          GROUP_MEMBER_ADDED,
          GROUP_MEMBER_REMOVED,
          GROUP_TEMPLATE_APPLIED,
          DOMAIN_CREATED,
          DOMAIN_VERIFIED,
          DOMAIN_DELETED,
          AGENT_CREATED,
          AGENT_SUSPENDED,
          AGENT_RESUMED,
          AGENT_DECOMMISSIONED,
          AGENT_DELETED,
          AGENT_SPONSOR_TRANSFERRED,
          AGENT_KILL_SWITCH,
          AGENT_RESUME_ALL,
          REBAC_TUPLE_WRITTEN,
          REBAC_TUPLE_DELETED,
          ABAC_POLICY_CREATED,
          ABAC_POLICY_UPDATED,
          ABAC_POLICY_DELETED,
          AUTH_HOOK_CREATED,
          AUTH_HOOK_UPDATED,
          AUTH_HOOK_DELETED,
          SIEM_SINK_CREATED,
          SIEM_SINK_UPDATED,
          SIEM_SINK_DELETED,
          RATE_LIMITS_UPDATED,
          RATE_LIMITS_DELETED,
          CREDENTIAL_ISSUED,
          CREDENTIAL_REVOKED,
          OIDC_CLIENT_LOCKED_OUT,
          TENANT_SUSPENDED,
          TENANT_REACTIVATED,
          TENANT_DELETED);

  private WebhookEventTypes() {}
}
