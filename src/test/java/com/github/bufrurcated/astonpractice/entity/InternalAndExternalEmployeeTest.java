package com.github.bufrurcated.astonpractice.entity;

import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InternalAndExternalEmployeeTest {
    private ConfigurationDB configuration;

    @SneakyThrows
    @BeforeEach
    void init() {
        configuration = new ConfigurationDB();
    }

    @AfterEach
    void clear() {
        configuration.shutdown();
    }

    @Test
    void testExternalEmployeeAddAndGet() {
        try (var session = configuration.getSessionFactory().openSession()) {
            session.beginTransaction();
            var externalEmployee = ExternalEmployee.EEBuilder()
                    .firstName("Neik")
                    .lastName("Phdw")
                    .age(29)
                    .country("Japan")
                    .city("Tokyo")
                    .street("Y. Haiko")
                    .streetNumber(632)
                    .build();
            session.persist(externalEmployee);
            session.getTransaction().commit();
        }
        try (var session = configuration.getSessionFactory().openSession()) {
            var result = session.get(ExternalEmployee.class, 1L);
            var expected = ExternalEmployee.EEBuilder()
                    .id(1L)
                    .firstName("Neik")
                    .lastName("Phdw")
                    .age(29)
                    .country("Japan")
                    .city("Tokyo")
                    .street("Y. Haiko")
                    .streetNumber(632)
                    .build();
            assertEquals(expected, result);
        }
    }

    @Test
    void testInternalEmployeeAddAndGet() {
        try (var session = configuration.getSessionFactory().openSession()) {
            session.beginTransaction();
            var internalEmployee = InternalEmployee.IEBuilder()
                    .firstName("Neik")
                    .lastName("Phdw")
                    .age(29)
                    .city("Moscow")
                    .street("A. Chekhov")
                    .streetNumber(35)
                    .build();
            session.persist(internalEmployee);
            session.getTransaction().commit();
        }
        try (var session = configuration.getSessionFactory().openSession()) {
            var result = session.get(InternalEmployee.class, 1L);
            var expected = InternalEmployee.IEBuilder()
                    .id(1L)
                    .firstName("Neik")
                    .lastName("Phdw")
                    .age(29)
                    .city("Moscow")
                    .street("A. Chekhov")
                    .streetNumber(35)
                    .build();
            assertEquals(expected, result);
        }
    }
}