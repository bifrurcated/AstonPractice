package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.DepartmentDAO;
import com.github.bufrurcated.astonpractice.db.DatabaseSource;
import com.github.bufrurcated.astonpractice.dto.ResponseDepartment;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;
import com.github.bufrurcated.astonpractice.mapper.DepartmentMapper;
import com.github.bufrurcated.astonpractice.service.DepartmentService;
import com.github.bufrurcated.astonpractice.utils.Parse;
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

@WebServlet(urlPatterns = {"/api/v1/department/*"})
public class DepartmentServlet extends HttpServlet {
    private DepartmentService service;
    private DatabaseSource databaseSource;
    private final DepartmentMapper departmentMapper = new DepartmentMapper();

    @Override
    public void init() throws ServletException {
        databaseSource = new DatabaseSource();
        var connection = databaseSource.getConnection();
        var dao = new DepartmentDAO(connection);
        service = new DepartmentService(dao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathInfo = Optional.ofNullable(req.getPathInfo());
        if (pathInfo.isPresent()) {
            var strId = pathInfo.get().substring(1);
            long id = Parse.stringToLong(resp, strId);
            Department department;
            try {
                department = service.getById(id);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            ResponseDepartment responseDepartment = departmentMapper.map(department);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONObject(responseDepartment).toString());
        } else {
            List<Department> employees;
            try {
                employees = service.getAll();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            var employeeList = employees.stream().map(departmentMapper::map).toList();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(employeeList).toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestCreateDepartment = Parse.jsonToCreateDepartment(resp, body);
        var dpt = departmentMapper.map(requestCreateDepartment);
        try {
            service.add(dpt);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Department created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestUpdateDepartment = Parse.jsonToUpdateDepartment(resp, body);
        var emp = departmentMapper.map(requestUpdateDepartment);
        try {
            service.update(emp);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Department update");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathInfo = Optional.ofNullable(req.getPathInfo());
        if (pathInfo.isPresent()) {
            var strId = pathInfo.get().substring(1);
            long id = Parse.stringToLong(resp, strId);
            try {
                service.removeById(id);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Department deleted");
        } else {
            try {
                service.removeAll();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("All departments deleted");
        }
    }

    @Override
    public void destroy() {
        databaseSource.shutdown();
    }
}
