package com.dthvinh.Server.Lib;

import com.dthvinh.Server.Lib.Kafka.KafkaRunner;
import com.dthvinh.Server.Lib.SummerBoot.SummerBootRunner;

import java.io.IOException;

public class AppRunner {
    public static void run() throws IOException {
        SummerBootRunner.run();
        KafkaRunner.run();
    }
}
