package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.EmployeeMapper;
import com.github.bufrurcated.astonpractice.service.EmployeeService;
import com.github.bufrurcated.astonpractice.utils.HttpMethodUtils;
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
        HttpMethodUtils.doGet(req, service::getById, service::getAll, employeeMapper::map, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestCreateEmployee = Parse.jsonToCreateEmployee(resp, body);
        var emp = employeeMapper.map(requestCreateEmployee);
        HttpMethodUtils.execute(service::add, emp, resp);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Employee created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestUpdateEmployee = Parse.jsonToUpdateEmployee(resp, body);
        var emp = employeeMapper.map(requestUpdateEmployee);
        HttpMethodUtils.execute(service::update, emp, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Employee update");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathInfo = Optional.ofNullable(req.getPathInfo());
        if (pathInfo.isPresent()) {
            String strId = pathInfo.get().substring(1);
            long id = Parse.stringToLong(resp, strId);
            HttpMethodUtils.execute(service::removeById, id, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Employee deleted");
        } else {
            HttpMethodUtils.execute(service::removeAll, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("All employees deleted");
        }
    }
}
