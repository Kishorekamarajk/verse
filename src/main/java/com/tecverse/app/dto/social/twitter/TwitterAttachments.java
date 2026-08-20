package com.tecverse.app.dto.social.twitter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TwitterAttachments(@JsonProperty("media_keys") List<String> mediaKeys) {
}
