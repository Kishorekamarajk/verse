package com.tecverse.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * An official TECVERSE social media post surfaced in the Media Coverage section.
 * Maps 1:1 to {@code media_coverage}.
 */
@Entity
@Table(
        name = "media_coverage",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_media_coverage_post_url", columnNames = "post_url")
        },
        indexes = {
                @Index(name = "idx_media_coverage_published_date", columnList = "published_date")
        }
)
public class MediaCoverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, length = 20)
    private SocialPlatform platform;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "post_url", nullable = false, length = 500)
    private String postUrl;

    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public MediaCoverage() {
    }

    public MediaCoverage(Long id, SocialPlatform platform, String title, String description, String imageUrl,
                          String postUrl, LocalDateTime publishedDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.platform = platform;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.publishedDate = publishedDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MediaCoverageBuilder builder() {
        return new MediaCoverageBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SocialPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(SocialPlatform platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class MediaCoverageBuilder {
        private Long id;
        private SocialPlatform platform;
        private String title;
        private String description;
        private String imageUrl;
        private String postUrl;
        private LocalDateTime publishedDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public MediaCoverageBuilder id(Long id) { this.id = id; return this; }
        public MediaCoverageBuilder platform(SocialPlatform platform) { this.platform = platform; return this; }
        public MediaCoverageBuilder title(String title) { this.title = title; return this; }
        public MediaCoverageBuilder description(String description) { this.description = description; return this; }
        public MediaCoverageBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public MediaCoverageBuilder postUrl(String postUrl) { this.postUrl = postUrl; return this; }
        public MediaCoverageBuilder publishedDate(LocalDateTime publishedDate) { this.publishedDate = publishedDate; return this; }
        public MediaCoverageBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public MediaCoverageBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public MediaCoverage build() {
            return new MediaCoverage(id, platform, title, description, imageUrl, postUrl, publishedDate, createdAt, updatedAt);
        }
    }
}
