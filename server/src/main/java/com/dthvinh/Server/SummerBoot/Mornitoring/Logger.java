package com.dthvinh.Server.SummerBoot.Mornitoring;

public class Logger {
    private static Logger instance;

    public static Logger getInstance() {
        if (instance == null)
            instance = new Logger();

        return instance;
    }

    public void info(String message) {
        System.out.println(message);
    }

    public void error(String message) {
        System.err.println(message);
    }

    public void debug(String message) {
        System.out.println(message);
    }
}
