package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.db.DatabaseSource;
import com.github.bufrurcated.astonpractice.dto.RequestCreateEmployee;
import com.github.bufrurcated.astonpractice.dto.RequestUpdateEmployee;
import com.github.bufrurcated.astonpractice.dto.ResponseEmployee;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;
import com.github.bufrurcated.astonpractice.mapper.EmployeeMapper;
import com.github.bufrurcated.astonpractice.service.EmployeeService;
import com.github.bufrurcated.astonpractice.utils.InitData;
import com.github.bufrurcated.astonpractice.utils.Parse;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/api/v1/employees/*"})
public class EmployeeServlet extends HttpServlet {
    private EmployeeService service;
    private DatabaseSource databaseSource;
    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        databaseSource = new DatabaseSource();
        var connection = databaseSource.getConnection();
        var dao = new EmployeeDAO(connection);
        service = new EmployeeService(dao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathInfo = Optional.ofNullable(req.getPathInfo());
        if (pathInfo.isPresent()) {
            var strId = pathInfo.get().substring(1);
            long id = Parse.stringToLong(resp, strId);
            Employee employee;
            try {
                employee = service.getById(id);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            var responseEmployee = employeeMapper.map(employee);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONObject(responseEmployee).toString());
        } else {
            List<Employee> employees;
            try {
                employees = service.getAll();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(employees).toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestCreateEmployee = Parse.jsonToCreateEmployee(resp, body);
        var emp = employeeMapper.map(requestCreateEmployee);
        try {
            service.add(emp);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Employee created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestUpdateEmployee = Parse.jsonToUpdateEmployee(resp, body);
        var emp = employeeMapper.map(requestUpdateEmployee);
        try {
            service.update(emp);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Employee update");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathInfo = Optional.ofNullable(req.getPathInfo());
        if (pathInfo.isPresent()) {
            String strId = pathInfo.get().substring(1);
            long id = Parse.stringToLong(resp, strId);
            try {
                service.removeById(id);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Employee deleted");
        } else {
            try {
                service.removeAll();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("All employees deleted");
        }
    }

    @Override
    public void destroy() {
        databaseSource.shutdown();
    }
}
