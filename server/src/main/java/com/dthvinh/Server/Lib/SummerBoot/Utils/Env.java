package com.dthvinh.Server.Lib.SummerBoot.Utils;

public class Env {
    public static final String KAFKA_BOOTSTRAP_SERVER = System.getenv("KAFKA_BOOTSTRAP_SERVER");
    public static final String DATABASE_HOST = System.getenv("DB_HOST");
    public static final String DATABASE_PORT = System.getenv("DB_PORT");
    public static final String DATABASE_NAME = System.getenv("DB_NAME");
    public static final String DATABASE_USER = System.getenv("DB_USER");
    public static final String DATABASE_PASSWORD = System.getenv("DB_PASS");
    public static final String ENVIRONMENT = System.getenv("ENVIRONMENT");
    public static final String OPENTSDB_URL = System.getenv("OPENTSDB_URL");

    public static boolean isDevelopment() {
        return ENVIRONMENT == null || ENVIRONMENT.equalsIgnoreCase("Development");
    }
}
