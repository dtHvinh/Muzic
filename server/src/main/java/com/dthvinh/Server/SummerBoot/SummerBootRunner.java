package com.dthvinh.Server.SummerBoot;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.dthvinh.Server.Repositories.ArtistRepository;
import com.dthvinh.Server.Repositories.PlaylistRepository;
import com.dthvinh.Server.Repositories.SongRepository;
import com.dthvinh.Server.SummerBoot.DI.ServiceContainer;
import com.dthvinh.Server.SummerBoot.Data.AppDataSource;
import com.dthvinh.Server.SummerBoot.Data.DatabaseService;
import com.dthvinh.Server.SummerBoot.Kafka.MuzicKafkaConsumerManager;
import com.dthvinh.Server.SummerBoot.Reflection.EndpointService;
import com.dthvinh.Server.SummerBoot.Utils.EnVariables;
import com.sun.net.httpserver.HttpServer;

public class SummerBootRunner {
    private static final int PORT = 8080;
    private static final MuzicKafkaConsumerManager consumerManager = new MuzicKafkaConsumerManager();

    public static void run() throws IOException {
        runServer();
        startKafkaConsumers();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumerManager.stopConsumers();
        }));
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

        ArtistRepository artistRepository = new ArtistRepository(databaseService);
        SongRepository songRepository = new SongRepository(databaseService);
        PlaylistRepository playlistRepository = new PlaylistRepository(databaseService);

        sc.register(ArtistRepository.class, artistRepository);
        sc.register(SongRepository.class, songRepository);
        sc.register(PlaylistRepository.class, playlistRepository);
    }

    private static void startKafkaConsumers() {
        String bootstrapServer = EnVariables.KAFKA_BOOTSTRAP_SERVER;
        String groupId = "muzic-consumer-group";
        String[] topics = { "song-plays" };
        int numConsumers = 1;

        consumerManager.startConsumers(bootstrapServer, groupId, topics, numConsumers);
    }
}
