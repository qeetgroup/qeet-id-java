# ADR-0005: One unchecked exception hierarchy

**Status:** Accepted

## Decision

Every method throws on failure; none returns an error value. Exceptions the SDK makes extend `QeetIDException`, a
`RuntimeException` with a stable `code()`:

- `ApiException` — the API answered with an error, with `status()`, `requestId()`, `detail()`, `fields()`,
  `metadata()`, `retryable()`, `retryAfter()` and `headers()`. Subclasses by status: `BadRequestException` (400),
  `AuthenticationException` (401) and its `SessionRevokedException`, `PaymentRequiredException` (402),
  `PermissionDeniedException` (403), `NotFoundException` (404), `ConflictException` (409), `ValidationException` (422),
  `RateLimitException` (429), `InternalServerException` (5xx) and its `ServiceUnavailableException` (503), and
  `OAuthException` for RFC 6749 answers.
- `NetworkException` and its `TimeoutException` — no response; code `interrupted` when the thread was interrupted.
- `WebhookVerificationException`, `TokenVerificationException` — local verification failed, with a `reason()` enum.

Argument mistakes are `IllegalArgumentException`, `IllegalStateException` or `NullPointerException`.

## Why

Azure's guidelines and the AWS SDK use unchecked exceptions: a checked one on every call forces boilerplate on callers
who can only rethrow it. Classes per status let callers handle the common cases without reading numbers; `code()`
covers the rest without parsing messages. The hierarchy mirrors the Node SDK's, adding 402, which the server uses for
plan limits.
