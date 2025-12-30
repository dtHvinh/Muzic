package com.dthvinh.Server.SummerBoot.Filters;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class PerformanceMeter extends BaseFilter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain)
            throws IOException {

        long start = System.nanoTime();

        try {
            chain.doFilter(exchange);
        } finally {
            long durationMs = (System.nanoTime() - start) / 1_000_000;

            logger.info(
                    "%s - %s took %dms".formatted(exchange.getRequestMethod(), exchange.getRequestURI(), durationMs));
        }
    }

    @Override
    public String description() {
        return "Timing filter";
    }
}