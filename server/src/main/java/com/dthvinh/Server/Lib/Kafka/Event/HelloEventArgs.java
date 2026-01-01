package com.dthvinh.Server.Lib.Kafka.Event;

import com.dthvinh.Server.Endpoints.HelloEndpoint;
import com.dthvinh.Server.Lib.Kafka.Event.Base.EventArgs;

public class HelloEventArgs<TData> extends EventArgs<TData> {
    public HelloEventArgs(TData tData) {
        super(HelloEndpoint.class, tData);
    }
}
