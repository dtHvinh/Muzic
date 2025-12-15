package com.dthvinh.Server;

import java.net.InetSocketAddress;

import com.dthvinh.Server.Service.DatabaseService;
import com.dthvinh.Server.SummerBoot.Reflection.EndpointService;
import com.sun.net.httpserver.HttpServer;

public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        EndpointService.registerEndpoints(server);
        DatabaseService.initDb();

        server.setExecutor(null);
        server.start();

        System.out.println("Server running on http://localhost:" + PORT);
    }
}
