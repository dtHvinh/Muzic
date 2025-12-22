package RepositoryTests;

import com.dthvinh.Server.Models.Playlist;
import com.dthvinh.Server.Models.PlaylistSongEntry;
import com.dthvinh.Server.Repositories.PlaylistRepository;
import com.dthvinh.Server.SummerBoot.Data.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistRepositoryTest {

    @Mock
    DatabaseService databaseService;

    @Mock
    Connection connection;

    @Mock
    PreparedStatement ps;

    @Mock
    ResultSet rs;

    @InjectMocks
    PlaylistRepository repo;

    Playlist playlist;

    @BeforeEach
    void setup() {
        playlist = new Playlist(
                1L,
                "My Playlist",
                "Description",
                LocalDateTime.now()
        );
    }

    @Test
    void save_success() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);

        Playlist result = repo.save(playlist);

        assertNotNull(result);
        assertEquals("My Playlist", result.getName());
    }

    @Test
    void save_throwSQLException() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("boom"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> repo.save(playlist)
        );

        assertTrue(ex.getMessage().contains("Failed to save playlist"));
    }

    @Test
    void findAll_noFilter() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("name")).thenReturn("P1");
        when(rs.getString("description")).thenReturn("D1");
        when(rs.getObject("created_at", LocalDateTime.class))
                .thenReturn(LocalDateTime.now());

        List<Playlist> result = repo.findAll(null, null, null);

        assertEquals(1, result.size());
    }

    @Test
    void findAll_withSearchLimitOffset() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        List<Playlist> result = repo.findAll("rock", 10, 5);

        assertNotNull(result);
    }

    @Test
    void findById_found() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("name")).thenReturn("Playlist");
        when(rs.getString("description")).thenReturn("Desc");
        when(rs.getObject("created_at", LocalDateTime.class))
                .thenReturn(LocalDateTime.now());

        Optional<Playlist> result = repo.findById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void findById_notFound() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Optional<Playlist> result = repo.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void update_success() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        LocalDateTime createdAt = LocalDateTime.now();
        when(rs.next()).thenReturn(true);
        when(rs.getObject("created_at", LocalDateTime.class))
                .thenReturn(createdAt);

        Playlist updated = repo.update(1L, playlist);

        assertNotNull(updated);
        assertEquals(createdAt, updated.getCreatedAt());
    }

    @Test
    void update_notFound() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Playlist updated = repo.update(1L, playlist);

        assertNull(updated);
    }

    @Test
    void delete_success() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);

        assertTrue(repo.delete(1L));
    }

    @Test
    void delete_notFound() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(0);

        assertFalse(repo.delete(1L));
    }

    @Test
    void addToPlaylist_success() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);

        assertDoesNotThrow(() -> repo.addToPlaylist(1L, 2L));
        verify(ps).executeUpdate();
    }

    @Test
    void deleteFromPlaylist_success() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);

        assertDoesNotThrow(() -> repo.deleteFromPlaylist(1L, 2L));
        verify(ps).executeUpdate();
    }

    @Test
    void findSongsByPlaylistId_success() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getLong("song_id")).thenReturn(1L);
        when(rs.getString("song_title")).thenReturn("Song");
        when(rs.getLong("artist_id")).thenReturn(2L);
        when(rs.getString("artist_name")).thenReturn("Artist");
        when(rs.getString("artist_profile_image")).thenReturn("img");
        when(rs.getString("spotify_id")).thenReturn("sp");
        when(rs.getString("audio_url")).thenReturn("url");

        List<PlaylistSongEntry> result =
                repo.findSongsByPlaylistId(1L);

        assertEquals(1, result.size());
    }
}
