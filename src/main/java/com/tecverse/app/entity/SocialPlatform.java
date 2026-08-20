package com.tecverse.app.entity;

/**
 * Official TECVERSE social media channels that back the Media Coverage section.
 */
public enum SocialPlatform {

    INSTAGRAM("Instagram"),
    FACEBOOK("Facebook"),
    TWITTER("X (Twitter)");

    private final String displayName;

    SocialPlatform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
