package com.dthvinh.Server.Models;

public record PlaylistSongEntry(
                Long songId,
                String songTitle,
                Long artistId,
                String artistName,
                String artistProfileImage,
                String spotifyId,
                String audioUrl) {
}
