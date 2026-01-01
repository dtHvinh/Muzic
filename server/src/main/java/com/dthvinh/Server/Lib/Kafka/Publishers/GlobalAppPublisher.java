package com.dthvinh.Server.Lib.Kafka.Publishers;

import com.dthvinh.Server.Lib.SummerBoot.Utils.Env;

public class GlobalAppPublisher extends KafkaPublisher {
    public static final String TOPIC = "app";

    public GlobalAppPublisher() {
        super(Env.KAFKA_BOOTSTRAP_SERVER, TOPIC);
    }
}
