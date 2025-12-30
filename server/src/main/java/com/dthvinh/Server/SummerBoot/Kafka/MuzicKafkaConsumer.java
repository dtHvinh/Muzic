package com.dthvinh.Server.SummerBoot.Kafka;

import java.time.Duration;
import java.util.Arrays;
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

    public MuzicKafkaConsumer(String bootstrapServer, String groupId, String[] topics) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Arrays.asList(topics));
    }

    @Override
    public void run() {
        logger.info("Muzic Kafka Consumer started");

        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                // If this stays empty forever, you're likely not getting partition assignments.
                if (records.isEmpty()) {
                    logger.info("No message (assigned=%s)".formatted(consumer.assignment()));
                    continue;
                }

                logger.info("Polled %d record(s) (assigned=%s)".formatted(records.count(), consumer.assignment()));

                for (ConsumerRecord<String, String> record : records) {
                    processRecord(record);
                }
            }
        } catch (Exception e) {
            logger.error("Error in Kafka consumer loop: " + e.getMessage());
            throw e;
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
    }

    public void shutdown() {
        running = false;
    }

    @Override
    public void close() {
        shutdown();
    }
}