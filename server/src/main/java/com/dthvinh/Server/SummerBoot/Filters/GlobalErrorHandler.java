package com.dthvinh.Server.SummerBoot.Filters;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class GlobalErrorHandler extends BaseFilter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain)
            throws IOException {
        try {
            chain.doFilter(exchange);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
        }
    }

    @Override
    public String description() {
        return "Error handler";
    }
}