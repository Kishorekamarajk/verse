package com.tecverse.app.dto.social.twitter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TwitterIncludes(List<TwitterMedia> media) {
}
