# Workflows — qeet-id-java

**Level:** L2 · **Last verified:** 2026-11-05
**Verification scope:** every command checked against `build.gradle.kts` and `.github/workflows/`.

## Set up

```bash
./gradlew build      # downloads Gradle and, if missing, JDK 17 and 25 through the toolchain resolver
```

No services required — tests use an in-process HTTP server.

## Add a method to an existing resource

```text
1  find the route        qeet-id-server api/openapi/<area>.yaml; its security must list apiKeyAuth
2  open <area>/<Resource>.java and match the neighbours: identifiers first, then params, then
                         an overload with RequestOptions; Paths.of("/v1/…/{id}", id) for the path
3  types                 an answer is a record (@JsonProperty on multi-word fields, @Nullable when it
                         may be absent, Instant for times); a body is a <Resource><Action>Params builder
4  retries               a write is retried only if the API makes it idempotent (.idempotent())
                         or it changes nothing (.retrySafe())
5  test                  a row in ResourceRequestsTest; rows in ModelsTest for new records and builders
6  README and CHANGELOG
7  ./gradlew spotlessApply && ./gradlew build javadoc
```

## Add a whole resource

```text
1  <area>/<Resource>.java — public final class with a Transport field and a public constructor
2  an accessor on QeetID, under its area comment
3  its package is already exported in module-info.java; a new package must be added there
4  steps 5–7 above
```

## Paged lists

A route answering `{items, next_cursor}` returns `Page<T>` via `http.page(path, params, T.class, options)`. A route
answering `{items, truncated?}` returns `ListResult<T>` via `http.list(...)`.

## Change transport, retries or errors

**Review required.** `internal/{Transport,RetryPolicy,ErrorMapper,Credential}.java`. Keep ADR-0004: never make a
write retryable without an idempotency key, never retry `auth.session_revoked`. Add a test in `internal/`.

## Change cryptography

**Security review required.** `webhook/WebhookVerifier.java` or `jwt/TokenVerifier.java`. Cite the RFC or the server
code; constant-time comparisons; negative tests. Webhook fixtures (`src/test/resources/webhook/deliveries.json`) are
regenerated from the server's own `deliver` and `sign`, never written by hand.

## Change a dependency

Edit `gradle/libs.versions.toml`, then `./gradlew dependencies --write-locks` and commit `gradle.lockfile`.

## Release

```text
1  CHANGELOG.md: move [Unreleased] under the version being released
2  open a pull request from develop into main: version.yml bumps the patch in gradle.properties on the PR
   branch (for a minor or major, set the version yourself)
3  merge it: release.yml sees a version GitHub Packages does not have, runs the gate, publishes
   (publishReleasePublicationToRegistryRepository), attests the files, then the vX.Y.Z tag and a GitHub Release
```

A push whose version is already in GitHub Packages releases nothing, so re-running is safe. A published version is
never replaced: run `rollback.yml` (good and bad version) to mark a bad release withdrawn, then release a fix.

No secret is needed: the publish job uses the run's own `GITHUB_TOKEN`, in the `maven-publish` environment. To try
the publication locally, `./gradlew publishReleasePublicationToReleaseCandidateRepository` writes it to
`build/release-candidate/maven` and uploads nothing.
