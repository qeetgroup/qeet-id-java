# Changelog

All notable changes to the Qeet ID Java SDK are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- **`QeetID`**, an immutable, thread-safe client built with a builder: an API key, or a service principal's client
  credentials (the token is cached and replaced before it expires); an https base URL (http only for loopback);
  timeouts, retries and backoff; default headers; `appInfo`; a `System.Logger`; your own `HttpClient`.
- **38 resource groups and 270 API calls** across identity, access, federation, developer and operations, each checked
  against the server's OpenAPI documents. Answers are records; requests are builders that send only the fields set,
  with an explicit `null` clearing a field. Every call has an overload taking `RequestOptions` (timeout, retries,
  headers, idempotency key).
- **Typed exceptions**: `QeetIDException` and `ApiException`, with `BadRequestException`, `AuthenticationException`,
  `SessionRevokedException`, `PaymentRequiredException`, `PermissionDeniedException`, `NotFoundException`,
  `ConflictException`, `ValidationException`, `RateLimitException`, `InternalServerException`,
  `ServiceUnavailableException`, `OAuthException`, `NetworkException` and `TimeoutException`, each carrying the code,
  message, status, request ID and `Retry-After`.
- **Retries that can't do anything twice**: exponential backoff with jitter, honouring `Retry-After` up to 60 s, for
  `GET`, `DELETE`, read-only POSTs and requests carrying an `Idempotency-Key`; never `401 auth.session_revoked`.
  Idempotency keys are generated on the nine calls the API makes idempotent.
- **Pagination**: `Page` iterates and streams every item across pages; `ListResult` reports a list the API capped.
- **Webhook verification**: `WebhookVerifier`, `WebhookEvent`, `WebhookEventTypes` and `WebhookHeaders`; several
  secrets during a rotation, a five-minute replay window, and `sign` for tests.
- **Access-token verification**: `TokenVerifier`, `AccessTokenClaims` and `Discovery`, with ES256 only, a required
  issuer and audience, and a cached, rate-limited key set, using the JDK's own cryptography.
- **OAuth**: the client-credentials, authorization-code, refresh, token-exchange, device-code and CIBA grants, device
  and backchannel authorization, introspection, revocation, `TokenGuard`, consent grants, device authorizations and
  signing keys.
- A named module, `com.qeetgroup.qeetid`, with JSpecify nullness annotations throughout.
