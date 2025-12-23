package com.dthvinh.Server.DTOs;

import com.dthvinh.Server.Models.Artist;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ArtistResponseDto(
        Long id,
        String name,
        String bio,
        @JsonProperty("profileImage") String profileImage,
        @JsonProperty("spotifyId") String spotifyId) {
    public static ArtistResponseDto from(Artist artist) {
        return new ArtistResponseDto(
                artist.getId(),
                artist.getName(),
                artist.getBio(),
                artist.getProfileImage(),
                artist.getSpotifyId());
    }
}
