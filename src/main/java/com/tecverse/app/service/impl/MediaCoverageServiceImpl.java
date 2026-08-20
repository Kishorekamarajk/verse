package com.tecverse.app.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tecverse.app.dto.social.SocialPostDto;
import com.tecverse.app.entity.MediaCoverage;
import com.tecverse.app.fetcher.SocialMediaFetcher;
import com.tecverse.app.repository.MediaCoverageRepository;
import com.tecverse.app.service.MediaCoverageService;

@Service
public class MediaCoverageServiceImpl implements MediaCoverageService {

    private static final Logger log = LoggerFactory.getLogger(MediaCoverageServiceImpl.class);

    private final MediaCoverageRepository mediaCoverageRepository;
    private final List<SocialMediaFetcher> fetchers;

    public MediaCoverageServiceImpl(MediaCoverageRepository mediaCoverageRepository, List<SocialMediaFetcher> fetchers) {
        this.mediaCoverageRepository = mediaCoverageRepository;
        this.fetchers = fetchers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MediaCoverage> getLatestMediaCoverage() {
        return mediaCoverageRepository.findTop8ByOrderByPublishedDateDesc();
    }

    @Override
    @Transactional
    public void refreshFromSocialMedia() {
        for (SocialMediaFetcher fetcher : fetchers) {
            try {
                List<SocialPostDto> posts = fetcher.fetchLatestPosts();
                saveNewPosts(posts);
            } catch (Exception ex) {
                log.error("Failed to refresh media coverage from {}: {}", fetcher.getPlatform(), ex.getMessage(), ex);
            }
        }
    }

    private void saveNewPosts(List<SocialPostDto> posts) {
        if (posts.isEmpty()) {
            return;
        }

        List<String> postUrls = posts.stream().map(SocialPostDto::postUrl).toList();
        Set<String> existingUrls = mediaCoverageRepository.findByPostUrlIn(postUrls).stream()
                .map(MediaCoverage::getPostUrl)
                .collect(Collectors.toSet());

        List<MediaCoverage> newEntries = posts.stream()
                .filter(post -> !existingUrls.contains(post.postUrl()))
                .map(this::toEntity)
                .toList();

        if (!newEntries.isEmpty()) {
            mediaCoverageRepository.saveAll(newEntries);
            log.info("Saved {} new media coverage post(s) from {}", newEntries.size(), newEntries.get(0).getPlatform());
        }
    }

    private MediaCoverage toEntity(SocialPostDto dto) {
        return MediaCoverage.builder()
                .platform(dto.platform())
                .title(dto.title())
                .description(dto.description())
                .imageUrl(dto.imageUrl())
                .postUrl(dto.postUrl())
                .publishedDate(dto.publishedDate())
                .build();
    }
}
