# Security Policy

## Reporting a vulnerability

Please report security vulnerabilities privately — do not open a public GitHub issue. Email **security@qeet.in** with:

- A description of the vulnerability and its impact.
- Steps to reproduce (a minimal code sample against this SDK, not the Qeet ID backend).
- The SDK version (in the `User-Agent`, and in the jar's `Implementation-Version`) and the Java version affected.

We aim to acknowledge reports within 2 business days.

## Scope

This policy covers the `qeet-id-java` SDK itself — access-token verification (`jwt/TokenVerifier.java`), webhook
verification (`webhook/WebhookVerifier.java`), and the HTTP transport and credentials (`internal/Transport.java`,
`internal/Credential.java`) are the highest-sensitivity surfaces. Vulnerabilities in the Qeet ID platform itself should
be reported through Qeet ID's own security channel, not here.

## Disclosure

We'll credit reporters (unless anonymity is requested) in the release notes once a fix ships.
