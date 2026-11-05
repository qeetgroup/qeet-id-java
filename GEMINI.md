# GEMINI.md — qeet-id-java

**Read [AGENTS.md](AGENTS.md) first.** It is the model-neutral instruction file and the source of truth for this
repository. This file is a pointer and adds no architecture.

```text
GEMINI.md  →  AGENTS.md  →  docs/llm/*
```

## Canonical context

| File | For |
| --- | --- |
| [qeet-repo.yml](qeet-repo.yml) | Machine-readable repository identity |
| [AGENTS.md](AGENTS.md) | **Rules, commands, what CI enforces** |
| [docs/llm/context.md](docs/llm/context.md) | How this repository actually works |
| [docs/llm/boundaries.md](docs/llm/boundaries.md) | What it owns, and what it must not touch |
| [docs/llm/workflows.md](docs/llm/workflows.md) | Step-by-step for common changes |
| [docs/llm/architecture-map.md](docs/llm/architecture-map.md) | "Where is X?" — fastest path to a file |

Parent context: **L0** `qeetgroup/qeet-context` · **L1** `qeetgroup/qeet-id-context`.

## What this repository is

The server-side **Java SDK for Qeet ID** — `com.qeetgroup:qeet-id`, module `com.qeetgroup.qeetid`. Java 17 bytecode,
Gradle, **Jackson 2 as the only runtime dependency**. It wraps a contract owned by `qeet-id-server`.

## Non-negotiables

1. **Jackson is the only runtime dependency**, kept out of the API.
2. **Java 17 APIs only.**
3. **Records for answers, builders for requests.**
4. **Only routes an API key may call.**
5. **Retries never repeat a write** (ADR-0004).
6. **Never weaken** webhook or token verification.

## Commands

`./gradlew build` · `./gradlew test -PtestJdk=21` · `./gradlew javadoc` · `./gradlew spotlessApply` ·
`./gradlew dependencies --write-locks`

That list is complete — **do not invent commands.**

## Before finishing

```bash
./gradlew build javadoc
git diff
```
