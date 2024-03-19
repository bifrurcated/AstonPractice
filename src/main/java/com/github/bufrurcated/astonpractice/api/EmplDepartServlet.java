package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.EmplDepartMapper;
import com.github.bufrurcated.astonpractice.service.EmplDepartService;
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
            List<ResponseEmplDepart> responseEmplDeparts;
            try {
                responseEmplDeparts = service.get(emplDepart).stream().map(emplDepartMapper::map).toList();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(responseEmplDeparts).toString());
        } else {
            List<ResponseEmplDepart> responseEmplDeparts;
            try {
                responseEmplDeparts = service.getAll().stream().map(emplDepartMapper::map).toList();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(responseEmplDeparts).toString());
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
}
