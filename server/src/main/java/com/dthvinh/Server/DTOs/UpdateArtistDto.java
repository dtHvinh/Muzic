package com.dthvinh.Server.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateArtistDto(
        String name,
        String bio,
        @JsonProperty("profile_image")
        String profileImage,
        @JsonProperty("spotify_id")
        String spotifyId

) {
}
