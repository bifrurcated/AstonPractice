package com.github.bufrurcated.astonpractice.db;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Slf4j
public class DatabaseSource {

    @Getter private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            log.info("Configuration failed: ", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        sessionFactory.close();
        log.info("Close connection");
    }
}
