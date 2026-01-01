package com.dthvinh.Server.Lib.Kafka.Event.Base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventArgs<TData> {
    public Class<?> sender;
    public TData data;
}
