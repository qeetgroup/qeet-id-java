# ADR-0004: Retry only what can't happen twice

**Status:** Accepted

## Context

Retrying a request whose first attempt may have reached the server can perform its action twice. The API honours an
`Idempotency-Key` on nine create calls (`IdempotentRoutes()` in the server's router): a repeated request with the same
key and body gets the first answer instead of acting again, for 24 hours.

## Decision

A request is **retry-safe** when it is a `GET` or `DELETE`, a POST that changes nothing (checks, evaluations, previews,
introspection, revocation; marked `retrySafe()`), or it carries an `Idempotency-Key`. Only retry-safe requests are
retried, and only after no response, a timeout, `408`, `429`, `500`, `502`, `503`, `504`, or `409
idempotency.in_progress`.

- `PUT`, `PATCH` and other POSTs are never retried, nor are OAuth grants (single-use) and directory sign-ins (each
  attempt counts toward the lockout).
- `401 auth.session_revoked` is never retried, even though the server marks it `retryable` (meaning "sign in again").
- The nine idempotent creates always send an `Idempotency-Key`: a UUID generated per call and kept across its retries,
  or the caller's own. Any other call refuses a key.
- Backoff: `Retry-After` when sent and at most 60 s (otherwise the error is thrown), else 0.5 s doubling to 8 s with
  equal jitter. Default two retries. An interrupted thread stops at once.

## Why

The same rule as the Node and Go SDKs, so the three behave alike. RFC 9110 §9.2.2 defines which methods are idempotent;
Stripe's SDKs generate idempotency keys for the same reason.
