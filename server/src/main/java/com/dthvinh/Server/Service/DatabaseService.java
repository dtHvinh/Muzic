package com.dthvinh.Server.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {
    private static final String URL = "jdbc:postgresql://localhost:5432/muzicdb";
    private static final String USER = "postgres";
    private static final String PASS = "0";

    public static void initDb() {
        createArtistTable();
        createSongTable();
        createPlaylistTables();
    }

    private static void createArtistTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS artist (
                    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
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

    private static void createSongTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS song (
                    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
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

    private static void createPlaylistTables() {
        String playlistSql = """
        CREATE TABLE IF NOT EXISTS playlist (
            id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
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

    private static void execute(String sql, String errorMessage) {
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
                Statement s = c.createStatement()) {
            s.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }

    public static Transaction startTransaction() throws SQLException {
        Connection c = DriverManager.getConnection(URL, USER, PASS);
        return new Transaction(c, c.createStatement());
    }

    public static class Transaction implements AutoCloseable {
        public Connection connection;
        public Statement statement;

        public Transaction(Connection connection, Statement statement) {
            this.connection = connection;
            this.statement = statement;
        }

        @Override
        public void close() throws Exception {
            connection.close();
            statement.close();
        }
    }
}
