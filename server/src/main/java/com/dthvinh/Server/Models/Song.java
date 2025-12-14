package com.dthvinh.Server.Models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Song {
    private Long id;
    private String title;

    private Long artistId;
    private String spotifyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Song(Long id, String title, Long artistId, String spotifyId,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.spotifyId = spotifyId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Song(String title, Long artistId, String spotifyId) {
        this(null, title, artistId, spotifyId, null, null);
    }
}
