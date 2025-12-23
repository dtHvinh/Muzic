package SummerBootTests.Security;


import com.dthvinh.Server.SummerBoot.Security.Cors;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CorsTest {
    @Test
    void applyHeaders_shouldAddCorsHeaders() {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();

        when(exchange.getResponseHeaders()).thenReturn(headers);

        Cors.applyHeaders(exchange);

        assertEquals("*", headers.getFirst("Access-Control-Allow-Origin"));
        assertTrue(headers.getFirst("Access-Control-Allow-Methods").contains("POST"));
        assertTrue(headers.getFirst("Access-Control-Allow-Headers").contains("Authorization"));
    }

    @Test
    void withCors_optionsRequest_shouldReturn204AndNotCallHandler() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();

        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestMethod()).thenReturn("OPTIONS");

        HttpHandler handler = mock(HttpHandler.class);
        HttpHandler wrapped = Cors.withCors(handler);

        wrapped.handle(exchange);

        verify(exchange).sendResponseHeaders(204, -1);
        verify(handler, never()).handle(any());
    }

    @Test
    void withCors_nonOptionsRequest_shouldDelegateToHandler() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();

        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestMethod()).thenReturn("POST");

        HttpHandler handler = mock(HttpHandler.class);
        HttpHandler wrapped = Cors.withCors(handler);

        wrapped.handle(exchange);

        verify(handler).handle(exchange);
    }
}
