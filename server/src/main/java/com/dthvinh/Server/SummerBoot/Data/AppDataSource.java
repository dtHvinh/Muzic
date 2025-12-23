package com.dthvinh.Server.SummerBoot.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

public class AppDataSource implements DataSource {

    private final String url;
    private final String user;
    private final String pass;

    public AppDataSource(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public static AppDataSource fromEnv() {
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);

        return new AppDataSource(url, user, pass);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iFace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iFace) {
        return false;
    }

    @Override
    public java.io.PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(java.io.PrintWriter out) {
    }

    @Override
    public int getLoginTimeout() {
        return 0;
    }

    @Override
    public void setLoginTimeout(int seconds) {
    }

    @Override
    public java.util.logging.Logger getParentLogger() {
        throw new UnsupportedOperationException();
    }
}