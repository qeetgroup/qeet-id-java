package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

/**
 * An application holding a machine grant that nobody has reviewed: a candidate for unmanaged AI or
 * automation access.
 *
 * @param id its id
 * @param clientId the client's id
 * @param name its name
 * @param grantTypes the grant types
 * @param liveGrants Live refresh grants: the higher, the more it is in use.
 * @param createdAt when it was created
 */
public record ShadowAiApplication(
    String id,
    @JsonProperty("client_id") String clientId,
    String name,
    @JsonProperty("grant_types") List<String> grantTypes,
    @JsonProperty("live_grants") int liveGrants,
    @JsonProperty("created_at") Instant createdAt) {}
