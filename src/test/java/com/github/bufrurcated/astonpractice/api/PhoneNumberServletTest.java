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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PhoneNumberServletTest {
    private PhoneNumberServlet servlet;
    private StringWriter writer;
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
        servlet = new PhoneNumberServlet(phoneNumberService, new PhoneNumberMapper());
        writer = new StringWriter();
    }

    @AfterEach
    @SneakyThrows
    public void clear() {
        configurationDB.shutdown();
    }

    @SneakyThrows
    @Test
    void testGetOnePhoneNumber() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        var map = new HashMap<String, String[]>();
        map.put("id", new String[]{"1"});
        when(request.getParameterMap()).thenReturn(map);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
        var responsePhoneNumber = new ResponsePhoneNumber(1L, 1L, "88005454234");
        String expected = new JSONObject(responsePhoneNumber).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testGetAllPhoneNumbersOfEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        var map = new HashMap<String, String[]>();
        map.put("employeeId", new String[]{"1"});
        when(request.getParameterMap()).thenReturn(map);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);

        List<ResponsePhoneNumber> responsePhoneNumbers = new ArrayList<>();
        responsePhoneNumbers.add(new ResponsePhoneNumber(1L, 1L, "88005454234"));
        responsePhoneNumbers.add(new ResponsePhoneNumber(2L, 1L, "89243423412"));
        String expected = new JSONArray(responsePhoneNumbers).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testGetAllPhoneNumbers() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameterMap()).thenReturn(new HashMap<>());
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);

        List<ResponsePhoneNumber> responsePhoneNumbers = new ArrayList<>();
        responsePhoneNumbers.add(new ResponsePhoneNumber(1L, 1L, "88005454234"));
        responsePhoneNumbers.add(new ResponsePhoneNumber(2L, 1L, "89243423412"));
        responsePhoneNumbers.add(new ResponsePhoneNumber(3L, 2L, "+123412515216"));
        responsePhoneNumbers.add(new ResponsePhoneNumber(4L, 3L, "+23412515216"));
        String expected = new JSONArray(responsePhoneNumbers).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testPostCreatePhoneNumber() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String json = """
                {
                    "phone_number": "89003323345",
                    "employee_id": 1
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(request, response);

        assertEquals("Phone number created", writer.toString());
    }

    @SneakyThrows
    @Test
    void testPUTUpdateEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String json = """
                {
                    "id": 1,
                    "employee_id": 3,
                    "phone_number": "88005454234"
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPut(request, response);

        assertEquals("Phone number update", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEOnePhoneNumber() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        var map = new HashMap<String, String[]>();
        map.put("id", new String[]{"1"});
        when(request.getParameterMap()).thenReturn(map);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("Phone number deleted", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEPhoneNumbersOfEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        var map = new HashMap<String, String[]>();
        map.put("employeeId", new String[]{"1"});
        when(request.getParameterMap()).thenReturn(map);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("Phone numbers by employee deleted", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEAllEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameterMap()).thenReturn(new HashMap<>());
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("Phone numbers deleted", writer.toString());
    }
}