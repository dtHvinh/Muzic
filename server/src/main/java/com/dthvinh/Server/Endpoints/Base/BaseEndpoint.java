package com.dthvinh.Server.Endpoints.Base;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.dthvinh.Server.SummerBoot.Mornitoring.Logger;
import com.dthvinh.Server.Utils.ResponseUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 *
 * @author dthvinh
 */
public abstract class BaseEndpoint implements HttpHandler {
    protected Logger logger = Logger.getInstance();
    protected ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    protected ObjectWriter writer = new ObjectMapper()
            .findAndRegisterModules()
            .writer()
            .withDefaultPrettyPrinter();

    public void sendOk(HttpExchange ex, Object response) throws IOException {
        ResponseUtils.writeResponse(ex, 200, String.valueOf(writer.writeValueAsString(response)));
    }

    public void sendBadRequest(HttpExchange ex, String message) throws IOException {
        ResponseUtils.writeResponse(ex, 400,
                """
                        {
                           "reason": "%s"
                        }
                        """.formatted(message));
    }

    public void sendNotFound(HttpExchange ex) throws IOException {
        ResponseUtils.writeResponse(ex, 404, "");
    }

    public Map<String, String> parseQueryParams(HttpExchange exchange) {
        String query = exchange.getRequestURI().getRawQuery(); // name=adele&country=GB
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> params = new HashMap<>();
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }
        return params;
    }

    public Map<String, Object> parseBodyAsMap(HttpExchange exchange) throws IOException {
        try (InputStream in = exchange.getRequestBody()) {
            if (in.available() == 0) {
                return Map.of();
            }

            return mapper.readValue(in, new TypeReference<Map<String, Object>>() {
            });
        }
    }

    public <T> T parseBody(HttpExchange exchange, Class<T> clazz) throws IOException {
        try (var in = exchange.getRequestBody()) {
            String body = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            return mapper.readValue(body, clazz);
        } catch (Exception ex) {
            return null;
        }
    }

    public String getQuery(HttpExchange exchange, String key) {
        return parseQueryParams(exchange).getOrDefault(key, null);
    }

    public Verb getVerb(HttpExchange ex) {
        return Verb.valueOf(ex.getRequestMethod().toUpperCase());
    }

    public boolean isGet(HttpExchange ex) {
        return getVerb(ex) == Verb.GET;
    }

    public boolean isPost(HttpExchange ex) {
        return getVerb(ex) == Verb.POST;
    }

    public boolean isPut(HttpExchange ex) {
        return getVerb(ex) == Verb.PUT;
    }

    public boolean isDelete(HttpExchange ex) {
        return getVerb(ex) == Verb.DELETE;
    }

    public boolean isOptions(HttpExchange ex) {
        return getVerb(ex) == Verb.OPTIONS;
    }

    public enum Verb {
        POST,
        PUT,
        DELETE,
        OPTIONS,
        GET
    }
}
