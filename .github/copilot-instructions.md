# GitHub Copilot — qeet-id-java

**Canonical instructions: [`AGENTS.md`](../AGENTS.md).** This file is a summary; it adds no architecture.

## Repository

The server-side **Java SDK for Qeet ID** — `com.qeetgroup:qeet-id`, module `com.qeetgroup.qeetid`. Java 17 bytecode
compiled by JDK 25, Gradle, **Jackson 2 as the only runtime dependency**. It wraps a contract owned by
`qeet-id-server`.

## Structure

`QeetID.java` (the client and its builder) · `internal/` (HTTP, JSON, retries, credentials; not exported) ·
`exception/` (the hierarchy) · `{identity,access,federation,developer,operations}/` (one package per OpenAPI document:
resources, answer records, request builders) · `webhook/` (delivery verification) · `jwt/` (access-token verification
and discovery) · `module-info.java` (every export).

## Rules

1. **Jackson is the only runtime dependency**, never in a public signature.
2. **Java 17 APIs only.**
3. **Records for answers, builders for requests**; `@JsonProperty` on multi-word wire fields.
4. **Only routes an API key may call.**
5. **Retries never repeat a write:** GET, DELETE, `retrySafe()` POSTs and idempotency-keyed requests only.
6. **Never weaken** webhook or token verification.

## Commands

`./gradlew build` · `./gradlew test -PtestJdk=21` · `./gradlew javadoc` · `./gradlew spotlessApply`

## Do not

- suggest adding a runtime dependency, or exposing a Jackson type
- use an API newer than Java 17
- return null for a collection — return an empty one
- make PUT, PATCH, a non-idempotent POST, an OAuth grant or directory sign-in retryable
- log, embed or print an API key, client secret or token — including in `toString` or an exception
- invent Gradle tasks — read `build.gradle.kts`
