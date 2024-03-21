package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.DepartmentDAO;
import com.github.bufrurcated.astonpractice.dao.EmplDepartDAO;
import com.github.bufrurcated.astonpractice.dao.EmplDepartN1DAO;
import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.db.ConfigurationDB;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.entity.EmplDepart;
import com.github.bufrurcated.astonpractice.entity.Employee;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

public class EmplDepartServiceTest {

    private ConfigurationDB configurationDB;
    private EmplDepartService emplDepartService;
    private EmplDepartService emplDepartServiceN1;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        configurationDB = new ConfigurationDB();
        var emplDepartDAO = new EmplDepartDAO(configurationDB.getSessionFactory());
        emplDepartService = new EmplDepartService(emplDepartDAO);
        var employeeDAO = new EmployeeDAO(configurationDB.getSessionFactory());
        EmployeeService employeeService = new EmployeeService(employeeDAO);
        var departmentDAO = new DepartmentDAO(configurationDB.getSessionFactory());
        DepartmentService departmentService = new DepartmentService(departmentDAO);

        var employee = new Employee();
        employee.setFirstName("Nick");
        employee.setLastName("Malkovich");
        employee.setAge(21);
        employeeService.add(employee);

        var department = new Department();
        department.setName("Programmer");
        departmentService.add(department);

        emplDepartServiceN1 = new EmplDepartService(new EmplDepartN1DAO(configurationDB.getSessionFactory()));
    }

    @SneakyThrows
    @AfterEach
    void close() {
        configurationDB.shutdown();
    }

    @Test
    public void testAddEmplDepartN1Problem() throws Exception {
        EmplDepart emplDepart = new EmplDepart(1L, 1L);
        emplDepartServiceN1.add(emplDepart);

        var getEmplDepart = new EmplDepart(1L, null);
        var result = emplDepartServiceN1.get(getEmplDepart);
        var expected = new EmplDepart(1L, 1L);
        Assertions.assertEquals(expected, result.getFirst());
    }

    @Test
    public void testAddEmplDepartWithoutN1Problem() throws Exception {
        EmplDepart emplDepart = new EmplDepart(1L, 1L);
        emplDepartService.add(emplDepart);

        var getEmplDepart = new EmplDepart(1L, null);
        var result = emplDepartService.get(getEmplDepart);
        var expected = new EmplDepart(1L, 1L);
        Assertions.assertEquals(expected, result.getFirst());
    }
}
