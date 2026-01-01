package com.dthvinh.Server.Repositories;

import com.dthvinh.Server.Models.Playlist;
import com.dthvinh.Server.Models.PlaylistSongEntry;
import com.dthvinh.Server.Repositories.Contract.Repository;
import com.dthvinh.Server.Lib.SummerBoot.Data.DatabaseService;
import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PlaylistRepository implements Repository<Playlist, Long> {
    private final DatabaseService databaseService;

    private static Playlist map(ResultSet rs) throws SQLException {
        return new Playlist(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getObject("created_at", LocalDateTime.class));
    }

    @Override
    public Playlist save(Playlist a) {
        String sql = """
                INSERT INTO playlist (name, description)
                VALUES (?, ?)
                RETURNING id
                """;

        try (var c = databaseService.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

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

        try (var c = databaseService.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    playlists.add(new Playlist(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getObject("created_at", LocalDateTime.class)));
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

        try (var c = databaseService.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

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

        try (var c = databaseService.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

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

        try (var c = databaseService.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

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

        try (var c = databaseService.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, playlistId);
            ps.setLong(2, songId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to add song " + songId + " to playlist " + playlistId,
                    e);
        }
    }

    public void deleteFromPlaylist(Long playlistId, Long songId) {
        String sql = "DELETE FROM playlist_song WHERE playlist_id = ? AND song_id = ?";

        try (var c = databaseService.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, playlistId);
            ps.setLong(2, songId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to remove song " + songId + " from playlist " + playlistId,
                    e
            );
        }
    }

    public List<PlaylistSongEntry> findSongsByPlaylistId(Long playlistId) {
        String sql = """
                SELECT
                    s.id AS song_id,
                    s.title AS song_title,
                    s.artist_id AS artist_id,
                    a.name AS artist_name,
                    a.profile_image AS artist_profile_image,
                    s.spotify_id AS spotify_id,
                    s.audio_url AS audio_url
                FROM playlist_song ps
                JOIN song s ON ps.song_id = s.id
                LEFT JOIN artist a ON s.artist_id = a.id
                WHERE ps.playlist_id = ?
                ORDER BY s.created_at DESC
                """;

        List<PlaylistSongEntry> result = new ArrayList<>();
        try (var c = databaseService.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, playlistId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new PlaylistSongEntry(
                            rs.getLong("song_id"),
                            rs.getString("song_title"),
                            rs.getLong("artist_id"),
                            rs.getString("artist_name"),
                            rs.getString("artist_profile_image"),
                            rs.getString("spotify_id"),
                            rs.getString("audio_url")));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find songs for playlist id: " + playlistId, e);
        }

        return result;
    }
}
