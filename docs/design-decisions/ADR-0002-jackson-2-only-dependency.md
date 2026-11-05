# ADR-0002: Jackson 2 is the only runtime dependency, and stays out of the API

**Status:** Accepted

## Context

The JDK provides HTTP (`java.net.http`), cryptography (HMAC, ECDSA) and logging (`System.Logger`), but no JSON. Jackson
is the de facto JSON library on the JVM. Jackson 3 (October 2025) moved to new packages and needs `jackson-annotations`
2.20 or later; Spring Boot 4 defaults to it, while Spring Boot 3 services carry Jackson 2 and pin `jackson-annotations`
below 2.20.

## Decision

- `com.fasterxml.jackson.core:jackson-databind` 2.21 (an LTS line) is the one runtime dependency, an `implementation`
  dependency: runtime scope in the POM. JSpecify's annotations are the only compile-scope dependency.
- No Jackson type appears in a public signature. Free-form JSON is `Map<String, Object>`; events and claims expose
  their fields as maps or typed accessors.
- Records name their wire fields with `@JsonProperty` rather than a naming strategy, so the mapping holds on every
  Jackson 2 version and reads as documentation.
- HTTP, cryptography and logging come from the JDK; no SLF4J, no HTTP client library, no JOSE library.

## Why

Jackson 2 is already on the class path of most JVM services, so the SDK adds nothing for them; Jackson 3 would add a
second copy or clash with Spring Boot 3's pins. Keeping Jackson out of the API means moving to Jackson 3 later breaks no
caller. The suite passes on Jackson 2.15, 2.17 and 2.21; 2.15 is the floor.

## Consequences

The SDK uses one private `ObjectMapper` with its own settings (unknown fields ignored, absent collections empty,
`Instant` as RFC 3339) and never touches the caller's.
