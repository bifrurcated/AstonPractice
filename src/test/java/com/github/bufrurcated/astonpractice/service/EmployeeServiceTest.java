package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.dao.EmployeeWithDepartmentsDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class EmployeeServiceTest {
    private EmployeeService employeeService;
    private EmployeeService employeeWithDepartmentsService;
    private ConfigurationDB configuration;

    @SneakyThrows
    @BeforeEach
    void init() {
        configuration = new ConfigurationDB();
        var dao = new EmployeeDAO(configuration.getSessionFactory());
        employeeService = new EmployeeService(dao);

        employeeWithDepartmentsService = new EmployeeService(new EmployeeWithDepartmentsDAO(configuration.getSessionFactory()));
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
        employee.setDepartments(List.of(department));
        employeeService.add(employee);

        var result = employeeService.getById(1L);
        var expected = new Employee(
                1L,
                "Nick",
                "Malkovich",
                21,
                List.of(phoneNumber),
                List.of(new Department(1L, "Programmer")));
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testLazyInitializationException() throws Exception {
        var employee = new Employee();
        employee.setFirstName("Nick");
        employee.setLastName("Malkovich");
        employee.setAge(21);
        var phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber("89005551123");
        phoneNumber.setEmployee(employee);
        var department = new Department();
        department.setName("Programmer");
        employee.setDepartments(List.of(department));
        employeeService.add(employee);

        var result = employeeService.getById(1L);
        Assertions.assertThrows(LazyInitializationException.class, () -> {
            result.getDepartments().getFirst();
        });
    }

    @Test
    public void testFixLazyInitializationException() throws Exception {
        var employee = new Employee();
        employee.setFirstName("Nick");
        employee.setLastName("Malkovich");
        employee.setAge(21);
        var phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber("89005551123");
        phoneNumber.setEmployee(employee);
        var department = new Department();
        department.setName("Programmer");
        employee.setDepartments(List.of(department));
        employeeWithDepartmentsService.add(employee);

        var emp = employeeWithDepartmentsService.getById(1L);
        var departmentName = emp.getDepartments().getFirst().getName();
        Assertions.assertEquals("Programmer", departmentName);
    }
}
