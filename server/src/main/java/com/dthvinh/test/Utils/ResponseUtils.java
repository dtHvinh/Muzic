/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dthvinh.test.Utils;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 *
 * @author dthvinh
 */
public class ResponseUtils {

    public static String readBody(HttpExchange exchange) throws IOException {
        try (InputStreamReader isr
                = new InputStreamReader(exchange.getRequestBody()); BufferedReader reader
                = new BufferedReader(isr)) {
            return reader.lines().reduce("", (acc, line) -> acc + line);
        }
    }

    public static void writeResponse(
            HttpExchange exchange,
            int statusCode,
            String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
