package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.dao.PhoneNumberDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;
import com.github.bufrurcated.astonpractice.errors.PhoneNumberNotFoundError;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.util.List;

public class PhoneNumberServiceTest {

    private ConfigurationDB configurationDB;
    private PhoneNumberService phoneNumberService;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        configurationDB = new ConfigurationDB();
        var employeeDAO = new EmployeeDAO(configurationDB.getSessionFactory());
        EmployeeService employeeService = new EmployeeService(employeeDAO);
        var phoneNumberDAO = new PhoneNumberDAO(configurationDB.getSessionFactory());
        phoneNumberService = new PhoneNumberService(phoneNumberDAO);

        var employee1 = Employee.builder()
                .firstName("Nick")
                .lastName("Malkovich")
                .age(21)
                .build();
        var employee2 = Employee.builder()
                .firstName("Marta")
                .lastName("Polinsky")
                .age(31)
                .build();
        employeeService.add(employee1);
        employeeService.add(employee2);
    }

    @SneakyThrows
    @AfterEach
    void close() {
        configurationDB.shutdown();
    }

    @Test
    public void testAddPhoneNumber() throws Exception {
        var phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber("89003363313");
        var emp = new Employee();
        emp.setId(1L);
        phoneNumber.setEmployee(emp);
        phoneNumberService.add(phoneNumber);

        var result = phoneNumberService.getById(1L);
        var expected = new PhoneNumber(1L, "89003363313");
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testGetPhoneNumbersOfEmployee() throws Exception {
        var phoneNumber1 = PhoneNumber.builder()
                .phoneNumber("89003363313")
                .employee(Employee.builder().id(1L).build())
                .build();
        var phoneNumber2 = PhoneNumber.builder()
                .phoneNumber("89222243210")
                .employee(Employee.builder().id(1L).build())
                .build();
        phoneNumberService.add(phoneNumber1);
        phoneNumberService.add(phoneNumber2);

        var result = phoneNumberService.getByEmployeeId(1L);
        var ph1 = new PhoneNumber(1L, "89003363313");
        var ph2 = new PhoneNumber(2L, "89003363313");
        var expected = List.of(ph1, ph2).toArray();
        Assertions.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testUpdatePhoneNumbers() throws Exception {
        var phoneNumber = PhoneNumber.builder()
                .phoneNumber("89003363313")
                .employee(Employee.builder().id(1L).build())
                .build();
        phoneNumberService.add(phoneNumber);

        phoneNumberService.update(PhoneNumber.builder()
                .id(1L)
                .phoneNumber("89003363313")
                .employee(Employee.builder().id(2L).build())
                .build());

        var result = phoneNumberService.getById(1L);
        var expected = PhoneNumber.builder()
                .id(1L)
                .phoneNumber("89003363313")
                .employee(Employee.builder().id(2L).build())
                .build();
        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected.getPhoneNumber(), result.getPhoneNumber());
        Assertions.assertEquals(expected.getEmployee(), result.getEmployee());
    }

    @Test
    public void testDeletePhoneNumber() throws Exception {
        var phoneNumber = PhoneNumber.builder()
                .phoneNumber("89003363313")
                .employee(Employee.builder().id(1L).build())
                .build();
        phoneNumberService.add(phoneNumber);

        phoneNumberService.removeById(1L);

        Assertions.assertThrows(PhoneNumberNotFoundError.class, () -> phoneNumberService.getById(1L));
    }

    @Test
    public void testDeletePhoneNumbersOfEmployee() throws Exception {
        var phoneNumber1 = PhoneNumber.builder()
                .phoneNumber("89003363313")
                .employee(Employee.builder().id(1L).build())
                .build();
        var phoneNumber2 = PhoneNumber.builder()
                .phoneNumber("89222243210")
                .employee(Employee.builder().id(1L).build())
                .build();
        phoneNumberService.add(phoneNumber1);
        phoneNumberService.add(phoneNumber2);

        phoneNumberService.removeByEmployeeId(1L);

        Assertions.assertThrows(PhoneNumberNotFoundError.class, () -> phoneNumberService.getByEmployeeId(1L));
    }

    @Test
    public void testDeleteAllPhoneNumbers() throws Exception {
        var phoneNumber1 = PhoneNumber.builder()
                .phoneNumber("89003363313")
                .employee(Employee.builder().id(1L).build())
                .build();
        var phoneNumber2 = PhoneNumber.builder()
                .phoneNumber("89222243210")
                .employee(Employee.builder().id(1L).build())
                .build();
        phoneNumberService.add(phoneNumber1);
        phoneNumberService.add(phoneNumber2);

        phoneNumberService.removeAll();

        Assertions.assertThrows(PhoneNumberNotFoundError.class, () -> phoneNumberService.getById(1L));
        Assertions.assertThrows(PhoneNumberNotFoundError.class, () -> phoneNumberService.getByEmployeeId(1L));
        Assertions.assertThrows(PhoneNumberNotFoundError.class, () -> phoneNumberService.getAll());
    }
}
