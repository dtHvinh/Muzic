package com.dthvinh.Server.Lib.SummerBoot;

import com.dthvinh.Server.Lib.SummerBoot.DI.ServiceContainer;
import com.dthvinh.Server.Lib.SummerBoot.Data.AppDataSource;
import com.dthvinh.Server.Lib.SummerBoot.Data.DatabaseService;
import com.dthvinh.Server.Lib.SummerBoot.Reflection.EndpointService;
import com.dthvinh.Server.Lib.SummerBoot.Utils.Env;
import com.dthvinh.Server.Repositories.ArtistRepository;
import com.dthvinh.Server.Repositories.PlaylistRepository;
import com.dthvinh.Server.Repositories.SongRepository;
import com.dthvinh.Server.Service.MetricService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SummerBootRunner {
    private static final int PORT = 8080;

    public static void run() throws IOException {
        runServer();
    }

    private static void runServer() throws IOException {
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

        MetricService metricService = new MetricService(Env.OPENTSDB_URL);
        sc.register(MetricService.class, metricService);

        ArtistRepository artistRepository = new ArtistRepository(databaseService);
        SongRepository songRepository = new SongRepository(databaseService);
        PlaylistRepository playlistRepository = new PlaylistRepository(databaseService);

        sc.register(ArtistRepository.class, artistRepository);
        sc.register(SongRepository.class, songRepository);
        sc.register(PlaylistRepository.class, playlistRepository);
    }

}
