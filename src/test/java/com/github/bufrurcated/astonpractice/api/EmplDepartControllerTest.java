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
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmplDepartControllerTest {
    private EmplDepartController controller;
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
        controller = new EmplDepartController(emplDepartService, new EmplDepartMapper());
    }

    @AfterEach
    @SneakyThrows
    public void clear() {
        configurationDB.shutdown();
    }

    @SneakyThrows
    @Test
    void testGetAllDepartmentsOfEmployee() {
        String json = """
                {
                    "employee_id": 1,
                    "department_id": null
                }
                """;
        var response = controller.get(json);
        List<ResponseEmplDepart> responseEmplDeparts = new ArrayList<>();
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 1L));
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 2L));
        String expected = new JSONArray(responseEmplDeparts).toString();

        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testGetAllEmployeesOfDepartment() {
        String json = """
                {
                    "employee_id": null,
                    "department_id": 1
                }
                """;
        var response = controller.get(json);
        List<ResponseEmplDepart> responseEmplDeparts = new ArrayList<>();
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 1L));
        responseEmplDeparts.add(new ResponseEmplDepart(2L, 1L));
        String expected = new JSONArray(responseEmplDeparts).toString();

        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testGetAllEmplDeparts() {
        var response = controller.get(null);

        List<ResponseEmplDepart> responseEmplDeparts = new ArrayList<>();
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 1L));
        responseEmplDeparts.add(new ResponseEmplDepart(1L, 2L));
        responseEmplDeparts.add(new ResponseEmplDepart(2L, 1L));
        responseEmplDeparts.add(new ResponseEmplDepart(3L, 2L));
        String expected = new JSONArray(responseEmplDeparts).toString();

        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testPostCreateDepartment() {
        String json = """
                {
                    "employee_id": 3,
                    "department_id": 1
                }
                """;
        var response = controller.create(json);
        assertEquals("Employee Department created", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEAllDepartmentsOfEmployee() {
        String json = """
                {
                    "employee_id": 1,
                    "department_id": 1
                }
                """;
        var response = controller.delete(json);
        assertEquals("Employee Department deleted", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEAllEmployee() {
        var response = controller.delete(null);
        assertEquals("All Employee Department deleted", response.getBody());
    }
}