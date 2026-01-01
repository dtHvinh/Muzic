package com.dthvinh.Server.Lib.Kafka.Consumer.Base;

import com.dthvinh.Server.Lib.Kafka.Annotation.EventHandler;
import com.dthvinh.Server.Lib.SummerBoot.Mornitoring.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.reflections.Reflections;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaEventConsumerBridge implements Runnable, AutoCloseable {

    private static final Logger logger = Logger.getInstance();
    private final KafkaConsumer<String, String> consumer;
    private final Map<Class<?>, EventConsumer<?>> eventHandlers = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private volatile boolean running = true;

    public KafkaEventConsumerBridge(String bootstrapServer, String groupId, String[] topics) {
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

        registerEventHandlers();
    }

    private void registerEventHandlers() {
        logger.info("Scanning for @EventHandler annotated consumers...");

        Reflections reflections = new Reflections(
                "com.dthvinh.Server.Lib.Kafka.Consumer"
        );

        Set<Class<?>> handlerClasses = reflections.getTypesAnnotatedWith(EventHandler.class);

        for (Class<?> clazz : handlerClasses) {
            if (!EventConsumer.class.isAssignableFrom(clazz)) {
                logger.info("Class %s is annotated with @EventHandler but does not implement EventConsumer".formatted(clazz.getName()));
                continue;
            }

            EventHandler annotation = clazz.getAnnotation(EventHandler.class);
            Class<?> sender = annotation.sender();

            if (sender == void.class || sender == Object.class) {
                logger.info("Invalid sender in @EventHandler on %s".formatted(clazz.getName()));
                continue;
            }

            try {
                EventConsumer<?> handler = (EventConsumer<?>) clazz.getDeclaredConstructor().newInstance();
                EventConsumer<?> previous = eventHandlers.put(sender, handler);

                if (previous != null) {
                    logger.info("Multiple handlers found for sender %s. Last one wins: %s".formatted(
                            sender.getSimpleName(), clazz.getSimpleName()));
                }

                logger.info("Registered handler %s for sender %s".formatted(clazz.getSimpleName(), sender.getSimpleName()));
            } catch (Exception e) {
                logger.error("Failed to instantiate event handler %s: %s".formatted(
                        clazz.getName(), e.getMessage()));
            }
        }

        if (eventHandlers.isEmpty()) {
            logger.info("No valid @EventHandler annotated consumers found!");
        } else {
            logger.info(String.format("Found %s event handler(s)", eventHandlers.size()));
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    processRecord(record);
                }
            }
        } catch (Exception e) {
            logger.error("Error in Kafka consumer loop: " + e.getMessage());
        } finally {
            consumer.close();
            logger.info("Muzic Kafka Consumer stopped");
        }
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        if (value == null || value.isBlank()) {
            logger.info("Received empty/null value from topic");
            return;
        }

        try {
            Object data = mapper.readValue(value, Object.class);

            @SuppressWarnings("unchecked")
            EventConsumer<Object> handler = (EventConsumer<Object>) eventHandlers.get(Class.forName(key));

            if (handler == null) {
                logger.info("No handler registered for sender: {}");
                return;
            }

            handler.handleEvent(data);

            logger.info("Processed event from sender: {}, data: {}");

        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize Kafka message: {}");
        } catch (ClassCastException e) {
            logger.error("Type mismatch for event handler: {}");
        } catch (Exception e) {
            logger.error("Unexpected error processing record: {}");
        }
    }

    public void shutdown() {
        running = false;
    }

    @Override
    public void close() {
        shutdown();
    }
}