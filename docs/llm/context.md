# Repository Context — qeet-id-java

**Level:** L2 · **Last verified:** 2026-11-05
**Verification scope:** client, transport, errors, webhooks and tokens read from source; every resource route checked
against `qeet-id-server`'s OpenAPI documents (`security` lists `apiKeyAuth`) and the idempotent routes against the
server's `IdempotentRoutes()`.

## Identity

`com.qeetgroup:qeet-id`, module `com.qeetgroup.qeetid` — the server-side Java SDK for Qeet ID. Java 17 bytecode,
compiled by JDK 25, tested on 17, 21 and 25. Gradle with the Kotlin DSL. One runtime dependency, Jackson 2.

## Architecture

```text
caller
  ↓  QeetID.builder().apiKey(…).build()
QeetID                          accessors: qeetId.users(), qeetId.webhooks().deliveries(), …
  ↓
<area>/<Resource>.java          one class per resource; builds an ApiRequest, Paths.of(…) for the path
  ↓
internal/Transport.java         headers → body → sendAsync bounded by the timeout → typed error → retry if safe
  ↓
api.id.qeet.in
```

Standalone, needing no client: `webhook/WebhookVerifier`, `jwt/TokenVerifier`, `jwt/Discovery`.

## Transport

| Property | Value |
| --- | --- |
| Base URL | `https://api.id.qeet.in`; https required except for loopback |
| Auth | `Authorization: ApiKey <key>`, or `Bearer <token>` from the client-credentials grant, cached and replaced 60 s (or half its life) before expiry, forgotten after a 401. Not sent to the OAuth, directory sign-in, agent token and credential verification endpoints |
| OAuth client auth | `client_secret_post` |
| Timeout | 30 s per attempt, response body included (`sendAsync` + `get(timeout)`) |
| Retries | 2, for GET, DELETE, `retrySafe()` POSTs and idempotency-keyed requests; after no response, timeout, 408, 429, 500, 502, 503, 504, 409 `idempotency.in_progress`; never `auth.session_revoked` |
| Backoff | 500 ms doubling to 8 s, equal jitter; `Retry-After` honoured up to 60 s |
| Idempotency-Key | a UUID per call on the nine idempotent routes; the caller's own through `RequestOptions` |
| User-Agent | `[app/version ]qeet-id-java/<version> java/<version> (<os>; <arch>)` |
| Logging | `System.Logger` `com.qeetgroup.qeetid`, DEBUG: method, path, attempt, status, duration, request ID |

## Models

Answers are records (`@JsonProperty` on multi-word fields, `Instant` times, non-null collections, redacting
`toString` where a secret is held); requests are `Params` builders backed by an ordered map, sending only what was set.
`Page<T>` iterates and streams every item across pages; `ListResult<T>` is a whole list with `truncated`.

## Cryptography

`webhook/WebhookVerifier` — HMAC-SHA256 over `timestamp + "." + body`, every signature value against every secret,
`MessageDigest.isEqual`, 5-minute tolerance. `jwt/TokenVerifier` — ES256 via `SHA256withECDSAinP1363Format`; issuer and
audience required; JWKS cached 5 minutes, unknown-kid refetch at most every 30 s, stale keys kept through an outage;
30 s clock skew.

## Testing

JUnit Jupiter against `testing/MockServer` (the JDK's `HttpServer`). `ResourceRequestsTest` has a row per resource
method; `ModelsTest` round-trips every record and builder; webhook fixtures were made by the server's own `deliver` and
`sign`; token tests sign with real P-256 keys.

## Known constraints

- What the API offers beyond the Node SDK's coverage (ABAC, directory sync, OIDC enterprise connections, compliance,
  email delivery) is not wrapped yet.
- Maven Central needs a licence in the POM before the first release.
