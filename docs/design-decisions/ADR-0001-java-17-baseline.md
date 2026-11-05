# ADR-0001: Java 17 as the baseline, compiled by JDK 25, tested on every LTS

**Status:** Accepted

## Context

An SDK serves whatever Java its callers run. On 5 November 2026 the LTS releases are 17, 21 and 25. Java 17 is past
Oracle's Premier Support but in Extended Support to 2029 and still updated by Adoptium and the other vendors; Spring
Boot 3 and 4 and Jackson 3 all require 17, so enterprise services sit on 17 or later. Error Prone, which the build
relies on, runs only on JDK 21 or later since 2.43.

## Decision

- The library is compiled with `--release 17`: Java 17 bytecode and Java 17 APIs only.
- JDK 25 compiles it, through a Gradle toolchain, so Error Prone and NullAway can run.
- The tests run on 17 by default and on 17, 21 and 25 in CI (`-PtestJdk`).
- The language is Java, not Kotlin.

## Why

17 is the oldest LTS that enterprise services still run and the newest that excludes none of them. 21's additions
(virtual threads, pattern matching in `switch`) give an HTTP SDK nothing it needs: a synchronous client already works on
virtual threads. A Kotlin library would push `kotlin-stdlib` and Kotlin idioms onto Java callers; a Java library serves
Java, Kotlin, Scala and Groovy alike.

## Consequences

Records, sealed interfaces and pattern matching for `instanceof` are available; `switch` patterns are not. Raising the
baseline is a breaking change for callers on 17 and needs a major version.
