package com.qeetgroup.qeetid.federation;

/**
 * A directory sign-in's outcome: {@link Tokens}, or a second-factor {@link Challenge}.
 *
 * <pre>{@code
 * if (result instanceof LdapAuthenticationResult.Challenge challenge) { ... }
 * }</pre>
 */
public sealed interface LdapAuthenticationResult {
  /**
   * The person is signed in.
   *
   * @param tokens the session's tokens
   */
  record Tokens(TokenPair tokens) implements LdapAuthenticationResult {}

  /**
   * The sign-in stopped before its session: complete it at {@code POST /v1/auth/mfa} with the
   * challenge's {@code mfaToken}.
   *
   * @param challenge the second-factor challenge
   */
  record Challenge(MfaChallenge challenge) implements LdapAuthenticationResult {}
}
