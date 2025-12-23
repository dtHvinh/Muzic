package EndpointTests;

import com.dthvinh.Server.Endpoints.Base.BaseEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseEndpointTest {
    private final ObjectMapper mapper = new ObjectMapper();
    BaseEndpoint endpoint = new BaseEndpoint() {
        @Override
        public void handle(HttpExchange exchange) {
        }
    };

    @Test
    void testParseQueryParams() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);

        URI uri = new URI("http://localhost:8080/test?name=adele&country=GB");
        when(exchange.getRequestURI()).thenReturn(uri);

        BaseEndpoint endpoint = new BaseEndpoint() {
            @Override
            public void handle(HttpExchange exchange) {
            }
        };

        Map<String, String> params = endpoint.parseQueryParams(exchange);

        assertEquals(2, params.size());
        assertEquals("adele", params.get("name"));
        assertEquals("GB", params.get("country"));
    }

    @Test
    void testParseQueryParamsEmpty() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        URI uri = new URI("http://localhost:8080/test");
        when(exchange.getRequestURI()).thenReturn(uri);

        BaseEndpoint endpoint = new BaseEndpoint() {
            @Override
            public void handle(HttpExchange exchange) {
            }
        };

        Map<String, String> params = endpoint.parseQueryParams(exchange);

        assertEquals(0, params.size());
    }

    @Test
    void testParseQueryParamsWithEmptyValue() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        URI uri = new URI("http://localhost:8080/test?key=");
        when(exchange.getRequestURI()).thenReturn(uri);

        BaseEndpoint endpoint = new BaseEndpoint() {
            @Override
            public void handle(HttpExchange exchange) {
            }
        };

        Map<String, String> params = endpoint.parseQueryParams(exchange);

        assertEquals(1, params.size());
        assertEquals("", params.get("key"));
    }

    @Test
    void testParseBodyAsMap() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        String json = "{\"name\":\"adele\",\"age\":30}";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        when(exchange.getRequestBody()).thenReturn(input);

        BaseEndpoint endpoint = new BaseEndpoint() {
            @Override
            public void handle(HttpExchange exchange) {
            }
        };

        Map<String, Object> map = endpoint.parseBodyAsMap(exchange);

        assertEquals("adele", map.get("name"));
        assertEquals(30, map.get("age"));
    }

    @Test
    void testParseBodyAsMapEmpty() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        InputStream input = new ByteArrayInputStream(new byte[0]);
        when(exchange.getRequestBody()).thenReturn(input);

        BaseEndpoint endpoint = new BaseEndpoint() {
            @Override
            public void handle(HttpExchange exchange) {
            }
        };

        Map<String, Object> map = endpoint.parseBodyAsMap(exchange);
        assertTrue(map.isEmpty());
    }

    @Test
    void testParseBodyGeneric() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        String json = "{\"name\":\"adele\",\"age\":30}";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        when(exchange.getRequestBody()).thenReturn(input);

        Person person = endpoint.parseBody(exchange, Person.class);

        assertNotNull(person);
        assertEquals("adele", person.name);
        assertEquals(30, person.age);
    }

    @Test
    void testGetQuery() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        URI uri = new URI("http://localhost/test?name=adele&country=GB");
        when(exchange.getRequestURI()).thenReturn(uri);

        BaseEndpoint endpoint = new BaseEndpoint() {
            @Override
            public void handle(HttpExchange exchange) {
            }
        };

        assertEquals("adele", endpoint.getQuery(exchange, "name"));
        assertEquals("GB", endpoint.getQuery(exchange, "country"));
        assertNull(endpoint.getQuery(exchange, "nonexistent"));
    }

    @Test
    void testHttpVerbs() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        BaseEndpoint endpoint = new BaseEndpoint() {
            @Override
            public void handle(HttpExchange exchange) {
            }
        };

        when(exchange.getRequestMethod()).thenReturn("GET");
        assertTrue(endpoint.isGet(exchange));
        assertFalse(endpoint.isPost(exchange));

        when(exchange.getRequestMethod()).thenReturn("POST");
        assertTrue(endpoint.isPost(exchange));
        assertFalse(endpoint.isGet(exchange));

        when(exchange.getRequestMethod()).thenReturn("PUT");
        assertTrue(endpoint.isPut(exchange));

        when(exchange.getRequestMethod()).thenReturn("DELETE");
        assertTrue(endpoint.isDelete(exchange));

        when(exchange.getRequestMethod()).thenReturn("OPTIONS");
        assertTrue(endpoint.isOptions(exchange));
    }

    static class Person {
        public String name;
        public int age;
    }
}

