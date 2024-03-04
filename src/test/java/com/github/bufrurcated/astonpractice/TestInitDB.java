package com.github.bufrurcated.astonpractice;

import com.github.bufrurcated.astonpractice.db.DatabaseSource;
import com.github.bufrurcated.astonpractice.utils.InitData;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestInitDB {

    private static DatabaseSource databaseSource;

    @SneakyThrows
    @BeforeAll
    static void init() {
        databaseSource = new DatabaseSource();
        InitData.initialization(databaseSource.getConnection());
    }

    @SneakyThrows
    @AfterAll
    static void close() {
        InitData.clear(databaseSource.getConnection());
        databaseSource.shutdown();
    }

    @Test
    public void testServlet() throws Exception {

    }
}
