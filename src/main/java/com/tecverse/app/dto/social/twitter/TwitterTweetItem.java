package com.tecverse.app.dto.social.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single item from the X (Twitter) API v2 {@code /2/users/{id}/tweets} endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TwitterTweetItem(
        String id,
        String text,
        @JsonProperty("created_at") String createdAt,
        TwitterAttachments attachments
) {
}
