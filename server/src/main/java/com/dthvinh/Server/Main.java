package com.dthvinh.Server;

import java.net.InetSocketAddress;

import com.dthvinh.Server.Repositories.ArtistRepository;
import com.dthvinh.Server.Repositories.PlaylistRepository;
import com.dthvinh.Server.Repositories.SongRepository;
import com.dthvinh.Server.SummerBoot.DI.ServiceContainer;
import com.dthvinh.Server.SummerBoot.Data.AppDataSource;
import com.dthvinh.Server.SummerBoot.Data.DatabaseService;
import com.dthvinh.Server.SummerBoot.Reflection.EndpointService;
import com.sun.net.httpserver.HttpServer;

public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        ServiceContainer sc = new ServiceContainer();
        registerService(sc);

        EndpointService es = new EndpointService(sc);
        es.registerEndpoints(server);

        server.setExecutor(null);
        server.start();

        System.out.println("Server running on http://localhost:" + PORT);
    }

    private static void registerService(ServiceContainer sc) {
        AppDataSource appDataSource = AppDataSource.fromEnv();

        DatabaseService databaseService = new DatabaseService(appDataSource);
        databaseService.initDb();

        ArtistRepository artistRepository = new ArtistRepository(databaseService);
        SongRepository songRepository = new SongRepository(databaseService);
        PlaylistRepository playlistRepository = new PlaylistRepository(databaseService);

        sc.register(ArtistRepository.class, artistRepository);
        sc.register(SongRepository.class, songRepository);
        sc.register(PlaylistRepository.class, playlistRepository);
    }
}
