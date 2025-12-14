package com.dthvinh.Server.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateSongDto(
        String title,
        @JsonProperty("artist_id") Long artistId,
        @JsonProperty("spotify_id") String spotifyId,
        @JsonProperty("audio_url") String audioUrl) {
}
