package RepositoryTests;

import com.dthvinh.Server.Models.Song;
import com.dthvinh.Server.Repositories.SongRepository;
import com.dthvinh.Server.Lib.SummerBoot.Data.DatabaseService;
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
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongRepositoryTest {

    @Mock
    DatabaseService databaseService;

    @Mock
    Connection connection;

    @Mock
    PreparedStatement ps;

    @Mock
    ResultSet rs;

    @InjectMocks
    SongRepository repo;

    Song song;

    @BeforeEach
    void setup() {
        song = new Song(
                1L,
                "My Song",
                10L,
                "spotify-id",
                "audio-url",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void save_success() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        mockSongRow();

        Song result = repo.save(song);

        assertNotNull(result);
        assertEquals("My Song", result.getTitle());
    }

    @Test
    void save_returnsNull_whenNoRowReturned() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        Song result = repo.save(song);

        assertNull(result);
    }

    @Test
    void findAll_noFilter() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        mockSongRow();

        List<Song> result = repo.findAll(null, null, null);

        assertEquals(1, result.size());
    }

    @Test
    void findAll_withSearchLimitOffset() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        List<Song> result = repo.findAll("rock", 10, 5);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_found() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        mockSongRow();

        Optional<Song> result = repo.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("My Song", result.get().getTitle());
    }

    @Test
    void findById_notFound() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        Optional<Song> result = repo.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void update_success() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        mockSongRow();

        Song updated = repo.update(1L, song);

        assertNotNull(updated);
        assertEquals("My Song", updated.getTitle());
    }

    @Test
    void update_notFound() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(false);

        Song updated = repo.update(1L, song);

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

    private void mockSongRow() throws SQLException {
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("title")).thenReturn("My Song");
        when(rs.getLong("artist_id")).thenReturn(10L);
        when(rs.getString("spotify_id")).thenReturn("spotify-id");
        when(rs.getString("audio_url")).thenReturn("audio-url");
        when(rs.getObject("created_at", LocalDateTime.class))
                .thenReturn(LocalDateTime.now());
        when(rs.getObject("updated_at", LocalDateTime.class))
                .thenReturn(LocalDateTime.now());
    }
}
