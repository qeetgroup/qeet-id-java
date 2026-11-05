package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * What the retention policy would purge now.
 *
 * @param ripeDeletedUsers the ripe deleted users
 * @param deletedUsersDays the deleted users days
 */
public record RetentionPreview(
    @JsonProperty("ripe_deleted_users") int ripeDeletedUsers,
    @JsonProperty("deleted_users_days") int deletedUsersDays) {}
