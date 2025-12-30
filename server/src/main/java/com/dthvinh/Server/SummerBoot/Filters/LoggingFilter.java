package com.dthvinh.Server.SummerBoot.Filters;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class LoggingFilter extends BaseFilter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        logger.info("Request at endpoint: %s".formatted(
                exchange.getRequestURI()
                        .toString()
                        .substring(0, exchange.getRequestURI().toString().indexOf('?'))));

        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "Log request";
    }
}
