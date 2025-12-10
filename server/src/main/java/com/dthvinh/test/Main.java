package com.dthvinh.test;

import com.dthvinh.test.Reflection.EndpointService;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        EndpointService.registerEndpoints(server);
        server.setExecutor(null);
        server.start();
        System.out.println("Server running on http://localhost:" + PORT);
    }
}
