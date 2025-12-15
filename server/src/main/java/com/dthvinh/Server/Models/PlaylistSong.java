package com.dthvinh.Server.Models;

public final class PlaylistSong {
    private final Long playlistId;
    private final Long songId;

    public PlaylistSong(long playlistId, long songId){
        this.playlistId = playlistId;
        this.songId = songId;
    }
}
