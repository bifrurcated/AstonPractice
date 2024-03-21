package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.DepartmentMapper;
import com.github.bufrurcated.astonpractice.service.DepartmentService;
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
public class DepartmentServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = -8381272867247581720L;

    private final DepartmentService service;
    private final DepartmentMapper departmentMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpMethodUtils.doGet(req, service::getById, service::getAll, departmentMapper::map, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestCreateDepartment = Parse.jsonToCreateDepartment(resp, body);
        var dpt = departmentMapper.map(requestCreateDepartment);
        HttpMethodUtils.execute(service::add, dpt, resp);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Department created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestUpdateDepartment = Parse.jsonToUpdateDepartment(resp, body);
        var emp = departmentMapper.map(requestUpdateDepartment);
        HttpMethodUtils.execute(service::update, emp, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Department update");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathInfo = Optional.ofNullable(req.getPathInfo());
        if (pathInfo.isPresent()) {
            var strId = pathInfo.get().substring(1);
            long id = Parse.stringToLong(resp, strId);
            HttpMethodUtils.execute(service::removeById, id, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Department deleted");
        } else {
            HttpMethodUtils.execute(service::removeAll, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("All departments deleted");
        }
    }
}
