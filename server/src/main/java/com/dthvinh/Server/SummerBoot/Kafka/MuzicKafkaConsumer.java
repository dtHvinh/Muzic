package com.dthvinh.Server.SummerBoot.Kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.dthvinh.Server.SummerBoot.Mornitoring.Logger;

public class MuzicKafkaConsumer implements Runnable, AutoCloseable {

    private static final Logger logger = Logger.getInstance();
    private final KafkaConsumer<String, String> consumer;
    private volatile boolean running = true;

    public MuzicKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "muzic-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // or "latest"
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList("song-plays"));
    }

    @Override
    public void run() {
        logger.info("Muzic Kafka Consumer started - listening to topic: song-plays");

        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    processRecord(record);
                }
            }
        } catch (Exception e) {
            logger.error("Error in Kafka consumer loop");
        } finally {
            consumer.close();
            logger.info("Muzic Kafka Consumer stopped");
        }
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        logger.info("Consumed message: key=%s, value=%s, partition=%s, offset=%s".formatted(
                key, value, record.partition(), record.offset()));

        // TODO: Logic here
    }

    public void shutdown() {
        running = false;
    }

    @Override
    public void close() {
        shutdown();
    }

    public static void startConsumer() {
        MuzicKafkaConsumer consumer = new MuzicKafkaConsumer();
        Thread thread = new Thread(consumer, "Muzic-Kafka-Consumer");
        thread.setDaemon(true);
        thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::shutdown));
    }
}