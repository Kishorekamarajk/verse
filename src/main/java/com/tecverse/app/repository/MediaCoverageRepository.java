package com.tecverse.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecverse.app.entity.MediaCoverage;

public interface MediaCoverageRepository extends JpaRepository<MediaCoverage, Long> {

    boolean existsByPostUrl(String postUrl);

    List<MediaCoverage> findByPostUrlIn(List<String> postUrls);

    List<MediaCoverage> findTop8ByOrderByPublishedDateDesc();
}
