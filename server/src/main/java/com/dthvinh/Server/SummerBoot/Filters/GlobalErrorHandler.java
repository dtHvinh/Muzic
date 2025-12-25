package com.dthvinh.Server.SummerBoot.Filters;

import java.io.IOException;
import java.util.Map;

import com.dthvinh.Server.Utils.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;

public class GlobalErrorHandler extends BaseFilter {
    protected ObjectWriter writer = new ObjectMapper()
            .findAndRegisterModules()
            .writer()
            .withDefaultPrettyPrinter();

    @Override
    public void doFilter(HttpExchange exchange, Chain chain)
            throws IOException {
        try {
            chain.doFilter(exchange);
        } catch (Exception e) {
            ResponseUtils.writeResponse(exchange, 500, String.valueOf(writer.writeValueAsString(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()))));
            exchange.close();
        }
    }

    @Override
    public String description() {
        return "Error handler";
    }
}