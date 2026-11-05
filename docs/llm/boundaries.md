# Boundaries — qeet-id-java

**Level:** L2 · **Last verified:** 2026-11-05

## Owns

Typed Java bindings for the Qeet ID API calls an API key may make · HTTP execution with timeouts and safe retries ·
cursor pagination · the exception hierarchy · webhook delivery verification · access-token verification · the OAuth
grants · the jar, its POM and its module descriptor.

## Does not own

| Not owned | Owner |
| --- | --- |
| **The API contract** | `qeet-id-server` — `api/openapi/*.yaml`, handlers, route policy |
| Authorization, tenancy, token issuance | `qeet-id-server` |
| A person's own account (sessions, factors, passkeys, email change) | the hosted pages and `qeet-id-react` |
| The Node.js and Go stories | `qeet-id-node`, `qeet-id-go` |
| Documentation site | `qeet-id-docs` |

**The SDK never adds behaviour the API lacks.** A missing endpoint is server work.

## Consumes

`api.id.qeet.in` over HTTPS: `ApiKey` or a client-credentials `Bearer` token for management calls; OAuth client
credentials in the form body for the token, introspection, revocation, device and backchannel endpoints; the JWKS and
discovery documents. Nothing else — no database, no filesystem.

## Provides

A Maven artifact whose **exported packages in `module-info.java`** are its contract. Once published, every public type
and method in them is a semver promise; `internal` is not exported and may change at any time.

## Security boundaries

| Boundary | Enforcement |
| --- | --- |
| Credential → wire | `internal/Transport.java`, `internal/Credential.java` — never logged, never in an exception or `toString`; https only |
| Token → claims | `jwt/TokenVerifier.java` — ES256, known kid, issuer and audience required |
| Webhook body → event | `webhook/WebhookVerifier.java` — HMAC over timestamp and body, constant time, replay window |
| Retry → side effects | `internal/RetryPolicy.java`, `internal/Transport.java` — writes retried only with an idempotency key |
| Identifier → path | `internal/Paths.java` — percent-encoded as one segment; blank and dot segments refused |

**This SDK is not a trust boundary.** It runs in the caller's process with the caller's credentials.

## Hard limits

1. **Never add a runtime dependency, or put a Jackson type in a public signature.**
2. **Never retry a write without an idempotency key**, an OAuth grant, a directory sign-in, or `auth.session_revoked`.
3. **Never return a null collection** — return an empty one.
4. **Never log or print a credential**, including in `toString` or an exception.
5. **Never skip signature, timestamp, issuer, audience or kid checks**, even in tests.
6. **Never wrap a route an API key can't call.**
7. **Never change another repository** from a task scoped to this one.
