package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.DepartmentDAO;
import com.github.bufrurcated.astonpractice.dao.EmplDepartDAO;
import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.dto.ResponseEmplDepart;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.entity.EmplDepart;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.mapper.EmplDepartMapper;
import com.github.bufrurcated.astonpractice.service.DepartmentService;
import com.github.bufrurcated.astonpractice.service.EmplDepartService;
import com.github.bufrurcated.astonpractice.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
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

class EmplDepartServletTest {
    private EmplDepartServlet servlet;
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
        var departmentService = new DepartmentService(new DepartmentDAO(configurationDB.getSessionFactory()));
        departmentService.add(Department.builder().name("Back-end").build());
        departmentService.add(Department.builder().name("Front-end").build());
        var emplDepartService = new EmplDepartService(new EmplDepartDAO(configurationDB.getSessionFactory()));
        emplDepartService.add(new EmplDepart(1L, 1L));
        emplDepartService.add(new EmplDepart(1L, 2L));
        emplDepartService.add(new EmplDepart(2L, 1L));
        emplDepartService.add(new EmplDepart(3L, 2L));
        servlet = new EmplDepartServlet(emplDepartService, new EmplDepartMapper());
        writer = new StringWriter();
    }

    @AfterEach
    @SneakyThrows
    public void clear() {
        configurationDB.shutdown();
    }

    @SneakyThrows
    @Test
    void testGetAllDepartmentsOfEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String json = """
                {
                    "employee_id": 1,
                    "department_id": null
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
        List<ResponseEmplDepart> responseEmplDeparts = new ArrayList<>();
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 1L));
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 2L));
        String expected = new JSONArray(responseEmplDeparts).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testGetAllEmployeesOfDepartment() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String json = """
                {
                    "employee_id": null,
                    "department_id": 1
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
        List<ResponseEmplDepart> responseEmplDeparts = new ArrayList<>();
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 1L));
        responseEmplDeparts.add(new ResponseEmplDepart(2L, 1L));
        String expected = new JSONArray(responseEmplDeparts).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testGetAllEmplDeparts() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);

        List<ResponseEmplDepart> responseEmplDeparts = new ArrayList<>();
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 1L));
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 2L));
        responseEmplDeparts.add(new ResponseEmplDepart(2L, 1L));
        responseEmplDeparts.add(new ResponseEmplDepart(3L, 2L));
        String expected = new JSONArray(responseEmplDeparts).toString();

        assertEquals(expected, writer.toString());
    }

    @SneakyThrows
    @Test
    void testPostCreateDepartment() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String json = """
                {
                    "employee_id": 3,
                    "department_id": 1
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(request, response);

        assertEquals("Employee Department created", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEAllDepartmentsOfEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String json = """
                {
                    "employee_id": 1,
                    "department_id": 1
                }
                """;
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("Employee Department deleted", writer.toString());
    }

    @SneakyThrows
    @Test
    void testDELETEAllEmployee() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(request, response);

        assertEquals("All Employee Department deleted", writer.toString());
    }
}