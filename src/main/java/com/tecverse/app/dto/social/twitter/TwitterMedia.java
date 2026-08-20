package com.tecverse.app.dto.social.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TwitterMedia(
        @JsonProperty("media_key") String mediaKey,
        String url,
        String type
) {
}
