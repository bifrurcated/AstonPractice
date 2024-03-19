package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.EmployeeMapper;
import com.github.bufrurcated.astonpractice.service.EmployeeService;
import com.github.bufrurcated.astonpractice.utils.Parse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class EmployeeServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = -7270386533793068282L;

    private final EmployeeService service;
    private final EmployeeMapper employeeMapper;

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
            List<ResponseEmployee> responseEmployees;
            try {
                responseEmployees = service.getAll().stream().map(employeeMapper::map).toList();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(responseEmployees).toString());
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
}
