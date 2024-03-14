package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.EmployeeSpicificFindDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.entity.Employee;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.random.RandomGenerator;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeSpecificFindTest {

    private static EmployeeService employeeSpecificFind;
    private static ConfigurationDB configuration;

    private static long timeWithoutIndex = 10000;

    @SneakyThrows
    @BeforeAll
    static void init(){
        configuration = new ConfigurationDB();
        employeeSpecificFind = new EmployeeService(new EmployeeSpicificFindDAO(configuration.getSessionFactory()));

        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        List<Employee> employeeList = new ArrayList<>(2_000_000);
        for (int i = 0; i < 2_000_000; i++) {
            employeeList.add(Employee.builder()
                    .firstName(UUID.randomUUID().toString())
                    .lastName(UUID.randomUUID().toString())
                    .age(randomGenerator.nextInt(1000, 100000))
                    .build());
        }
        int countParallelTask = 2000;
        int sizeList = employeeList.size();
        List<Callable<Boolean>> tasks = new ArrayList<>();
        try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
            int size = sizeList / countParallelTask;
            for (int i = 0; i < countParallelTask; i++) {
                int finalI = i;
                int batchSize = sizeList/countParallelTask;
                Callable<Boolean> callable = () -> {
                    insertingRandomValueToTable(employeeList, size * finalI, size * (finalI + 1), batchSize);
                    return true;
                };
                tasks.add(callable);
            }
            service.invokeAll(tasks);
        }
    }

    private static void insertingRandomValueToTable(List<Employee> employeeList, int start, int end, int batchSize) {
        try (var session = configuration.getSessionFactory().openSession()) {
            session.beginTransaction();
            for (int i = start; i < end; i++) {
                session.persist(employeeList.get(i));
                if (i % batchSize == 0) {//a batch size for safety
                    session.flush();
                    session.clear();
                }
            }
            session.getTransaction().commit();
        }
    }

    @AfterAll
    static void destroy() {
        configuration.shutdown();
    }

    @RepeatedTest(5)
    @Order(1)
    @SneakyThrows
    void testGetAllRowsWhereAgeGreater2000() {
        long start = System.currentTimeMillis();
        var employeesWhereAgeGreater2000 = employeeSpecificFind.getAll();
        long end = System.currentTimeMillis();
        long result = end - start;
        log.info("time: " + result);
        log.info("asd" + employeesWhereAgeGreater2000.getFirst().toString());
        if (result < timeWithoutIndex) {
            timeWithoutIndex = result;
        }
    }

    @Test
    @Order(2)
    @SneakyThrows
    void testGetAllRowsWhereAgeGreater2000OptimizationWithIndex() {
        configuration.getSessionFactory().inTransaction(session -> {
            String sql = """
                        CREATE INDEX employee_age_index
                        ON employees (age)
                    """;
            session.createNativeQuery(sql, Void.class).executeUpdate();
        });
        long start = System.currentTimeMillis();
        var employeesWhereAgeGreater2000 = employeeSpecificFind.getAll();
        long end = System.currentTimeMillis();
        long result = end - start;
        log.info("time: " + result);
        log.info("asd" + employeesWhereAgeGreater2000.getFirst().toString());
        Assertions.assertTrue(result < timeWithoutIndex);
    }

    @Order(3)
    @RepeatedTest(5)
    @SneakyThrows
    void testGetAllRowsWhereAgeGreater2000OptimizationWithIndexRepeat() {
        long start = System.currentTimeMillis();
        var employeesWhereAgeGreater2000 = employeeSpecificFind.getAll();
        long end = System.currentTimeMillis();
        long result = end - start;
        log.info("time: " + result);
        log.info("asd" + employeesWhereAgeGreater2000.getFirst().toString());
        Assertions.assertTrue(result < timeWithoutIndex);
    }

}
