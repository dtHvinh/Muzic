package com.dthvinh.Server.DTOs;

import com.dthvinh.Server.Models.Song;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SongResponseDto(
        Long id,
        String title,
        @JsonProperty("artistId") Long artistId,
        @JsonProperty("artistName") String artistName,
        @JsonProperty("spotifyId") String spotifyId,
        @JsonProperty("audioUrl") String audioUrl) {

    public static SongResponseDto from(Song song) {
        return new SongResponseDto(
                song.getId(),
                song.getTitle(),
                song.getArtistId(),
                null,
                song.getSpotifyId(),
                song.getAudioUrl());
    }

    public static SongResponseDto from(Song song, String artistName) {
        return new SongResponseDto(
                song.getId(),
                song.getTitle(),
                song.getArtistId(),
                artistName,
                song.getSpotifyId(),
                song.getAudioUrl());
    }
}
