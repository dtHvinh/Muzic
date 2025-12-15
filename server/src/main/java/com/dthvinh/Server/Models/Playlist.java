package com.dthvinh.Server.Models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public final class Playlist {
    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;

    public Playlist(
            Long id,
            String name,
            String description,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Playlist(String name, String description) {
        this(null, name, description, null);
    }
}

