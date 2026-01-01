package com.dthvinh.Server.Lib.SummerBoot.Data;

import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
public class DatabaseService {
    private final DataSource dataSource;

    public void initDb() {
        createArtistTable();
        createSongTable();
        createPlaylistTables();
    }

    private void createArtistTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS artist (
                    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    bio TEXT,
                    profile_image VARCHAR(512),
                    spotify_id VARCHAR(64) UNIQUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        execute(sql, "Failed to create artist table");
    }

    private void createSongTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS song (
                    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    artist_id BIGINT NOT NULL,
                    spotify_id VARCHAR(64) UNIQUE,
                    audio_url VARCHAR(512),
                    duration_ms INTEGER,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                
                    CONSTRAINT fk_song_artist
                        FOREIGN KEY (artist_id) REFERENCES artist(id)
                        ON DELETE CASCADE
                )
                """;

        execute(sql, "Failed to create song table");
    }

    private void createPlaylistTables() {
        String playlistSql = """
                CREATE TABLE IF NOT EXISTS playlist (
                    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        String playlistSongSql = """
                CREATE TABLE IF NOT EXISTS playlist_song (
                    playlist_id BIGINT NOT NULL REFERENCES playlist(id) ON DELETE CASCADE,
                    song_id BIGINT NOT NULL REFERENCES song(id) ON DELETE CASCADE,
                    PRIMARY KEY (playlist_id, song_id)
                )
                """;

        String indexSql = "CREATE INDEX IF NOT EXISTS idx_playlist_song_playlist ON playlist_song(playlist_id)";

        execute(playlistSql, "Failed to create playlist table");
        execute(playlistSongSql, "Failed to create playlist_song table");
        execute(indexSql, "Failed to create index");
    }

    private void execute(String sql, String errorMessage) {
        try (Connection c = dataSource.getConnection();
             Statement s = c.createStatement()) {
            s.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
