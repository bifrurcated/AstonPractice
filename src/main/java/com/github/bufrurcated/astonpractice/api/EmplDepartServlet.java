package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.EmplDepartMapper;
import com.github.bufrurcated.astonpractice.service.EmplDepartService;
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

@RequiredArgsConstructor
@Component
public class EmplDepartServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1960818093158963997L;

    private final EmplDepartService service;
    private final EmplDepartMapper emplDepartMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        if (body != null && !body.isEmpty()) {
            var requestEmplDepart = Parse.jsonToEmplDepart(resp, body);
            var emplDepart = emplDepartMapper.map(requestEmplDepart);
            HttpMethodUtils.getAll(service::get, emplDepart, emplDepartMapper::map, resp);
        } else {
            HttpMethodUtils.getAll(service::getAll, emplDepartMapper::map, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestEmplDepart = Parse.jsonToEmplDepart(resp, body);
        var emplDepart = emplDepartMapper.map(requestEmplDepart);
        HttpMethodUtils.execute(service::add, emplDepart, resp);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Employee Department created");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        if (body != null && !body.isEmpty()) {
            var requestEmplDepart = Parse.jsonToEmplDepart(resp, body);
            var emplDepart = emplDepartMapper.map(requestEmplDepart);
            HttpMethodUtils.execute(service::remove, emplDepart, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Employee Department deleted");
        } else {
            HttpMethodUtils.execute(service::removeAll, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("All Employee Department deleted");
        }
    }
}
