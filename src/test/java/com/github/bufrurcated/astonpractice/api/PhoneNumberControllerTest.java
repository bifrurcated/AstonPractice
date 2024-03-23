package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.dao.PhoneNumberDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.dto.ResponsePhoneNumber;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;
import com.github.bufrurcated.astonpractice.mapper.PhoneNumberMapper;
import com.github.bufrurcated.astonpractice.service.EmployeeService;
import com.github.bufrurcated.astonpractice.service.PhoneNumberService;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhoneNumberControllerTest {
    private PhoneNumberController controller;
    private ConfigurationDB configurationDB;


    @BeforeEach
    @SneakyThrows
    public void setUp() {
        configurationDB = new ConfigurationDB();
        var employeeService = new EmployeeService(new EmployeeDAO(configurationDB.getSessionFactory()));
        employeeService.add(Employee.builder().firstName("Nick").lastName("Jordan").age(19).build());
        employeeService.add(Employee.builder().firstName("Jonh").lastName("Mike").age(35).build());
        employeeService.add(Employee.builder().firstName("Artyom").lastName("Vedoseev").age(25).build());
        var phoneNumberService = new PhoneNumberService(new PhoneNumberDAO(configurationDB.getSessionFactory()));
        phoneNumberService.add(PhoneNumber.builder().phoneNumber("88005454234").employee(Employee.builder().id(1L).build()).build());
        phoneNumberService.add(PhoneNumber.builder().phoneNumber("89243423412").employee(Employee.builder().id(1L).build()).build());
        phoneNumberService.add(PhoneNumber.builder().phoneNumber("+123412515216").employee(Employee.builder().id(2L).build()).build());
        phoneNumberService.add(PhoneNumber.builder().phoneNumber("+23412515216").employee(Employee.builder().id(3L).build()).build());
        controller = new PhoneNumberController(phoneNumberService, new PhoneNumberMapper());
    }

    @AfterEach
    @SneakyThrows
    public void clear() {
        configurationDB.shutdown();
    }

    @SneakyThrows
    @Test
    void testGetOnePhoneNumber() {
        var response = controller.get(1L, null);
        var responsePhoneNumber = new ResponsePhoneNumber(1L, 1L, "88005454234");
        String expected = new JSONObject(responsePhoneNumber).toString();

        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testGetAllPhoneNumbersOfEmployee() {
        var response = controller.get(null, 1L);

        List<ResponsePhoneNumber> responsePhoneNumbers = new ArrayList<>();
        responsePhoneNumbers.add(new ResponsePhoneNumber(1L, 1L, "88005454234"));
        responsePhoneNumbers.add(new ResponsePhoneNumber(2L, 1L, "89243423412"));
        String expected = new JSONArray(responsePhoneNumbers).toString();

        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testGetAllPhoneNumbers() {
        var response = controller.get(null, null);

        List<ResponsePhoneNumber> responsePhoneNumbers = new ArrayList<>();
        responsePhoneNumbers.add(new ResponsePhoneNumber(1L, 1L, "88005454234"));
        responsePhoneNumbers.add(new ResponsePhoneNumber(2L, 1L, "89243423412"));
        responsePhoneNumbers.add(new ResponsePhoneNumber(3L, 2L, "+123412515216"));
        responsePhoneNumbers.add(new ResponsePhoneNumber(4L, 3L, "+23412515216"));
        String expected = new JSONArray(responsePhoneNumbers).toString();

        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testPostCreatePhoneNumber() {
        String json = """
                {
                    "phone_number": "89003323345",
                    "employee_id": 1
                }
                """;
        var response = controller.create(json);
        assertEquals("Phone number created", response.getBody());
    }

    @SneakyThrows
    @Test
    void testPUTUpdateEmployee() {
        String json = """
                {
                    "id": 1,
                    "employee_id": 3,
                    "phone_number": "88005454234"
                }
                """;
        var response = controller.update(json);
        assertEquals("Phone number updated", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEOnePhoneNumber() {
        var response = controller.delete(1L, null);
        assertEquals("Phone number deleted", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEPhoneNumbersOfEmployee() {
        var response = controller.delete(null,1L);
        assertEquals("Phone numbers by employee deleted", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEAllEmployee() {
        var response = controller.delete(null, null);
        assertEquals("Phone numbers deleted", response.getBody());
    }
}