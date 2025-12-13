package com.dthvinh.Server.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateArtistDto(
        String name,
        String bio,
        @JsonProperty("profile_image")
        String profileImage,
        @JsonProperty("spotify_id")
        String spotifyId
) {
}
