package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.dto.ResponseEmployee;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.mapper.EmployeeMapper;
import com.github.bufrurcated.astonpractice.service.EmployeeService;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeControllerTest {
    private EmployeeController controller;
    private ConfigurationDB configurationDB;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        configurationDB = new ConfigurationDB();
        var employeeService = new EmployeeService(new EmployeeDAO(configurationDB.getSessionFactory()));
        employeeService.add(Employee.builder().firstName("Nick").lastName("Jordan").age(19).build());
        employeeService.add(Employee.builder().firstName("Jonh").lastName("Mike").age(35).build());
        employeeService.add(Employee.builder().firstName("Artyom").lastName("Vedoseev").age(25).build());
        controller = new EmployeeController(employeeService, new EmployeeMapper());
    }

    @AfterEach
    @SneakyThrows
    public void clear() {
        configurationDB.shutdown();
    }

    @SneakyThrows
    @Test
    void testGetOneEmployee() {
        var response = controller.get(1L);
        var responseEmployee = new ResponseEmployee(1L, "Nick", "Jordan", 19);
        String expected = new JSONObject(responseEmployee).toString();
        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testGetAllEmployee() {
        var response = controller.get();
        List<ResponseEmployee> employeeList = new ArrayList<>();
        employeeList.add(new ResponseEmployee(1L, "Nick", "Jordan", 19));
        employeeList.add(new ResponseEmployee(2L, "Jonh", "Mike", 35));
        employeeList.add(new ResponseEmployee(3L, "Artyom", "Vedoseev", 25));
        String expected = new JSONArray(employeeList).toString();
        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testPostCreateEmployee() {
        String json = """
                {
                    "first_name": "Pol",
                    "last_name": "Anderson",
                    "age": 21
                }
                """;

        var response = controller.create(json);
        assertEquals("Employee created", response.getBody());
    }

    @SneakyThrows
    @Test
    void testPUTUpdateEmployee() {
        String json = """
                {
                    "id": 1,
                    "first_name": "Pol",
                    "last_name": "Anderson",
                    "age": 21
                }
                """;
        var response = controller.update(json);
        assertEquals("Employee updated", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEOneEmployee() {
        var response = controller.delete(1L);
        assertEquals("Employee deleted", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEAllEmployee() {
        var response = controller.delete();
        assertEquals("All employees deleted", response.getBody());
    }
}