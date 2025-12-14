package com.dthvinh.Server.DTOs;

import com.dthvinh.Server.Models.Song;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SongResponseDto(
        Long id,
        String title,
        @JsonProperty("artistId") Long artistId,
        @JsonProperty("spotifyId") String spotifyId,
        @JsonProperty("durationMs") Integer durationMs) {

    public static SongResponseDto from(Song song) {
        return new SongResponseDto(
                song.getId(),
                song.getTitle(),
                song.getArtistId(),
                song.getSpotifyId(),
                song.getDurationMs());
    }
}
