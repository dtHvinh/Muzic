package com.dthvinh.Server.Lib.Kafka.Consumer.Base;

public interface EventConsumer<TEvent> {
    void handleEvent(TEvent e);
}
