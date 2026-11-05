# CLAUDE.md — qeet-id-java

**Read [AGENTS.md](AGENTS.md) first.** It is the model-neutral instruction file and the source of truth for this
repository. This file adds only Claude-specific guidance.

```text
CLAUDE.md  →  AGENTS.md  →  docs/llm/*
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

## The things most likely to trip you up here

1. **Jackson is the only runtime dependency**, and never in a public signature.
2. **Java 17 APIs only**; JDK 25 compiles with `--release 17`.
3. **Records for answers, builders for requests**; `@JsonProperty` on multi-word fields.
4. **Only routes an API key may call** — the route's OpenAPI `security` must list `apiKeyAuth`.
5. **Retries never repeat a write** (ADR-0004); never `auth.session_revoked`.
6. **Never weaken** `WebhookVerifier`, `TokenVerifier` or `Transport`.
7. **`-Werror`:** Error Prone, NullAway and every javac lint fail the build. Fix the cause; suppress only with a reason.

## Working style

- **Read before editing.** Match the neighbouring resource class rather than introducing an abstraction.
- Run `./gradlew spotlessApply` before `./gradlew build`, not in the same invocation.
- **Do not read `.env*` or secret files** into anything you write.

## Finishing a change

```bash
./gradlew build javadoc
git diff
```

## Escalate rather than proceed

Stop and report if a task would weaken a security control, change a published contract, or require modifying another
repository.
