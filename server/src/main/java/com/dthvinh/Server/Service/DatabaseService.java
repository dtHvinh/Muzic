package com.dthvinh.Server.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {
    private static final String URL = "jdbc:postgresql://localhost:5432/muzicdb";
    private static final String USER = "postgres";
    private static final String PASS = "0";

    public static void initDb() {
        String sql = """
                CREATE TABLE IF NOT EXISTS artist (
                    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    name VARCHAR(255) NOT NULL,
                    bio TEXT,
                    profile_image VARCHAR(512),
                    spotify_id VARCHAR(64) UNIQUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             Statement s = c.createStatement()) {
            s.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create table", e);
        }
    }

    public static Transaction startTransaction() throws SQLException {
        Connection c = DriverManager.getConnection(URL, USER, PASS);
        return new Transaction(c, c.createStatement());
    }

    public static class Transaction implements AutoCloseable {
        public Connection connection;
        public Statement statement;

        public Transaction(Connection connection, Statement statement) {
            this.connection = connection;
            this.statement = statement;
        }

        @Override
        public void close() throws Exception {
            connection.close();
            statement.close();
        }
    }
}
