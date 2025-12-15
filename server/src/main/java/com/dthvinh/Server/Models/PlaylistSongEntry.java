package com.dthvinh.Server.Models;

import java.time.LocalDateTime;

public record PlaylistSongEntry(
        Long songId,
        String songTitle,
        Long artistId,
        String artistName,
        String artistProfileImage
) {}
