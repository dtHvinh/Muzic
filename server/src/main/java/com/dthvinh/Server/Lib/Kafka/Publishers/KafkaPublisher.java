package com.dthvinh.Server.Lib.Kafka.Publishers;

import com.dthvinh.Server.Lib.Kafka.Event.Base.EventArgs;
import com.dthvinh.Server.Lib.SummerBoot.Utils.Env;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaPublisher implements AutoCloseable {
    private final KafkaProducer<String, String> producer;
    private final String topic;
    private final ObjectMapper mapper;

    public KafkaPublisher(String bootstrapServers, String topic) {
        this.topic = topic;
        this.mapper = new ObjectMapper();

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5);

        this.producer = new KafkaProducer<>(props);
    }

    public KafkaPublisher(String topic) {
        this(Env.KAFKA_BOOTSTRAP_SERVER, topic);
    }

    public <T> void send(EventArgs<T> e) throws JsonProcessingException {
        ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, e.sender.getName(), mapper.writeValueAsString(e.data));

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.err.println("Kafka send failed");
                exception.printStackTrace();
            } else {
                System.out.printf(
                        "Sent to topic=%s partition=%d offset=%d%n",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset()
                );
            }
        });
    }

    @Override
    public void close() {
        producer.flush();
        producer.close();
    }
}
