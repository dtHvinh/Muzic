package com.dthvinh.Server.Lib.Kafka;

import com.dthvinh.Server.Lib.Kafka.Consumer.Base.MuzicKafkaConsumerManager;
import com.dthvinh.Server.Lib.Kafka.Services.KafkaDataReader;
import com.dthvinh.Server.Lib.SummerBoot.Utils.Env;

import java.io.FileNotFoundException;
import java.util.List;

public class KafkaRunner {
    private static final MuzicKafkaConsumerManager consumerManager = new MuzicKafkaConsumerManager();

    public static void run() throws FileNotFoundException {
        startKafkaConsumers();
        Runtime.getRuntime().addShutdownHook(new Thread(consumerManager::stopConsumers));
    }

    private static void startKafkaConsumers() throws FileNotFoundException {
        String bootstrapServer = Env.KAFKA_BOOTSTRAP_SERVER;
//        String groupId = "muzic-consumer-group";
//        String[] topics = { "song-plays" };
//        int numConsumers = 1;
//
//        consumerManager.startConsumers(bootstrapServer, groupId, topics, numConsumers);

        List<KafkaDataReader.KafkaConfiguration> configurations = KafkaDataReader.loadKafkaConfigs();
        for (KafkaDataReader.KafkaConfiguration c : configurations) {
            consumerManager.startConsumers(bootstrapServer, c.groupId, c.topics, c.consumerCount);
        }
    }
}
