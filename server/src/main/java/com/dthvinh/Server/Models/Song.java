package com.dthvinh.Server.Models;

import java.time.LocalDateTime;

import lombok.Data;

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
}
