# Changelog

All notable changes to the Qeet ID Java SDK are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.2.0] — 2026-11-21

Follows the API's rename of tenant to organization, and its single OAuth token endpoint. The API accepts no old names
alongside the new ones, so 0.1.0 can't call it: upgrade, then rename in your code as below.

### Changed (breaking)

- **Organization, not tenant, everywhere.** `qeetId.tenants()` is `qeetId.organizations()`, and the classes `Tenants`,
  `Tenant`, `TenantUpdateParams`, `TenantDeletion`, `TenantBranding` and `TenantRateLimits` are `Organizations`,
  `Organization`, `OrganizationUpdateParams`, `OrganizationDeletion`, `OrganizationBranding` and
  `OrganizationRateLimits`. Every path under `/v1/tenants/{id}/…` is `/v1/organizations/{id}/…`, and
  `/v1/users/{id}/tenants/{id}/…` is `/v1/users/{id}/organizations/{id}/…`. Every `tenantId` parameter, builder method
  and record component is `organizationId` (`organization_id` on the wire); `User.homeTenantName` is
  `homeOrganizationName`; the rate limits' `tenant` bucket is `organization` (`RateLimitsUpdateParams.Builder.organization`).
  A decision's `decidedBy().model()` and an email template's `deliveryScope()` say `organization`.
- **Access tokens and introspection:** `AccessTokenClaims.organizationId()` and `Introspection.organizationId()` read the
  `organization_id` claim, in place of `tenantId()` and `tenant_id`.
- **Webhooks:** `WebhookEvent.organizationId()` replaces `tenantId()`, and the verifier requires `organization_id` in the
  event. `WebhookEventTypes.TENANT_SUSPENDED`, `TENANT_REACTIVATED` and `TENANT_DELETED` are `ORGANIZATION_SUSPENDED`,
  `ORGANIZATION_REACTIVATED` and `ORGANIZATION_DELETED` (`organization.suspended`, …).
- **One token endpoint.** The authorization-code, refresh, token-exchange, device and CIBA grants post to
  `POST /v1/oauth/token`, with `grant_type` choosing the grant; `/v1/oauth/token-code` is gone.
- **Agent tokens** (`agents().createToken`) are the client-credentials grant at `POST /v1/oauth/token`, with the agent's
  id as `client_id` and its secret as `client_secret`, in place of a JSON body to `/v1/agents/token`. A refusal is an
  `OAuthException` (`invalid_client`, or `unauthorized_client` for an agent that isn't active); like every grant, it is
  never retried. `AgentToken` gains `expiresAt`.
- `OrganizationUpdateParams` has `logoUrl` and no longer `plan`, which the API refuses there; `Organization` gains
  `logoUrl`, and its plan may be `starter`.
- `GroupCreateParams` no longer has `organizationId`: the API takes a new group's organization from the credential and
  documents no such field.

### Changed

- The webhook fixtures were captured again from the server's own delivery and request signing code, with
  `organization_id` in every event.
- Javadoc and documents spell organization as the API does, and name the server's documents `identity.yaml` and
  `access.yaml` (formerly `management.yaml` and `auth.yaml`).

## [0.1.0] — 2026-11-05

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

[Unreleased]: https://github.com/qeetgroup/qeet-id-java/compare/v0.2.0...HEAD
[0.2.0]: https://github.com/qeetgroup/qeet-id-java/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/qeetgroup/qeet-id-java/releases/tag/v0.1.0
