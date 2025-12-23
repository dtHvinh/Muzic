package com.dthvinh.Server.Models;

import com.dthvinh.Server.DTOs.CreateArtistDto;
import com.dthvinh.Server.DTOs.UpdateArtistDto;
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

    public static Artist from(CreateArtistDto dto) {
        return new Artist(null,
                dto.name(),
                dto.bio(),
                dto.profileImage(),
                dto.spotifyId(),
                null,
                null);
    }

    public Artist from(UpdateArtistDto other) {
        return new Artist(
                getId(),
                other.name() != null ? other.name() : getName(),
                other.bio() != null ? other.bio() : getBio(),
                other.profileImage() != null ? other.profileImage() : getProfileImage(),
                other.spotifyId() != null ? other.spotifyId() : getSpotifyId(),
                getCreatedAt(),
                LocalDateTime.now());
    }
}
