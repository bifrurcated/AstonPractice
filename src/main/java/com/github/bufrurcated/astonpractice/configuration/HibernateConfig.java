package com.github.bufrurcated.astonpractice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Slf4j
@Configuration
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        try {
            return new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            log.info("Configuration failed: ", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

}
