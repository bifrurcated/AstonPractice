package com.github.bufrurcated.astonpractice.db;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
@Slf4j
public class DatabaseSource {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "123";

    private final Connection connection = buildConnection();

    static {
        try {
            Class.forName(DB_DRIVER);
            log.info("Driver class loaded");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Connection buildConnection() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            log.info("Connection success");
            return conn;
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void shutdown() {
        try {
            getConnection().close();
            log.info("Close connection");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
