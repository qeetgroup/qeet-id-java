# ADR-0003: Records for answers, builders for requests

**Status:** Accepted

## Decision

- **Answers are records.** Each component is an API field, camel-cased, with `@JsonProperty` naming the wire field.
  Times are `Instant`; a field that may be absent is `@Nullable` and boxed; a list or map is never null (empty when
  absent). Values the API may extend (statuses, kinds) are `String`, with the known values in the Javadoc. A record
  holding a secret overrides `toString` to redact it.
- **Requests are builders.** `UserCreateParams.builder().tenantId(…).email(…).build()`. A builder sends only the fields
  set; setting a nullable field to `null` sends an explicit `null`, which clears it on an update. Required fields are
  checked in `build()`. Secret fields are set with `setSecret` and never printed.
- A few small value types used on both sides (`RateLimitBucket`, `SamlAttributeMapping`, `AuthzenEntity`,
  `EmailTemplateContent` and their like) are records the caller constructs.
- Identifiers come first in a signature, then the parameters, then `RequestOptions`, in an overload.

## Why

Records give immutability, equality and component names for Kotlin and Scala for free. A record's constructor would be a
poor request type: adding an optional field breaks every caller, and it can't tell "not set" from "set to null", which a
PATCH needs. A map-backed builder can.

## Consequences

Adding an answer field is additive for readers but changes the record's constructor; callers are not expected to
construct answer records except in tests.
