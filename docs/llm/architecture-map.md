# Architecture Map — qeet-id-java

**Level:** L2 · **Last verified:** 2026-11-05
**Verification scope:** every path below was confirmed to exist. Paths are under `src/main/java/com/qeetgroup/qeetid/`
unless they start with `src/`, `docs/` or `.github/`.

| Need | Path |
| --- | --- |
| Repository identity | [`qeet-repo.yml`](../../qeet-repo.yml) |
| Agent instructions | [`AGENTS.md`](../../AGENTS.md) |
| **Exported packages — the artifact's contract** | `src/main/java/module-info.java` |
| The client `QeetID` and its builder | `QeetID.java` |
| Per-call settings | `RequestOptions.java` |
| Request parameters base, paging | `Params.java`, `PageParams.java`, `Page.java`, `ListResult.java` |
| Build, coordinates, publishing | `build.gradle.kts`, `gradle/libs.versions.toml`, `gradle.lockfile` |

## Core (`internal/`, not exported)

| Concern | Path |
| --- | --- |
| **Request execution: credentials, headers, body, timeouts, retries, logging** | `internal/Transport.java` |
| API key and client-credentials token cache | `internal/Credential.java` |
| Retry policy and backoff | `internal/RetryPolicy.java` |
| **Error answer → exception** | `internal/ErrorMapper.java` |
| JSON mapper, `Instant` handling | `internal/Json.java` |
| Request description, path encoding, small bodies | `internal/ApiRequest.java`, `internal/Paths.java`, `internal/Bodies.java` |
| Header and idempotency-key checks, User-Agent, version | `internal/Headers.java`, `internal/UserAgent.java`, `internal/Version.java` |

## Security-critical

| Concern | Path |
| --- | --- |
| **Webhook verification** | `webhook/WebhookVerifier.java` |
| Webhook events and catalogue | `webhook/WebhookEvent.java`, `webhook/WebhookEventTypes.java` |
| **Access-token verification (JWKS, ES256)** | `jwt/TokenVerifier.java`, `jwt/AccessTokenClaims.java` |
| Discovery | `jwt/Discovery.java` |
| OAuth grants, introspection, `TokenGuard` | `federation/OAuth.java`, `federation/TokenGuard.java` |

## Resources — one package per OpenAPI document

```text
identity/     tenants · domains · users · groups · invitations · branding
access/       roles · permissions · relationships · decisions · auth policies · security policies · IP rules · threat detection
federation/   OAuth · applications · service principals · SAML · LDAP · SCIM · social providers · admin portal links
developer/    API keys · agents · verifiable credentials · auth hooks · webhooks · secrets · token vault
operations/   audit logs · analytics · billing · GDPR · email templates · retention · log sinks · rate limits
```

## Tests (`src/test/java/com/qeetgroup/qeetid/`)

| Concern | Path |
| --- | --- |
| In-process HTTP server, test clients | `testing/MockServer.java`, `testing/TestClients.java` |
| Every resource method's request | `ResourceRequestsTest.java` |
| Every record and builder's wire names | `ModelsTest.java` |
| Hand-written resource methods | `SpecialResourcesTest.java` |
| Transport, retries, errors, paging | `internal/*Test.java` |
| Webhooks (server-made fixtures) | `webhook/WebhookVerifierTest.java`, `src/test/resources/webhook/deliveries.json` |
| Tokens | `jwt/TokenVerifierTest.java` |
| OAuth | `federation/OAuthTest.java` |
