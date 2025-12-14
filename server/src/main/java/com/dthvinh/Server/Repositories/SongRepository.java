package com.dthvinh.Server.Repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dthvinh.Server.Models.Song;
import com.dthvinh.Server.Repositories.Contract.Repository;
import com.dthvinh.Server.Service.DatabaseService;

public class SongRepository implements Repository<Song, Long> {
    private static SongRepository instance;

    public static SongRepository getInstance() {
        if (instance == null)
            instance = new SongRepository();
        return instance;
    }

    private Song map(ResultSet rs) throws SQLException {
        return new Song(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getLong("artist_id"),
                rs.getString("spotify_id"),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("updated_at", LocalDateTime.class));
    }

    @Override
    public Song save(Song s) {
        String sql = """
                INSERT INTO song (title, artist_id, spotify_id)
                VALUES (?, ?, ?)
                RETURNING *
                """;
        try (var t = DatabaseService.startTransaction();
                PreparedStatement ps = t.connection.prepareStatement(sql)) {
            ps.setString(1, s.getTitle());
            ps.setLong(2, s.getArtistId());
            ps.setString(3, s.getSpotifyId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Save song failed", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Song> findAll(String search, Integer limit, Integer offset) {
        var sql = new StringBuilder("SELECT * FROM song WHERE 1=1");
        var params = new ArrayList<Object>();
        if (search != null && !search.isBlank()) {
            sql.append(" AND title ILIKE ?");
            params.add("%" + search.trim() + "%");
        }
        sql.append(" ORDER BY created_at DESC");
        if (limit != null && limit > 0) {
            sql.append(" LIMIT ?");
            params.add(limit);
        }
        if (offset != null && offset > 0) {
            sql.append(" OFFSET ?");
            params.add(offset);
        }

        var list = new ArrayList<Song>();
        try (var t = DatabaseService.startTransaction();
                PreparedStatement ps = t.connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(map(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("findAll songs failed", e);
        }
        return list;
    }

    @Override
    public Optional<Song> findById(Long id) {
        String sql = "SELECT * FROM song WHERE id = ?";
        try (var t = DatabaseService.startTransaction();
                PreparedStatement ps = t.connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("find song by id failed", e);
        }
    }

    @Override
    public Song update(Long id, Song s) {
        String sql = """
                UPDATE song SET
                    title = ?, artist_id = ?,
                    spotify_id = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ? RETURNING *
                """;
        try (var t = DatabaseService.startTransaction();
                PreparedStatement ps = t.connection.prepareStatement(sql)) {
            ps.setString(1, s.getTitle());
            ps.setLong(2, s.getArtistId());
            ps.setString(3, s.getSpotifyId());
            ps.setLong(4, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("update song failed", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM song WHERE id = ?";
        try (var t = DatabaseService.startTransaction();
                PreparedStatement ps = t.connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("delete song failed", e);
        }
    }
}
