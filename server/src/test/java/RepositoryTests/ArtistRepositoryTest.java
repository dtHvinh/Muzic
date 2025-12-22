package RepositoryTests;

import com.dthvinh.Server.Models.Artist;
import com.dthvinh.Server.Repositories.ArtistRepository;
import com.dthvinh.Server.SummerBoot.Data.DatabaseService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistRepositoryTest {

    @Mock
    DatabaseService databaseService;

    @Mock
    Connection connection;

    @Mock
    PreparedStatement ps;

    @Mock
    ResultSet rs;

    @InjectMocks
    ArtistRepository repo;

    @Test
    void findAll_noFilters_returnsList() throws Exception {
        defaultSetting();

        when(rs.next()).thenReturn(false); // no rows

        List<Artist> result = repo.findAll(null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(connection).prepareStatement(
                contains("SELECT * FROM artist WHERE 1=1 ORDER BY name ASC")
        );
    }

    @Test
    void findAll_withSearch_setsParameter() throws Exception {
        defaultSetting();

        when(rs.next()).thenReturn(false);

        repo.findAll("john", null, null);

        verify(ps).setObject(1, "%john%");
    }

    @Test
    void findAll_withLimitAndOffset_setsParametersInOrder() throws Exception {
        defaultSetting();

        when(rs.next()).thenReturn(false);

        repo.findAll(null, 10, 20);

        verify(ps).setObject(1, 10);
        verify(ps).setObject(2, 20);
    }

    @Test
    void findAll_withSearchLimitOffset_setsAllParamsCorrectly() throws Exception {
        defaultSetting();

        when(rs.next()).thenReturn(false);

        repo.findAll("john", 10, 20);

        verify(ps).setObject(1, "%john%");
        verify(ps).setObject(2, 10);
        verify(ps).setObject(3, 20);
    }

    @Test
    void findAll_mapsMultipleRows() throws Exception {
        defaultSetting();

        when(rs.next()).thenReturn(true, true, false);

        when(rs.getLong("id")).thenReturn(1L, 2L);
        when(rs.getString("name")).thenReturn("A", "B");

        List<Artist> result = repo.findAll(null, null, null);

        assertEquals(2, result.size());
    }

    @Test
    void findAll_sqlException_isWrapped() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("boom"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> repo.findAll(null, null, null)
        );

        assertTrue(ex.getMessage().contains("findAll failed"));
    }

    @Test
    void save_successfully() throws SQLException {
        defaultSetting();
        Artist a = new Artist("A", "B", "C", "D");

        repo.save(a);

        verify(ps).setString(1, "A");
        verify(ps).setString(2, "B");
        verify(ps).setString(3, "C");
        verify(ps).setString(4, "D");
    }

    @Test
    void save_throwSQLException() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("boom"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> repo.save(new Artist("A", "B", "C", "D"))
        );

        assertTrue(ex.getMessage().contains("Save failed"));
    }

    @Test
    void findById_found_returnsArtist() throws Exception {
        defaultSetting();

        when(rs.next()).thenReturn(true);

        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("name")).thenReturn("Artist");

        Optional<Artist> result = repo.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Artist", result.get().getName());

        verify(ps).setLong(1, 1L);
    }

    @Test
    void findById_notFound_returnsEmptyOptional() throws Exception {
        defaultSetting();

        when(rs.next()).thenReturn(false);

        Optional<Artist> result = repo.findById(99L);

        assertTrue(result.isEmpty());

        verify(ps).setLong(1, 99L);
    }

    @Test
    void findById_sqlException_isWrapped() throws Exception {
        when(databaseService.getConnection()).thenThrow(new SQLException("DB down"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> repo.findById(1L)
        );

        assertTrue(ex.getMessage().contains("Failed to find artist with id"));
    }

    @Test
    void update_success_returnsArtist() throws Exception {
        defaultSetting();

        Artist artist = new Artist(
                1L, "Name", "Bio", "Img", "Spotify",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("name")).thenReturn("Name");

        Artist result = repo.update(1L, artist);

        assertNotNull(result);
        assertEquals("Name", result.getName());

        verify(ps).setString(1, artist.getName());
        verify(ps).setString(2, artist.getBio());
        verify(ps).setString(3, artist.getProfileImage());
        verify(ps).setString(4, artist.getSpotifyId());
        verify(ps).setLong(5, 1L);
    }

    @Test
    void update_noRow_returnsNull() throws Exception {
        defaultSetting();

        Artist artist = new Artist(
                1L, "Name", "Bio", "Img", "Spotify",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(rs.next()).thenReturn(false);

        Artist result = repo.update(1L, artist);

        assertNull(result);
    }

    @Test
    void update_exception_wrapped() throws Exception {
        when(databaseService.getConnection()).thenThrow(new SQLException("DB error"));

        Artist artist = new Artist(
                1L, "Name", "Bio", "Img", "Spotify",
                LocalDateTime.now(), LocalDateTime.now()
        );

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> repo.update(1L, artist)
        );

        assertTrue(ex.getMessage().contains("Update failed"));
    }

    @Test
    void delete_success_returnsTrue() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);

        boolean result = repo.delete(1L);

        assertTrue(result);
        verify(ps).setLong(1, 1L);
    }

    @Test
    void delete_notFound_returnsFalse() throws Exception {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(0);

        boolean result = repo.delete(99L);

        assertFalse(result);
    }

    @Test
    void delete_exception_wrapped() throws Exception {
        when(databaseService.getConnection()).thenThrow(new SQLException("DB error"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> repo.delete(1L)
        );

        assertTrue(ex.getMessage().contains("Delete failed"));
    }

    private void defaultSetting() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
    }
}
