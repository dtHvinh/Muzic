package com.dthvinh.Server.Repositories;

import com.dthvinh.Server.Models.Artist;
import com.dthvinh.Server.Models.Playlist;
import com.dthvinh.Server.Models.PlaylistSongEntry;
import com.dthvinh.Server.Repositories.Contract.Repository;
import com.dthvinh.Server.Service.DatabaseService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class PlaylistRepository  implements Repository<Playlist, Long> {
    private static PlaylistRepository instance;

    public static PlaylistRepository getInstance() {
        if (instance == null)
            instance = new PlaylistRepository();

        return instance;
    }

    @Override
    public Playlist save(Playlist a) {
        String sql = """
            INSERT INTO playlist (name, description)
            VALUES (?, ?)
            RETURNING id
            """;

        try (DatabaseService.Transaction t = DatabaseService.startTransaction();
             PreparedStatement ps = t.connection.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getDescription());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    return new Playlist(id, a.getName(), a.getDescription(), LocalDateTime.now());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save playlist", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Playlist> findAll(String search, Integer limit, Integer offset) {
        List<Playlist> playlists = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT
            id,
            name,
            description,
            created_at
        FROM playlist
        """);

        List<Object> params = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            sql.append(" WHERE name ILIKE ?");
            params.add("%" + search.trim() + "%");
        }

        sql.append(" ORDER BY name");

        if (offset != null && offset > 0) {
            sql.append(" OFFSET ?");
            params.add(offset);
        }

        if (limit != null && limit > 0) {
            sql.append(" LIMIT ?");
            params.add(limit);
        }

        try (DatabaseService.Transaction t = DatabaseService.startTransaction();
             PreparedStatement ps = t.connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    playlists.add(new Playlist(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getObject("created_at", LocalDateTime.class)
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find playlists", e);
        }

        return playlists;
    }

    @Override
    public Optional<Playlist> findById(Long id) {
        String sql = """
                SELECT * from playlist
                WHERE id = ?
                """;

        try (DatabaseService.Transaction t = DatabaseService.startTransaction();
             PreparedStatement ps = t.connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find artist with id: " + id, e);
        }
    }

    @Override
    public Playlist update(Long id, Playlist a) {
        String sql = """
            UPDATE playlist
            SET name = ?, description = ?
            WHERE id = ?
            RETURNING created_at
            """;

        try (DatabaseService.Transaction t = DatabaseService.startTransaction();
             PreparedStatement ps = t.connection.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getDescription());
            ps.setLong(3, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
                    return new Playlist(id, a.getName(), a.getDescription(), createdAt);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update playlist", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM playlist WHERE id = ?";

        try (DatabaseService.Transaction t = DatabaseService.startTransaction();
             PreparedStatement ps = t.connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete playlist", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addToPlaylist(Long playlistId, Long songId) {
        String sql = "INSERT INTO playlist_song (playlist_id, song_id) VALUES (?, ?)";

        try (DatabaseService.Transaction t = DatabaseService.startTransaction();
             PreparedStatement ps = t.connection.prepareStatement(sql)) {

            ps.setLong(1, playlistId);
            ps.setLong(2, songId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to add song " + songId + " to playlist " + playlistId,
                    e
            );
        }
    }


    private static Playlist map(ResultSet rs) throws SQLException {
        return new Playlist(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getObject("created_at", LocalDateTime.class)
        );
    }
}
