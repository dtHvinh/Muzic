package com.dthvinh.Server.Models;

import com.dthvinh.Server.DTOs.UpdateSongDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public final class Song {
    private Long id;
    private String title;

    private Long artistId;
    private String spotifyId;
    private String audioUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Song(Long id, String title, Long artistId, String spotifyId, String audioUrl,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.spotifyId = spotifyId;
        this.audioUrl = audioUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Song(String title, Long artistId, String spotifyId, String audioUrl) {
        this(null, title, artistId, spotifyId, audioUrl, null, null);
    }

    public Song(String title, Long artistId, String spotifyId) {
        this(title, artistId, spotifyId, null);
    }

    public Song with(UpdateSongDto other) {
        return new Song(
                getId(),
                other.title() != null && !other.title().isBlank() ? other.title().trim() : getTitle(),
                other.artistId() != null ? other.artistId() : getArtistId(),
                other.spotifyId() != null && !other.spotifyId().isBlank() ? other.spotifyId().trim() : getSpotifyId(),
                other.audioUrl() != null && !other.audioUrl().isBlank() ? other.audioUrl().trim() : getAudioUrl(),
                getCreatedAt(),
                LocalDateTime.now());
    }
}
