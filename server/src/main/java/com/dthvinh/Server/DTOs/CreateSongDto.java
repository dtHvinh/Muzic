package com.dthvinh.Server.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateSongDto(
        String title,
        @JsonProperty("artist_id") Long artistId,
        @JsonProperty("spotify_id") String spotifyId,
        @JsonProperty("duration_ms") Integer durationMs) {
}
