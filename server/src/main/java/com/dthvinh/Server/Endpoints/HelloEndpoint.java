/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dthvinh.Server.Endpoints;

import com.dthvinh.Server.Endpoints.Base.BaseEndpoint;
import com.dthvinh.Server.Lib.Kafka.Event.HelloEventArgs;
import com.dthvinh.Server.Lib.Kafka.Publishers.GlobalAppPublisher;
import com.dthvinh.Server.Lib.Kafka.Publishers.KafkaPublisher;
import com.dthvinh.Server.Lib.SummerBoot.Anotations.Endpoint;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author dthvinh
 */
@Endpoint(route = "hello")
public class HelloEndpoint extends BaseEndpoint {
    public final KafkaPublisher publisher = new GlobalAppPublisher();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = parseQueryParams(exchange);

        logger.info("Hello endpoint");
        if (queryParams.get("mode") != null && queryParams.get("mode").equals("test")) {
            logger.info("Test mode activated");
            sendOk(exchange, Map.of("status", "success", "data", "This is a test response"));
            return;
        } else if (queryParams.get("mode") != null && queryParams.get("mode").equals("exception")) {
            logger.info("Exception mode activated");
            throw new IOException("Test exception from HelloEndpoint");
        }
        sendOk(exchange, Map.of("message", "Hello, World!"));

        HelloEventArgs<Map<String, String>> e = new HelloEventArgs<>(Map.of("name", "John"));
        publisher.send(e);
    }
}
