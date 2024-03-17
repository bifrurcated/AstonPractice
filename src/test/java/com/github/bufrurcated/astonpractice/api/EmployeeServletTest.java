package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.dto.ResponseEmployee;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeServletTest {
    private static EmployeeServlet servlet;
    private StringWriter writer;
    private ConfigurationDB configurationDB;

    @BeforeAll
    @SneakyThrows
    static void init() {
        servlet = new EmployeeServlet();
        servlet.init();
    }

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        configurationDB = new ConfigurationDB();
        var employeeService = new EmployeeService(new EmployeeDAO(configurationDB.getSessionFactory()));
        employeeService.add(Employee.builder().firstName("Nick").lastName("Jordan").age(19).build());
        employeeService.add(Employee.builder().firstName("Jonh").lastName("Mike").age(35).build());
        employeeService.add(Employee.builder().firstName("Artyom").lastName("Vedoseev").age(25).build());
        writer = new StringWriter();
    }

    @AfterEach
    @SneakyThrows
    public void clear() {
        configurationDB.shutdown();
    }

    @SneakyThrows
    @Test
    void testGetOneEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
        var responseEmployee = new ResponseEmployee(1L, "Nick", "Jordan", 19);
        String expected = new JSONObject(responseEmployee).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testGetAllEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
        List<ResponseEmployee> employeeList = new ArrayList<>();
        employeeList.add(new ResponseEmployee(1L, "Nick", "Jordan", 19));
        employeeList.add(new ResponseEmployee(2L, "Jonh", "Mike", 35));
        employeeList.add(new ResponseEmployee(3L, "Artyom", "Vedoseev", 25));
        String expected = new JSONArray(employeeList).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testPostCreateEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String json = """
                {
                    "first_name": "Pol",
                    "last_name": "Anderson",
                    "age": 21
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(request, response);

        assertEquals("Employee created", writer.toString());
    }

    @SneakyThrows
    @Test
    void testPUTUpdateEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String json = """
                {
                    "id": 1,
                    "first_name": "Pol",
                    "last_name": "Anderson",
                    "age": 21
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPut(request, response);

        assertEquals("Employee update", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEOneEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("Employee deleted", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEAllEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("All employees deleted", writer.toString());
    }
}