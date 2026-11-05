/**
 * The exceptions this SDK throws. Every one extends {@link
 * com.qeetgroup.qeetid.exception.QeetIDException} and is unchecked.
 *
 * <pre>
 * QeetIDException
 * ├── ApiException                     the API answered with an error
 * │   ├── BadRequestException          400
 * │   ├── AuthenticationException      401
 * │   │   └── SessionRevokedException  401 auth.session_revoked
 * │   ├── PaymentRequiredException     402
 * │   ├── PermissionDeniedException    403
 * │   ├── NotFoundException            404
 * │   ├── ConflictException            409
 * │   ├── ValidationException          422
 * │   ├── RateLimitException           429
 * │   ├── InternalServerException      5xx
 * │   │   └── ServiceUnavailableException 503
 * │   └── OAuthException               an RFC 6749 token-endpoint error
 * ├── NetworkException                 no response: DNS, TCP, TLS, reset
 * │   └── TimeoutException             no complete response in time
 * ├── WebhookVerificationException     a webhook delivery failed verification
 * └── TokenVerificationException       an access token failed verification
 * </pre>
 *
 * <p>Mistakes in the caller's own arguments are {@link IllegalArgumentException}s and {@link
 * IllegalStateException}s, not part of this tree.
 */
@NullMarked
package com.qeetgroup.qeetid.exception;

import org.jspecify.annotations.NullMarked;
