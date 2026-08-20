package com.tecverse.app.dto.social.twitter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Root response of the X (Twitter) API v2 {@code /2/users/{id}/tweets} endpoint,
 * including the expanded media objects referenced by each tweet's attachments.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TwitterTweetsResponse(List<TwitterTweetItem> data, TwitterIncludes includes) {
}
