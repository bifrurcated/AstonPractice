package com.github.bufrurcated.astonpractice.dao;

import java.sql.Connection;

public class AbstractDao {
    protected final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }
}
