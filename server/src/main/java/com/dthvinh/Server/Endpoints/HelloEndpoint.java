/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dthvinh.Server.Endpoints;

import com.dthvinh.Server.Endpoints.Base.BaseEndpoint;
import com.dthvinh.Server.SummerBoot.Anotations.Endpoint;
import com.dthvinh.Server.SummerBoot.Mornitoring.Logger;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 *
 * @author dthvinh
 */
@Endpoint(route = "hello")
public class HelloEndpoint extends BaseEndpoint {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Logger.getInstance().Info("Hello endpoint");

        String json = """
                {
                    "message": "Hello from helloEndpoint",
                    "status": "ok",
                    "timestamp": "%s"
                }
                """.formatted(java.time.Instant.now());

        sendOk(exchange, json);
    }
}
