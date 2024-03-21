package com.github.bufrurcated.astonpractice.db;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Getter
@Slf4j
public class ConfigurationDB {

    private final SessionFactory sessionFactory = buildSessionFactory();

    private SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            log.info("Configuration failed: ", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void shutdown() {
        sessionFactory.close();
        log.info("Close connection");
    }
}
