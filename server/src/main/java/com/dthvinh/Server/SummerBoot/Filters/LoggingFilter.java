package com.dthvinh.Server.SummerBoot.Filters;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LoggingFilter extends BaseFilter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        logger.Info("Request at endpoint: %s".formatted(
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
