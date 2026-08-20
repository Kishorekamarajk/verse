package com.tecverse.app.fetcher;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.tecverse.app.config.SocialMediaProperties;
import com.tecverse.app.dto.social.SocialPostDto;
import com.tecverse.app.dto.social.twitter.TwitterMedia;
import com.tecverse.app.dto.social.twitter.TwitterTweetItem;
import com.tecverse.app.dto.social.twitter.TwitterTweetsResponse;
import com.tecverse.app.entity.SocialPlatform;

/**
 * Retrieves the latest TECVERSE posts from the X (Twitter) API v2 for a given user.
 */
@Component
public class TwitterPostFetcher implements SocialMediaFetcher {

    private static final Logger log = LoggerFactory.getLogger(TwitterPostFetcher.class);
    private static final String API_BASE_URL = "https://api.twitter.com/2";
    private static final int MAX_DESCRIPTION_LENGTH = 220;
    private static final int MAX_TITLE_LENGTH = 100;

    private final RestClient restClient;
    private final SocialMediaProperties.Twitter config;

    public TwitterPostFetcher(RestClient socialMediaRestClient, SocialMediaProperties socialMediaProperties) {
        this.restClient = socialMediaRestClient;
        this.config = socialMediaProperties.getTwitter();
    }

    @Override
    public SocialPlatform getPlatform() {
        return SocialPlatform.TWITTER;
    }

    @Override
    public List<SocialPostDto> fetchLatestPosts() {
        if (!config.isEnabled() || isBlank(config.getBearerToken()) || isBlank(config.getUserId())) {
            log.debug("X (Twitter) fetcher is disabled or not configured; skipping.");
            return Collections.emptyList();
        }

        try {
            TwitterTweetsResponse response = restClient.get()
                    .uri(API_BASE_URL
                            + "/users/{userId}/tweets?max_results=10&tweet.fields=created_at,text&expansions=attachments.media_keys&media.fields=url,type",
                            config.getUserId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + config.getBearerToken())
                    .retrieve()
                    .body(TwitterTweetsResponse.class);

            if (response == null || response.data() == null) {
                return Collections.emptyList();
            }

            Map<String, String> mediaUrlByKey = resolveMediaUrls(response);

            return response.data().stream()
                    .map(item -> toSocialPost(item, mediaUrlByKey))
                    .toList();
        } catch (RestClientResponseException ex) {
            logApiError(ex);
            return Collections.emptyList();
        } catch (ResourceAccessException ex) {
            log.error("X (Twitter) API network interruption: {}", ex.getMessage());
            return Collections.emptyList();
        } catch (Exception ex) {
            log.error("Unexpected error fetching X (Twitter) posts: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    private Map<String, String> resolveMediaUrls(TwitterTweetsResponse response) {
        if (response.includes() == null || response.includes().media() == null) {
            return Map.of();
        }
        return response.includes().media().stream()
                .filter(media -> media.url() != null)
                .collect(Collectors.toMap(TwitterMedia::mediaKey, TwitterMedia::url, (first, second) -> first));
    }

    private SocialPostDto toSocialPost(TwitterTweetItem item, Map<String, String> mediaUrlByKey) {
        String text = item.text() == null ? "" : item.text().trim();
        String title = text.isBlank() ? "X Update" : truncate(text, MAX_TITLE_LENGTH);
        String description = text.isBlank() ? "View this update on X." : truncate(text, MAX_DESCRIPTION_LENGTH);
        String imageUrl = resolveImageUrl(item, mediaUrlByKey);
        String handle = !isBlank(config.getHandle()) ? config.getHandle() : config.getUserId();
        String postUrl = "https://twitter.com/" + handle + "/status/" + item.id();

        return new SocialPostDto(SocialPlatform.TWITTER, title, description, imageUrl, postUrl, parseTimestamp(item.createdAt()));
    }

    private String resolveImageUrl(TwitterTweetItem item, Map<String, String> mediaUrlByKey) {
        if (item.attachments() == null || item.attachments().mediaKeys() == null) {
            return null;
        }
        return item.attachments().mediaKeys().stream()
                .map(mediaUrlByKey::get)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private LocalDateTime parseTimestamp(String createdAt) {
        if (isBlank(createdAt)) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.ofInstant(Instant.parse(createdAt), ZoneOffset.UTC);
        } catch (Exception ex) {
            log.warn("Could not parse X (Twitter) timestamp '{}', defaulting to now.", createdAt);
            return LocalDateTime.now();
        }
    }

    private void logApiError(RestClientResponseException ex) {
        int statusCode = ex.getStatusCode().value();
        if (statusCode == 429) {
            log.warn("X (Twitter) API rate limit exceeded: {}", ex.getMessage());
        } else if (statusCode == 401 || statusCode == 403) {
            log.error("X (Twitter) API authentication failed: {}", ex.getMessage());
        } else {
            log.error("X (Twitter) API returned an error (status {}): {}", statusCode, ex.getMessage());
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength).trim() + "...";
    }
}
