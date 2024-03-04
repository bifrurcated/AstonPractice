package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.db.DatabaseSource;
import com.github.bufrurcated.astonpractice.dto.RequestCreateEmployee;
import com.github.bufrurcated.astonpractice.dto.ResponseEmployee;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.utils.InitData;
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
    private static DatabaseSource databaseSource;
    private EmployeeServlet servlet;
    private StringWriter writer;

    @SneakyThrows
    @BeforeAll
    static void init() {
        databaseSource = new DatabaseSource();
        InitData.clear(databaseSource.getConnection());
    }

    @SneakyThrows
    @AfterAll
    static void close() {
        databaseSource.shutdown();
    }

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        InitData.initialization(databaseSource.getConnection());
        servlet = new EmployeeServlet();
        servlet.init();
        writer = new StringWriter();
    }

    @AfterEach
    public void clear() {
        InitData.clear(databaseSource.getConnection());
        servlet.destroy();
    }

    @SneakyThrows
    @Test
    void testGetOneEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
        var responseEmployee = new ResponseEmployee("Nick", "Jordan", 19);
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
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1L, "Nick", "Jordan", 19));
        employeeList.add(new Employee(2L, "Jonh", "Mike", 35));
        employeeList.add(new Employee(3L, "Artyom", "Vedoseev", 25));
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