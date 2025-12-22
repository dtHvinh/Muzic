package EndpointTests;

import com.dthvinh.Server.DTOs.CreatePlaylistDto;
import com.dthvinh.Server.Endpoints.PlaylistEndpoint;
import com.dthvinh.Server.Models.Playlist;
import com.dthvinh.Server.Repositories.PlaylistRepository;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistEndpointTest {

    @Spy
    @InjectMocks
    PlaylistEndpoint endpoint;

    @Mock
    PlaylistRepository repository;

    @Mock
    HttpExchange exchange;

    @BeforeEach
    void setup() throws Exception {
        when(exchange.getResponseHeaders()).thenReturn(new Headers());
        when(exchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
    }

    @Test
    void getPlaylistById_found() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");
        doReturn(Map.of("id", "1")).when(endpoint).parseQueryParams(exchange);

        when(repository.findById(1L))
                .thenReturn(Optional.of(new Playlist(1L, "P", "D", LocalDateTime.now())));

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void getPlaylistById_notFound() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");
        doReturn(Map.of("id", "1")).when(endpoint).parseQueryParams(exchange);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        endpoint.handle(exchange);

        verify(endpoint).sendNotFound(exchange);
    }

    @Test
    void getPlaylists_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");
        doReturn(Map.of("name", "", "limit", "10", "offset", "0"))
                .when(endpoint).parseQueryParams(exchange);

        when(repository.findAll("", 10, 0))
                .thenReturn(List.of(
                        new Playlist(1L, "P1", null, LocalDateTime.now()),
                        new Playlist(2L, "P2", null, LocalDateTime.now())
                ));

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void getPlaylistDetails_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");
        doReturn(Map.of("mode", "details", "id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        when(repository.findById(1L))
                .thenReturn(Optional.of(new Playlist(1L, "P", null, LocalDateTime.now())));

        when(repository.findSongsByPlaylistId(1L)).thenReturn(List.of());

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void createPlaylist_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("POST");
        doReturn(Map.of()).when(endpoint).parseQueryParams(exchange);

        CreatePlaylistDto dto = new CreatePlaylistDto("My Playlist", "desc");
        doReturn(dto).when(endpoint).parseBody(eq(exchange), eq(CreatePlaylistDto.class));

        when(repository.save(any()))
                .thenReturn(new Playlist(1L, "My Playlist", "desc", LocalDateTime.now()));

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void createPlaylist_validationFail() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("POST");
        doReturn(Map.of()).when(endpoint).parseQueryParams(exchange);
        doReturn(null).when(endpoint).parseBody(eq(exchange), eq(CreatePlaylistDto.class));

        endpoint.handle(exchange);

        verify(endpoint).sendBadRequest(eq(exchange), any());
    }

    @Test
    void addSong_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("POST");
        doReturn(Map.of(
                "mode", "add-song",
                "playlistId", "1",
                "songId", "2"
        )).when(endpoint).parseQueryParams(exchange);

        endpoint.handle(exchange);

        verify(repository).addToPlaylist(1L, 2L);
        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void removeSong_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("POST");
        doReturn(Map.of(
                "mode", "remove-song",
                "playlistId", "1",
                "songId", "2"
        )).when(endpoint).parseQueryParams(exchange);

        endpoint.handle(exchange);

        verify(repository).deleteFromPlaylist(1L, 2L);
        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void deletePlaylist_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        doReturn(Map.of("id", "1")).when(endpoint).parseQueryParams(exchange);

        when(repository.delete(1L)).thenReturn(true);

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }
}

