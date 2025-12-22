package EndpointTests;

import com.dthvinh.Server.DTOs.CreateArtistDto;
import com.dthvinh.Server.DTOs.UpdateArtistDto;
import com.dthvinh.Server.Endpoints.ArtistEndpoint;
import com.dthvinh.Server.Models.Artist;
import com.dthvinh.Server.Repositories.ArtistRepository;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistEndpointTest {

    @Mock
    ArtistRepository repository;

    @Mock
    HttpExchange exchange;

    @Spy
    @InjectMocks
    ArtistEndpoint endpoint;

    @Test
    void shouldGetAllArtists() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");

        doReturn(Map.of(
                "query", "",
                "limit", "10",
                "offset", "0"
        )).when(endpoint).parseQueryParams(exchange);

        when(repository.findAll("", 10, 0))
                .thenReturn(List.of(
                        new Artist("A", "A1", "A2", "A3"),
                        new Artist("B", "B1", "B2", "B3")
                ));

        doNothing().when(endpoint).sendOk(any(), any());

        endpoint.handle(exchange);

        verify(repository).findAll("", 10, 0);
        verify(endpoint).sendOk(eq(exchange), any());

    }

    @Test
    void shouldGetArtistById() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");

        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        when(repository.findById(1L))
                .thenReturn(Optional.of(new Artist("A", "A1", "A2", "A3")));

        doNothing().when(endpoint).sendOk(any(), any());

        endpoint.handle(exchange);

        verify(repository).findById(1L);
        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void shouldReturn404WhenArtistNotFound() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("GET");

        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        doNothing().when(endpoint).sendNotFound(exchange);

        endpoint.handle(exchange);

        verify(endpoint).sendNotFound(exchange);
        verify(endpoint, never()).sendOk(any(), any());
    }

    @Test
    void shouldCreateArtist() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("POST");

        CreateArtistDto dto =
                new CreateArtistDto("A", "A1", "A2", "A3");

        Artist saved = new Artist("A", "A1", "A2", "A3");
        saved.setId(1L);

        doReturn(dto).when(endpoint).parseBody(exchange, CreateArtistDto.class);
        when(repository.save(any())).thenReturn(saved);
        doNothing().when(endpoint).sendOk(any(), any());

        endpoint.handle(exchange);

        verify(repository).save(any());
        verify(endpoint).sendOk(eq(exchange), any());
    }


    @Test
    void shouldDeleteArtist() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("DELETE");

        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        when(repository.delete(1L)).thenReturn(true);
        doNothing().when(endpoint).sendOk(any(), any());

        endpoint.handle(exchange);

        verify(repository).delete(1L);
        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void shouldReturn404WhenDeleteFails() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("DELETE");

        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        when(repository.delete(1L)).thenReturn(false);
        doNothing().when(endpoint).sendNotFound(exchange);

        endpoint.handle(exchange);

        verify(endpoint).sendNotFound(exchange);
    }

    @Test
    void shouldUpdateArtist() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("PUT");

        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        UpdateArtistDto dto =
                new UpdateArtistDto("A-updated", "A1", "A2", "A3");

        Artist existing = new Artist("A", "A1", "A2", "A3");
        Artist updated = new Artist("A-updated", "A1", "A2", "A3");

        doReturn(dto).when(endpoint).parseBody(exchange, UpdateArtistDto.class);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.update(eq(1L), any())).thenReturn(updated);
        doNothing().when(endpoint).sendOk(any(), any());

        endpoint.handle(exchange);

        verify(repository).update(eq(1L), any());
        verify(endpoint).sendOk(eq(exchange), any());
    }

    @Test
    void shouldReturn404WhenUpdateArtistNotFound() throws Exception {
        when(exchange.getRequestMethod()).thenReturn("PUT");

        doReturn(Map.of("id", "1"))
                .when(endpoint).parseQueryParams(exchange);

        UpdateArtistDto dto =
                new UpdateArtistDto("A", "A1", "A2", "A3");

        doReturn(dto).when(endpoint).parseBody(exchange, UpdateArtistDto.class);
        when(repository.findById(1L)).thenReturn(Optional.empty());
        doNothing().when(endpoint).sendNotFound(exchange);

        endpoint.handle(exchange);

        verify(endpoint).sendNotFound(exchange);
        verify(repository, never()).update(any(), any());
    }

}
