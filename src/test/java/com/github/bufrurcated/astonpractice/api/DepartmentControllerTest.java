package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.DepartmentDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.dto.ResponseDepartment;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.mapper.DepartmentMapper;
import com.github.bufrurcated.astonpractice.service.DepartmentService;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentControllerTest {
    private DepartmentController controller;
    private ConfigurationDB configurationDB;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        configurationDB = new ConfigurationDB();
        var departmentService = new DepartmentService(new DepartmentDAO(configurationDB.getSessionFactory()));
        departmentService.add(Department.builder().name("Back-end").build());
        departmentService.add(Department.builder().name("Front-end").build());
        controller = new DepartmentController(departmentService, new DepartmentMapper());
    }

    @AfterEach
    @SneakyThrows
    public void clear() {
        configurationDB.shutdown();
    }

    @SneakyThrows
    @Test
    void testGetOneDepartment() {
        var response = controller.get(1L);
        var responseDepartment = new ResponseDepartment(1L, "Back-end");
        String expected = new JSONObject(responseDepartment).toString();
        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testGetAllDepartments() {
        var response = controller.get();
        List<ResponseDepartment> responseDepartments = new ArrayList<>();
        responseDepartments.add(new ResponseDepartment(1L, "Back-end"));
        responseDepartments.add(new ResponseDepartment(2L, "Front-end"));
        String expected = new JSONArray(responseDepartments).toString();
        assertEquals(expected, response.getBody());
    }

    @SneakyThrows
    @Test
    void testPostCreateDepartment() {
        String json = """
                {
                    "name": "Testers"
                }
                """;
        var response = controller.create(json);
        assertEquals("Department created", response.getBody());
    }

    @SneakyThrows
    @Test
    void testPUTUpdateDepartment() {
        String json = """
                {
                    "id": 1,
                    "name": "Java Back-end",
                }
                """;
        var response = controller.update(json);
        assertEquals("Department updated", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEOneEmployee() {
        var response = controller.delete(1L);
        assertEquals("Department deleted", response.getBody());
    }

    @SneakyThrows
    @Test
    void testDELETEAllEmployee() {
        var response = controller.delete();
        assertEquals("All departments deleted", response.getBody());
    }
}