# AGENTS.md — qeet-id-java

**The model-neutral instruction file for coding agents.** [CLAUDE.md](CLAUDE.md), [GEMINI.md](GEMINI.md) and
[.github/copilot-instructions.md](.github/copilot-instructions.md) point here and add nothing architectural.

## What this repository is

The **server-side Java SDK for Qeet ID**, Maven coordinates `com.qeetgroup:qeet-id`, module `com.qeetgroup.qeetid`.
Java 17 bytecode, compiled by JDK 25 and tested on 17, 21 and 25. Gradle (Kotlin DSL) through the wrapper.
**One runtime dependency: Jackson 2** (`jackson-databind`), kept out of the public API; JSpecify annotations at compile
time.

It is a **client of a contract it does not own.** The contract is `qeet-id-server`'s five OpenAPI documents
(`api/openapi/{auth,management,federation,developer,operations}.yaml`), with the server's handlers and route policy as
the tiebreaker. The SDK wraps them and never adds behaviour the API lacks. It covers what the Node SDK covers.

## Read before changing code

| File | For |
| --- | --- |
| [qeet-repo.yml](qeet-repo.yml) | Machine-readable identity |
| [docs/llm/architecture-map.md](docs/llm/architecture-map.md) | "Where is X?" |
| [docs/llm/context.md](docs/llm/context.md) | How this SDK works |
| [docs/llm/boundaries.md](docs/llm/boundaries.md) | What it owns, and what it must not touch |
| [docs/llm/workflows.md](docs/llm/workflows.md) | Adding a resource or a method; releasing |
| [docs/engineering-practices.md](docs/engineering-practices.md) | The practices followed, with sources |

Decisions: `docs/design-decisions/` (ADR-0001 to ADR-0005), all binding.

## Rules

1. **Jackson is the only runtime dependency**, and no Jackson type appears in a public signature (ADR-0002). HTTP,
   crypto and logging come from the JDK.
2. **Java 17 APIs only** (`--release 17`, ADR-0001). The build fails otherwise.
3. **One package per API area** — `com.qeetgroup.qeetid.{identity,access,federation,developer,operations}`, matching
   the server's OpenAPI documents. Resources are reached from `QeetID` (`qeetId.users()`).
4. **Answers are records, requests are builders** (ADR-0003). `@JsonProperty` names every multi-word wire field;
   collections are never null; nullable components are `@Nullable`.
5. **Every method throws** an unchecked `QeetIDException` (ADR-0005); argument mistakes are the JDK's exceptions.
6. **Never wrap a route an API key can't call.** The route's OpenAPI `security` must list `apiKeyAuth` (or be public
   or `clientCredentials` for the OAuth endpoints).
7. **Retries must never do anything twice** (ADR-0004). Only `GET`, `DELETE`, POSTs marked `retrySafe()` and requests
   with an `Idempotency-Key` are retried. Never `auth.session_revoked`, an OAuth grant or a directory sign-in.
8. **Never weaken verification:** `webhook/WebhookVerifier.java` (HMAC, timestamp, constant-time compare),
   `jwt/TokenVerifier.java` (ES256, kid, issuer, audience), `internal/Transport.java` (credentials, retries).
9. **No secret where it could leak:** not in `toString`, logs, exception messages or test snapshots. A record holding
   a secret redacts it; a builder sets it with `setSecret`.
10. **Tests:** a row in `ResourceRequestsTest` for every resource method, a row in `ModelsTest` for every record and
    builder. Webhook fixtures come from the server's own signing code.

## Commands

```bash
./gradlew build                 # format check, Error Prone + NullAway, tests on JDK 17, coverage floor — the gate
./gradlew test -PtestJdk=21     # the tests on another LTS (21 or 25)
./gradlew javadoc               # Javadoc with doclint, warnings as errors
./gradlew spotlessApply         # format
./gradlew dependencies --write-locks   # after changing a dependency: refresh gradle.lockfile
```

## What CI enforces

`ci.yml` on Java 17, 21 and 25: `./gradlew build javadoc`. `lint.yml`: `spotlessCheck`. `codeql.yml`: CodeQL for
Java. `security.yml`: OSV-Scanner over `gradle.lockfile`, and gitleaks. `version.yml` bumps the patch in
`gradle.properties` on every pull request into `main` (raise minor or major by hand). `release.yml` runs on every push
to `main`: when GitHub Packages does not have that version yet, it runs the gate, publishes with the run's own token
(no stored secret), attests the build provenance, then tags `vX.Y.Z` and opens a GitHub Release with the jars;
otherwise it does nothing. `rollback.yml` marks a bad release withdrawn and makes a good one the latest.

## Branches

Two long-lived branches: `develop`, where work lands, and `main`, what is released. Changes reach `main` only by a pull
request from `develop`. There are no release branches: a version is a `vX.Y.Z` tag, made by `release.yml` when a pull
request is merged into `main`.

## Before you finish

```bash
./gradlew build javadoc
git diff
```

## Escalate rather than proceed

A new runtime dependency · a public type renamed or removed · the auth scheme · weakening verification · making a
non-idempotent call retryable · raising the Java baseline · anything requiring another repository · the coordinates,
the licence or the first published version.
