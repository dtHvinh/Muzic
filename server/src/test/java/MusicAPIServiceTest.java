import com.dthvinh.Server.Service.MusicAPIService;
import com.dthvinh.Server.Utils.Types.ArtistSearchPaginationResponse;
import com.dthvinh.Server.Utils.Types.Base.PaginationParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MusicAPIServiceTest {

    private MusicAPIService service;
    private HttpClient mockClient;
    private HttpResponse<String> mockResponse;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        // Mock dependencies
        mockClient = mock(HttpClient.class);
        mockResponse = mock(HttpResponse.class);

        // Create service but replace internal client using reflection (since client is private final)
        service = new MusicAPIService();

        // Use reflection to set the private final HttpClient field
        try {
            var field = MusicAPIService.class.getDeclaredField("client");
            field.setAccessible(true);
            field.set(service, mockClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindArtistByName() throws IOException, InterruptedException {
        // Sample JSON returned from MusicBrainz
        String fakeJson = """
        {
            "created": "2025-01-01T00:00:00Z",
            "count": 1,
            "offset": 0,
            "artists": [
                {
                    "id": "123",
                    "name": "Test Artist",
                    "sort_name": "Artist, Test"
                }
            ]
        }
        """;

        // Mock response
        when(mockResponse.body()).thenReturn(fakeJson);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Call method
        PaginationParams params =  PaginationParams.create(0, 10);
        ArtistSearchPaginationResponse result =
                service.findArtistByName("test", params);

        // Validate parsing
        assertNotNull(result);
        assertEquals(1, result.count);
        assertEquals(1, result.artists.size());
        assertEquals("Test Artist", result.artists.get(0).name);

        // Check the requested URL is correct
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(mockClient).send(requestCaptor.capture(), any());

        HttpRequest sentRequest = requestCaptor.getValue();

        // Ensure URL contains expected parts
        String url = sentRequest.uri().toString();
        assertTrue(url.contains("https://musicbrainz.org/ws/2/"));
        assertTrue(url.contains("artist"));
        assertTrue(url.contains("query=test"));
        assertTrue(url.contains("offset=0"));
        assertTrue(url.contains("limit=10"));
    }
}
