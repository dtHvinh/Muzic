package com.dthvinh.Server.Models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public final class Artist {
    private Long id;
    private String name;
    private String bio;
    private String profileImage;
    private String spotifyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Artist(
            Long id,
            String name,
            String bio,
            String profileImage,     // matches profile_image column
            String spotifyId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.profileImage = profileImage;
        this.spotifyId = spotifyId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Artist(String name, String bio, String profileImage, String spotifyId) {
        this(null, name, bio, profileImage, spotifyId, null, null);
    }
}
