package com.dthvinh.Server.Lib.Kafka.Consumer;

import com.dthvinh.Server.Endpoints.HelloEndpoint;
import com.dthvinh.Server.Lib.Kafka.Annotation.EventHandler;
import com.dthvinh.Server.Lib.Kafka.Consumer.Base.EventConsumer;
import com.dthvinh.Server.Lib.SummerBoot.Mornitoring.Logger;

import java.util.Map;

@EventHandler(sender = HelloEndpoint.class)
public class HelloEventConsumer implements EventConsumer<Map<String, String>> {
    @Override
    public void handleEvent(Map<String, String> e) {
        Logger.getInstance().info("Handle %s".formatted(e.get("name")));
    }
}
