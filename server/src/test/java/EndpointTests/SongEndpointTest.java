package EndpointTests;

import com.dthvinh.Server.DTOs.CreateSongDto;
import com.dthvinh.Server.DTOs.UpdateSongDto;
import com.dthvinh.Server.Endpoints.SongEndpoint;
import com.dthvinh.Server.Models.Artist;
import com.dthvinh.Server.Models.Song;
import com.dthvinh.Server.Repositories.ArtistRepository;
import com.dthvinh.Server.Repositories.SongRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongEndpointTest {

    @Spy
    @InjectMocks
    SongEndpoint endpoint;

    @Mock
    SongRepository songRepository;

    @Mock
    ArtistRepository artistRepository;

    @Mock
    HttpExchange exchange;

    @BeforeEach
    void setup() {
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
    }

    @Test
    void getSongs_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");
        doReturn(Map.of("title", "", "limit", "10", "offset", "0"))
                .when(endpoint).parseQueryParams(exchange);

        Song s1 = new Song(1L, "A", 1L, null, null, null, null);
        Song s2 = new Song(2L, "B", 2L, null, null, null, null);

        when(songRepository.findAll("", 10, 0)).thenReturn(List.of(s1, s2));
        when(artistRepository.findById(1L)).thenReturn(Optional.of(new Artist("X", "", "", "")));
        when(artistRepository.findById(2L)).thenReturn(Optional.of(new Artist("Y", "", "", "")));

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void getSongById_found() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");
        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        Song song = new Song(1L, "A", 1L, null, null, null, null);
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(artistRepository.findById(1L))
                .thenReturn(Optional.of(new Artist("Artist", "", "", "")));

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void getSongById_notFound() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");
        doReturn(Map.of("id", "99"))
                .when(endpoint).parseQueryParams(exchange);

        when(songRepository.findById(99L)).thenReturn(Optional.empty());

        endpoint.handle(exchange);

        verify(endpoint).sendNotFound(exchange);
    }

    @Test
    void createSong_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("POST");

        CreateSongDto dto = new CreateSongDto("Title", 1L, null, null);
        doReturn(dto).when(endpoint).parseBody(exchange, CreateSongDto.class);

        Song saved = new Song(1L, "Title", 1L, null, null, null, null);
        when(songRepository.save(any())).thenReturn(saved);

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void createSong_validationFail() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("POST");

        CreateSongDto dto = new CreateSongDto("", null, null, null);
        doReturn(dto).when(endpoint).parseBody(exchange, CreateSongDto.class);

        endpoint.handle(exchange);

        verify(endpoint).sendBadRequest(eq(exchange), anyString());
    }

    @Test
    void updateSong_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("PUT");
        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        UpdateSongDto dto = new UpdateSongDto("New", 1L, null, null);
        doReturn(dto).when(endpoint).parseBody(exchange, UpdateSongDto.class);

        Song current = new Song(1L, "Old", 1L, null, null, null, null);
        when(songRepository.findById(1L)).thenReturn(Optional.of(current));
        when(songRepository.update(eq(1L), any())).thenReturn(current);

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void updateSong_notFound() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("PUT");
        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        when(songRepository.findById(1L)).thenReturn(Optional.empty());

        endpoint.handle(exchange);

        verify(endpoint).sendNotFound(exchange);
    }

    @Test
    void deleteSong_success() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        when(songRepository.delete(1L)).thenReturn(true);

        endpoint.handle(exchange);

        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void deleteSong_notFound() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        when(songRepository.delete(1L)).thenReturn(false);

        endpoint.handle(exchange);

        verify(endpoint).sendNotFound(exchange);
    }
}
