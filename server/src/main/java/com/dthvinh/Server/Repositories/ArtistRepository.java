package com.dthvinh.Server.Repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dthvinh.Server.Models.Artist;
import com.dthvinh.Server.Repositories.Contract.Repository;
import com.dthvinh.Server.SummerBoot.Data.DatabaseService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArtistRepository implements Repository<Artist, Long> {
    private final DatabaseService databaseService;

    public static Artist map(ResultSet rs) throws SQLException {
        return new Artist(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("bio"),
                rs.getString("profile_image"),
                rs.getString("spotify_id"),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("updated_at", LocalDateTime.class));
    }

    public Artist save(Artist a) {
        String sql = """
                INSERT INTO artist (name, bio, profile_image, spotify_id)
                VALUES (?, ?, ?, ?)
                RETURNING *
                """;

        try (var c = databaseService.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getBio());
            ps.setString(3, a.getProfileImage());
            ps.setString(4, a.getSpotifyId());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Save failed", e);
        }
    }

    public List<Artist> findAll(String search, Integer limit, Integer offset) {
        var list = new ArrayList<Artist>();
        var sql = new StringBuilder("SELECT * FROM artist WHERE 1=1");

        var params = new ArrayList<Object>();

        if (search != null && !search.isBlank()) {
            sql.append(" AND name ILIKE ?");
            params.add("%" + search.trim() + "%");
        }

        sql.append(" ORDER BY name ASC");

        if (limit != null && limit > 0) {
            sql.append(" LIMIT ?");
            params.add(limit);
        }
        if (offset != null && offset > 0) {
            sql.append(" OFFSET ?");
            params.add(offset);
        }

        try (var c = databaseService.getConnection();
                PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("findAll failed: " + e.getMessage(), e);
        }

        return list;
    }

    public Optional<Artist> findById(Long id) {
        String sql = "SELECT * FROM artist WHERE id = ?";

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

    public Artist update(Long id, Artist a) {
        String sql = """
                UPDATE artist SET
                    name = ?, bio = ?, profile_image = ?, spotify_id = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ? RETURNING *
                """;

        try (var c = databaseService.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getBio());
            ps.setString(3, a.getProfileImage());
            ps.setString(4, a.getSpotifyId());
            ps.setLong(5, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Update failed", e);
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM artist WHERE id = ?";
        try (var c = databaseService.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Delete failed", e);
        }
    }
}
