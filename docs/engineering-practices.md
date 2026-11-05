# Engineering practices

The practices this SDK follows, and where each comes from. Researched on 5 November 2026. When a practice changes,
change it here first and record why.

## Java support

| Practice | How this SDK applies it | Source |
| --- | --- | --- |
| Baseline: Java 17, the oldest LTS still widely supported | Compiled with `--release 17`. On 5 November 2026 Oracle's Premier Support for 17 has ended (September 2026) but Extended Support runs to September 2029, with the fee waived from October 2026; Adoptium and the other vendors still ship 17 updates. Spring Boot 3 and 4 and Jackson 3 all require 17, so 17 is where enterprise services are; choosing 21 would cut out every service still on 17 and gain little an SDK needs (virtual threads work with it either way) | [Oracle Java SE Support Roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html); [Adoptium support](https://adoptium.net/support/); [Spring Boot system requirements](https://docs.spring.io/spring-boot/system-requirements.html) |
| Tested on every supported LTS | CI runs the tests on 17, 21 and 25 (`-PtestJdk`); 25 is the newest LTS, 29 the next (September 2027) | [Oracle Java SE Support Roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html) |
| Compile with a newer JDK than the baseline | JDK 25 compiles, with `--release 17`, through a Gradle toolchain: Error Prone needs JDK 21 or later to run since 2.43, and `--release` guarantees only Java 17 APIs are used | [Error Prone releases](https://github.com/google/error-prone/releases); [NullAway compatibility](https://github.com/uber/NullAway/wiki/Compatibility); [Gradle toolchains](https://docs.gradle.org/current/userguide/toolchains.html) |
| Java, not Kotlin | Java libraries serve Java, Kotlin, Scala and Groovy callers equally; a Kotlin library pushes `kotlin-stdlib` and Kotlin idioms onto Java callers | The owner's decision |

## Build

| Practice | How | Source |
| --- | --- | --- |
| Gradle with the Kotlin DSL, through the wrapper | `./gradlew`, Gradle 9.8; a version catalog (`gradle/libs.versions.toml`); toolchains, auto-provisioned by the Foojay resolver where a JDK is missing. Gradle over Maven for toolchains (compile on 25, test on 17/21/25 in one build), and because it is what Stripe, Auth0 and WorkOS build their Java SDKs with | [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html); [Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html); [stripe-java](https://github.com/stripe/stripe-java); [auth0-java](https://github.com/auth0/auth0-java) |
| Reproducible resolution | Dependency locking: every resolved version is in `gradle.lockfile`, which the dependency scan reads | [Gradle dependency locking](https://docs.gradle.org/current/userguide/dependency_locking.html) |
| Formatting | google-java-format 2.0 through Spotless; `./gradlew spotlessApply`. The Google style is what most Java SDKs use; Spotless 8.11 requires google-java-format 2.0 | [google-java-format](https://github.com/google/google-java-format); [Spotless for Gradle](https://github.com/diffplug/spotless/tree/main/plugin-gradle) |
| Static analysis | Error Prone with NullAway in JSpecify mode, `-Xlint:all -Werror`. NullAway proves the JSpecify annotations the API promises | [Error Prone](https://errorprone.info/); [NullAway JSpecify support](https://github.com/uber/NullAway/wiki/JSpecify-Support) |
| Coverage | JaCoCo, with a build-breaking floor of 85% of lines | [JaCoCo](https://www.jacoco.org/jacoco/) |
| Tests | JUnit Jupiter (JUnit 6.1, the current line of the JUnit 5 programming model) with an in-process JDK `HttpServer`; no mocking library, no real server | [JUnit](https://junit.org/) |

## Dependencies

| Practice | How | Source |
| --- | --- | --- |
| HTTP from the JDK | `java.net.http.HttpClient`: HTTP/2, TLS and timeouts with no dependency. `sendAsync` bounded by the timeout covers the body as well as the headers | [HttpClient](https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/HttpClient.html) |
| One runtime dependency: Jackson 2 | Jackson is the de facto JSON library. Jackson 2 rather than 3: Spring Boot 3 services already carry 2.x, and Jackson 3 needs `jackson-annotations` 2.20 or later, which Spring Boot 3's dependency management pins lower. The 2.21 line is an LTS branch. Tested with 2.15, 2.17 and 2.21 | [Jackson 3.0 release](https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.0); [Jackson versioning and LTS](https://github.com/FasterXML/jackson/blob/main/VERSIONING.md); [Spring Boot and Jackson 3](https://spring.io/blog/2025/10/07/introducing-jackson-3-support-in-spring) |
| Keep the dependency out of the API | No Jackson type in a public signature: free-form JSON is `Map<String, Object>`, so moving to Jackson 3 later breaks no caller | [Azure: Java implementation](https://azure.github.io/azure-sdk/java_implementation.html) ("DO NOT depend on concrete … technologies") |
| Crypto from the JDK | HMAC-SHA256 (`Mac`), constant-time comparison (`MessageDigest.isEqual`), ES256 (`SHA256withECDSAinP1363Format`, which takes the JOSE `R‖S` signature directly). A JOSE library would add a dependency for one algorithm | [JDK standard algorithm names](https://docs.oracle.com/en/java/javase/17/docs/specs/security/standard-names.html); [RFC 7518 §3.4](https://www.rfc-editor.org/rfc/rfc7518#section-3.4) |
| Logging through `System.Logger` | No SLF4J dependency: the JDK's platform logging facade, which SLF4J (`slf4j-jdk-platform-logging`) and Log4j (`log4j-jpl`) take over | [JEP 264](https://openjdk.org/jeps/264); [SLF4J manual](https://www.slf4j.org/manual.html); [Azure: Java introduction](https://azure.github.io/azure-sdk/java_introduction.html) ("DO NOT depend on concrete logging … technologies") |

## API shape

| Practice | How this SDK applies it | Source |
| --- | --- | --- |
| One immutable, thread-safe client from a builder | `QeetID.builder()…build()`; no public constructor; resources as accessors (`qeetId.users()`) | [Azure: Java introduction](https://azure.github.io/azure-sdk/java_introduction.html) ("DO ensure that all service client classes are immutable", "DO offer a fluent builder API"); [StripeClient](https://github.com/stripe/stripe-java) |
| Records for answers | Components are the API's fields, camel-cased, with `@JsonProperty` naming the wire field; `Instant` for times; collections never null (empty when absent) | [JEP 395](https://openjdk.org/jeps/395); Effective Java, 3rd ed., Item 54; [Azure: Java introduction](https://azure.github.io/azure-sdk/java_introduction.html) ("must use the … `java.time` package") |
| Builders for requests | Only the fields set are sent; an explicit `null` clears a field on an update; a new optional field never breaks a caller, as a record's constructor would | [AWS SDK for Java 2.x](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/using.html) (request builders); [Stripe params](https://github.com/stripe/stripe-java) |
| Nullness: JSpecify, not `Optional` | Every package is `@NullMarked`; absent values are `@Nullable`. `Optional` is avoided in records and accessors, as Azure's guidelines and Jackson records make it awkward | [JSpecify user guide](https://jspecify.dev/docs/user-guide/); [Kotlin: nullability annotations](https://kotlinlang.org/docs/java-interop.html#nullability-annotations) |
| Per-call options last, as an overload | `RequestOptions` (timeout, retries, headers, idempotency key) on every method | [StripeClient RequestOptions](https://github.com/stripe/stripe-java); [Azure: Context/RequestOptions](https://azure.github.io/azure-sdk/java_introduction.html) |
| Paged lists are `Iterable` and streamable | `Page<T>` iterates every item across pages, `stream()` lazily, `pages()` page by page | [Azure: `PagedIterable`, `iterableByPage`](https://azure.github.io/azure-sdk/java_introduction.html); [AWS SDK pagination](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/pagination.html); [Stripe `autoPagingIterable`](https://github.com/stripe/stripe-java) |
| A named module | `module com.qeetgroup.qeetid` exports only the API packages; `internal` is not exported. Works on the class path too | [Azure: Java introduction](https://azure.github.io/azure-sdk/java_introduction.html) ("DO include a `module-info.java`", "DO export only packages that are considered public API") |
| Naming | `com.qeetgroup` and `com.qeetgroup.qeetid`, as Qeetrix uses `com.qeetgroup.qeetrix`; verbs from a short list (`get`, `list`, `create`, `update`, `delete`, plus domain verbs) | The owner's decision; [Azure: Java introduction](https://azure.github.io/azure-sdk/java_introduction.html) |

## Errors

| Practice | How | Source |
| --- | --- | --- |
| Unchecked exceptions | `QeetIDException extends RuntimeException`; a subclass per actionable status | [Azure: Java introduction](https://azure.github.io/azure-sdk/java_introduction.html) ("DO use unchecked exceptions"); [AWS SDK exceptions](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/handling-exceptions.html) |
| Exceptions carry what is needed to act | code, message, status, request ID, detail, fields, metadata, `Retry-After`, headers, from the envelope `{"error": {...}}` and `X-Request-Id` | the server's `httpx.WriteError`; [Stripe request IDs](https://github.com/stripe/stripe-java) |
| OAuth errors in their own shape | `OAuthException` for RFC 6749 §5.2 `{error, error_description, error_uri}` | [RFC 6749 §5.2](https://www.rfc-editor.org/rfc/rfc6749#section-5.2); [RFC 8628 §3.5](https://www.rfc-editor.org/rfc/rfc8628#section-3.5) |
| Argument mistakes are the JDK's exceptions | `IllegalArgumentException`, `IllegalStateException`, `NullPointerException`, before anything is sent | [Effective Java, Item 72](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/) |

## Reliability

| Practice | How | Source |
| --- | --- | --- |
| Retry only what is safe to repeat | GET and DELETE, read-only POSTs, and requests carrying an `Idempotency-Key`; never PUT or PATCH | [RFC 9110 §9.2.2](https://www.rfc-editor.org/rfc/rfc9110#section-9.2.2); [Stripe: idempotent requests](https://docs.stripe.com/api/idempotent_requests) |
| Retry only transient failures | no response, timeout, 408, 429, 500, 502, 503, 504, 409 `idempotency.in_progress`; never 401 `auth.session_revoked` | [AWS SDK retry strategies](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/retry-strategy.html); [Azure Core: retry policy](https://azure.github.io/azure-sdk/general_azurecore.html) |
| Backoff with jitter; honour `Retry-After` | 0.5 s doubling to 8 s with equal jitter; `Retry-After` up to 60 s is waited out, a longer one thrown | [AWS: Exponential backoff and jitter](https://aws.amazon.com/blogs/architecture/exponential-backoff-and-jitter/); [RFC 9110 §10.2.3](https://www.rfc-editor.org/rfc/rfc9110#section-10.2.3) |
| Idempotency keys make creates retryable | A UUID generated per call on the nine routes the server makes idempotent (`IdempotentRoutes()`), kept across that call's retries | [Stripe: idempotent requests](https://docs.stripe.com/api/idempotent_requests) |
| Timeouts per attempt | 30 s, body included, per client and per call | Stripe (80 s), WorkOS (60 s), Auth0 (60 s) defaults |
| Interruption is respected | An interrupted call stops at once, is not retried, and leaves the interrupt flag set | [Java Concurrency in Practice, §7.1](https://jcip.net/) |

## Security

| Practice | How | Source |
| --- | --- | --- |
| No secret in logs, errors or `toString` | the client, parameters (`setSecret`), records holding a secret, `OAuthClient` and exceptions redact; the logger gets method, path, status, timing and request ID only | [Azure: Java implementation](https://azure.github.io/azure-sdk/java_implementation.html) (logging); [OWASP Logging Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html) |
| No plaintext transport | `baseUrl`, JWKS and discovery URLs must be https except for loopback; redirects are not followed | — |
| Webhooks: HMAC over timestamp and body, constant time, replay window | every value against every secret with `MessageDigest.isEqual`, five-minute tolerance | [Standard Webhooks](https://www.standardwebhooks.com/); the server's `internal/developer/webhooks` |
| JWT: pinned algorithm, required issuer and audience, bounded clock skew, kid-driven refresh with a cooldown | ES256 only, no `crit`, 30 s leeway, 5-minute cache, 30-second refetch cooldown | [RFC 8725](https://www.rfc-editor.org/rfc/rfc8725); [RFC 9068](https://www.rfc-editor.org/rfc/rfc9068) |
| Client authentication by `client_secret_post` | credentials in the form body; the server accepts Basic too | [RFC 6749 §2.3.1](https://www.rfc-editor.org/rfc/rfc6749#section-2.3.1) |

## CI and releasing

| Practice | How | Source |
| --- | --- | --- |
| CI on every supported LTS | `ci.yml`: build once with JDK 25, test on 17, 21 and 25, Javadoc with doclint | [setup-java](https://github.com/actions/setup-java); [setup-gradle](https://github.com/gradle/actions) |
| Code scanning | CodeQL for Java, manual build mode | [CodeQL for compiled languages](https://docs.github.com/en/code-security/code-scanning/creating-an-advanced-setup-for-code-scanning/codeql-code-scanning-for-compiled-languages) |
| Dependency scanning | OSV-Scanner over `gradle.lockfile`, weekly and on every pull request; gitleaks over the history | [OSV-Scanner](https://google.github.io/osv-scanner/); [gitleaks](https://github.com/gitleaks/gitleaks) |
| GitHub Packages, like Qeetrix for Android | Gradle's own `maven-publish` publishes the jar, sources, Javadoc, POM and Gradle module metadata with the workflow's `GITHUB_TOKEN`: no stored secret, no signing key, no external account | [GitHub Packages: the Gradle registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry); [Gradle: Maven Publish plugin](https://docs.gradle.org/current/userguide/publishing_maven.html) |
| Build provenance | `actions/attest-build-provenance` attests every published file; the GitHub Release carries the jars and the POM | [Artifact attestations](https://docs.github.com/en/actions/security-for-github-actions/using-artifact-attestations/using-artifact-attestations-to-establish-provenance-for-builds) |
| Release on merge, version reviewed in the PR | `version.yml` raises the patch in `gradle.properties` on the pull request into `main` when something that ships changed, through the GitHub API without checking out the pull request; `release.yml` publishes on the push to `main` only when GitHub Packages does not have that version, and tags after a successful publish; `rollback.yml` withdraws a bad release | Qeetrix for Android's release workflows |
| Versions and history | Semantic Versioning; a Keep a Changelog `CHANGELOG.md` | [semver.org](https://semver.org/spec/v2.0.0.html); [keepachangelog.com](https://keepachangelog.com/en/1.1.0/) |

## Spring Boot

A small starter is the standard way to offer Spring Boot integration: Okta, Azure and AWS (Spring Cloud AWS) ship one;
Stripe, Auth0's management SDK and WorkOS do not. Spring's guidance is one combined module named
`<library>-spring-boot-starter`, not starting with `spring-boot`, with its own configuration namespace. None is built
here yet: whether to ship `qeet-id-spring-boot-starter` is the owner's decision.

Sources: [Spring Boot: creating your own auto-configuration](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html);
[okta-spring-boot](https://github.com/okta/okta-spring-boot); [Spring Cloud Azure](https://learn.microsoft.com/azure/developer/java/spring-framework/);
[Spring Cloud AWS](https://awspring.io/).
