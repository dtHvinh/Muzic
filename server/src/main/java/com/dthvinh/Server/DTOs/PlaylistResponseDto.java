package com.dthvinh.Server.DTOs;

import com.dthvinh.Server.Models.Playlist;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PlaylistResponseDto(
        Long id,
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime createdAt
) {
    public static PlaylistResponseDto from(Playlist playlist) {
        return new PlaylistResponseDto(
                playlist.getId(),
                playlist.getName(),
                playlist.getDescription(),
                playlist.getCreatedAt()
        );
    }
}
