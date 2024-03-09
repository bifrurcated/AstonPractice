package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.DepartmentDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.dto.ResponseDepartment;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.service.DepartmentService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DepartmentServletTest {
    private static DepartmentServlet servlet;
    private StringWriter writer;
    private ConfigurationDB configurationDB;

    @BeforeAll
    @SneakyThrows
    static void init() {
        servlet = new DepartmentServlet();
        servlet.init();
    }

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        configurationDB = new ConfigurationDB();
        var departmentService = new DepartmentService(new DepartmentDAO(configurationDB.getSessionFactory()));
        departmentService.add(Department.builder().name("Back-end").build());
        departmentService.add(Department.builder().name("Front-end").build());
        writer = new StringWriter();
    }

    @AfterEach
    @SneakyThrows
    public void clear() {
        configurationDB.shutdown();
    }

    @SneakyThrows
    @Test
    void testGetOneDepartment() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
        var responseDepartment = new ResponseDepartment(1L, "Back-end");
        String expected = new JSONObject(responseDepartment).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testGetAllDepartments() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
        List<ResponseDepartment> responseDepartments = new ArrayList<>();
        responseDepartments.add(new ResponseDepartment(1L, "Back-end"));
        responseDepartments.add(new ResponseDepartment(2L, "Front-end"));
        String expected = new JSONArray(responseDepartments).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testPostCreateDepartment() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String json = """
                {
                    "name": "Testers"
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(request, response);

        assertEquals("Department created", writer.toString());
    }

    @SneakyThrows
    @Test
    void testPUTUpdateDepartment() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String json = """
                {
                    "id": 1,
                    "name": "Java Back-end",
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPut(request, response);

        assertEquals("Department update", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEOneEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("Department deleted", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEAllEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("All departments deleted", writer.toString());
    }
}