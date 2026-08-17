package com.tecverse.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Credentials and identifiers for the official TECVERSE social media accounts polled
 * by the Media Coverage feature. Bound from {@code tecverse.social.*} properties.
 */
@Component
@ConfigurationProperties(prefix = "tecverse.social")
public class SocialMediaProperties {

    private Instagram instagram = new Instagram();
    private Facebook facebook = new Facebook();
    private Twitter twitter = new Twitter();

    public Instagram getInstagram() {
        return instagram;
    }

    public void setInstagram(Instagram instagram) {
        this.instagram = instagram;
    }

    public Facebook getFacebook() {
        return facebook;
    }

    public void setFacebook(Facebook facebook) {
        this.facebook = facebook;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public static class Instagram {
        private boolean enabled = false;
        private String accessToken;
        private String userId;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public static class Facebook {
        private boolean enabled = false;
        private String accessToken;
        private String pageId;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getPageId() {
            return pageId;
        }

        public void setPageId(String pageId) {
            this.pageId = pageId;
        }
    }

    public static class Twitter {
        private boolean enabled = false;
        private String bearerToken;
        private String userId;
        private String handle;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getBearerToken() {
            return bearerToken;
        }

        public void setBearerToken(String bearerToken) {
            this.bearerToken = bearerToken;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }
    }
}
