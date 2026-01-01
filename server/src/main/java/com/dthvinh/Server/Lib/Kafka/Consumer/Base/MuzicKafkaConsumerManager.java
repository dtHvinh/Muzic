package com.dthvinh.Server.Lib.Kafka.Consumer.Base;

import java.util.ArrayList;
import java.util.List;

public class MuzicKafkaConsumerManager {

    private final List<KafkaEventConsumerBridge> consumers = new ArrayList<>();
    private final List<Thread> consumerThreads = new ArrayList<>();
    private volatile boolean running = false;

    public synchronized void startConsumers(String bootstrapServer, String groupId, String[] topics, int numConsumers) {
        if (running) {
            return;
        }

        for (int i = 0; i < numConsumers; i++) {
            KafkaEventConsumerBridge consumer = new KafkaEventConsumerBridge(bootstrapServer, groupId, topics);
            Thread thread = new Thread(consumer, "Muzic-Kafka-Consumer-" + i);
            thread.setDaemon(true);
            thread.start();

            consumers.add(consumer);
            consumerThreads.add(thread);
        }

        running = true;
    }

    public synchronized void stopConsumers() {
        if (!running) {
            return;
        }

        for (KafkaEventConsumerBridge consumer : consumers) {
            consumer.close();
        }

        for (Thread thread : consumerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        consumers.clear();
        consumerThreads.clear();
        running = false;
    }
}