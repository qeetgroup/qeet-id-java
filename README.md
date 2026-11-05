<div align="center">

# ☕ Qeet ID for Java

**The server-side Java SDK for Qeet ID: manage an organisation's people and access, verify webhooks and access tokens, and run OAuth flows from the JVM.**

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)](#-you-need)
[![Typed](https://img.shields.io/badge/API-typed-2EA043)](#-whats-inside)
[![OpenID Connect](https://img.shields.io/badge/OpenID%20Connect-%E2%9C%93-F78C40?logo=openid&logoColor=white)](#-whats-inside)

[🌐 Qeet ID](https://id.qeet.in) · [📖 Docs](https://docs.qeet.in) · [🧩 API](https://apis.qeet.in) · [🟢 Node SDK](https://github.com/qeetgroup/qeet-id-node) · [🐹 Go SDK](https://github.com/qeetgroup/qeet-id-go)

</div>

## 🧭 What it's for

Qeet ID for Java brings Qeet ID to JVM servers, from Spring Boot services to plain Java. Your backend manages people, roles and organisations through the API, checks that a webhook really came from Qeet ID, and verifies the access tokens your APIs receive, without writing the HTTP, signing or retry code itself.

## 📦 What's inside

| | |
|:--|:--|
| 🧩 **Typed client** | Every resource with typed requests and responses |
| 🚨 **Typed exceptions** | One exception per kind of failure, each with the stable error `code` and the request ID |
| 🔁 **Retries and timeouts** | Safe retries with backoff for idempotent requests, `Retry-After` honoured, never for a revoked session |
| 📄 **Pagination** | Iterate over every item of a list, page by page underneath |
| 🪝 **Webhooks** | Signature checks in constant time, rotation-aware, with typed events |
| 🔑 **Access tokens** | ES256 verification against the JWKS, with the issuer and audience required |
| 🤝 **OAuth** | Client credentials, code exchange, refresh, token exchange, introspection and revocation |

## 🔗 How it fits

| SDK | Language | Role |
|:--|:--|:--|
| **Qeet ID for Java** | Java | This package: the API, webhooks and tokens from JVM servers |
| [`@qeet-id/node`](https://github.com/qeetgroup/qeet-id-node) | Node.js / TypeScript | The same for Node.js |
| [`qeet-id-go`](https://github.com/qeetgroup/qeet-id-go) | Go | The same for Go |
| [`qeet-id-python`](https://github.com/qeetgroup/qeet-id-python) | Python | The same for Python |

## 🧱 You need

| | |
|:--|:--|
| ☕ **Java** | 17 or later |
| 🔐 **Credentials** | An API key, or a service principal's client ID and secret |

## 🛠️ Working on it

| | |
|:--|:--|
| 🌿 **Branches** | Work lands on `develop`; `main` changes only by a pull request from `develop` |
| 🏷️ **Versions** | `vX.Y.Z` tags, made when the release workflow is run on `main` |

<div align="center">

Made with 🧡 by Qeet Group · [⬆️ Back to top](#-qeet-id-for-java)

</div>
