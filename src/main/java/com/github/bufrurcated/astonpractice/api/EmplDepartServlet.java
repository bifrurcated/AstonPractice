package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.EmplDepartDAO;
import com.github.bufrurcated.astonpractice.db.DatabaseSource;
import com.github.bufrurcated.astonpractice.dto.*;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.entity.EmplDepart;
import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;
import com.github.bufrurcated.astonpractice.mapper.EmplDepartMapper;
import com.github.bufrurcated.astonpractice.service.EmplDepartService;
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

@WebServlet(urlPatterns = {"/api/v1/employee-department"})
public class EmplDepartServlet extends HttpServlet {
    private EmplDepartService service;
    private DatabaseSource databaseSource;
    private final EmplDepartMapper emplDepartMapper = new EmplDepartMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        databaseSource = new DatabaseSource();
        var connection = databaseSource.getConnection();
        var dao = new EmplDepartDAO(connection);
        service = new EmplDepartService(dao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        if (body != null && !body.isEmpty()) {
            var requestEmplDepart = Parse.jsonToEmplDepart(resp, body);
            var emplDepart = emplDepartMapper.map(requestEmplDepart);
            List<EmplDepart> emplDeparts;
            try {
                emplDeparts = service.get(emplDepart);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            var emplDepartList = emplDeparts.stream().map(emplDepartMapper::map).toList();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(emplDepartList).toString());
        } else {
            List<EmplDepart> emplDeparts;
            try {
                emplDeparts = service.getAll();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            var emplDepartList = emplDeparts.stream().map(emplDepartMapper::map).toList();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(emplDepartList).toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestEmplDepart = Parse.jsonToEmplDepart(resp, body);
        var emplDepart = emplDepartMapper.map(requestEmplDepart);
        try {
            service.add(emplDepart);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Employee Department created");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        if (body != null && !body.isEmpty()) {
            var requestEmplDepart = Parse.jsonToEmplDepart(resp, body);
            var emplDepart = emplDepartMapper.map(requestEmplDepart);
            try {
                service.remove(emplDepart);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Employee Department deleted");
        } else {
            try {
                service.removeAll();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("All Employee Department deleted");
        }
    }

    @Override
    public void destroy() {
        databaseSource.shutdown();
    }
}
