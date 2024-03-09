package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.db.DatabaseSource;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.util.Set;

public class EmployeeServiceTest {
    private EmployeeService employeeService;
    private ConfigurationDB configuration;

    @SneakyThrows
    @BeforeEach
    void init() {
        configuration = new ConfigurationDB();
        var dao = new EmployeeDAO(configuration.getSessionFactory());
        employeeService = new EmployeeService(dao);
    }

    @AfterEach
    void clear() {
        configuration.shutdown();
    }

    @Test
    public void testAddEmployee() throws Exception {
        var employee = new Employee();
        employee.setFirstName("Nick");
        employee.setLastName("Malkovich");
        employee.setAge(21);
        var phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber("89005551123");
        phoneNumber.setEmployee(employee);
        var department = new Department();
        department.setName("Programmer");
        employee.setDepartments(Set.of(department));
        employeeService.add(employee);

        var result = employeeService.getById(1L);
        var expected = new Employee(
                1L,
                "Nick",
                "Malkovich",
                21,
                Set.of(phoneNumber),
                Set.of(new Department(1L, "Programmer")));
        Assertions.assertEquals(expected, result);
    }
}
