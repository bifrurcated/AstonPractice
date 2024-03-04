package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.dao.PhoneNumberDAO;
import com.github.bufrurcated.astonpractice.db.DatabaseSource;
import com.github.bufrurcated.astonpractice.dto.RequestCreatePhoneNumber;
import com.github.bufrurcated.astonpractice.dto.RequestUpdateEmployee;
import com.github.bufrurcated.astonpractice.dto.RequestUpdatePhoneNumber;
import com.github.bufrurcated.astonpractice.dto.ResponsePhoneNumber;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;
import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;
import com.github.bufrurcated.astonpractice.mapper.PhoneNumberMapper;
import com.github.bufrurcated.astonpractice.service.PhoneNumberService;
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

@WebServlet(urlPatterns = {"/api/v1/phone-numbers/*"})
public class PhoneNumberServlet extends HttpServlet {
    private PhoneNumberService service;
    private DatabaseSource databaseSource;
    private final PhoneNumberMapper phoneNumberMapper = new PhoneNumberMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        databaseSource = new DatabaseSource();
        var connection = databaseSource.getConnection();
        var dao = new PhoneNumberDAO(connection);
        service = new PhoneNumberService(dao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var phoneNumberId = Optional.ofNullable(req.getParameterMap().get("id"));
        var employeeId = Optional.ofNullable(req.getParameterMap().get("employeeId"));
        if (phoneNumberId.isPresent()) {
            var strId = phoneNumberId.get()[0];
            long id = Parse.stringToLong(resp, strId);
            PhoneNumber phoneNumber;
            try {
                phoneNumber = service.getById(id);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            var responsePhoneNumber = phoneNumberMapper.map(phoneNumber);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONObject(responsePhoneNumber).toString());
        } else if (employeeId.isPresent()) {
            var strId = employeeId.get()[0];
            long id = Parse.stringToLong(resp, strId);
            List<PhoneNumber> phoneNumbers;
            try {
                phoneNumbers = service.getByEmployeeId(id);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(phoneNumbers).toString());
        } else {
            List<PhoneNumber> phoneNumbers;
            try {
                phoneNumbers = service.getAll();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            var phoneNumberList = phoneNumbers.stream().map(phoneNumberMapper::map).toList();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new JSONArray(phoneNumberList).toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestCreatePhoneNumber = Parse.jsonToCreatePhoneNumber(resp, body);
        var phoneNumber = phoneNumberMapper.map(requestCreatePhoneNumber);
        try {
            service.add(phoneNumber);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Phone number created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestUpdatePhoneNumber = Parse.jsonToUpdatePhoneNumber(resp, body);
        var phoneNumber = phoneNumberMapper.map(requestUpdatePhoneNumber);
        try {
            service.update(phoneNumber);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Phone number update");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var phoneNumberId = Optional.ofNullable(req.getParameterMap().get("id"));
        var employeeId = Optional.ofNullable(req.getParameterMap().get("employeeId"));
        if (phoneNumberId.isPresent()) {
            var strId = phoneNumberId.get()[0];
            long id = Parse.stringToLong(resp, strId);
            try {
                service.removeById(id);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Phone number deleted");
        } else if (employeeId.isPresent()) {
            var strId = employeeId.get()[0];
            long id = Parse.stringToLong(resp, strId);
            try {
                service.removeByEmployeeId(id);
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Phone numbers by employee deleted");
        } else {
            try {
                service.removeAll();
            } catch (ResponseStatusException exception) {
                resp.sendError(exception.getStatus(), exception.getReason());
                throw new RuntimeException(exception.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Phone numbers deleted");
        }
    }

    @Override
    public void destroy() {
        databaseSource.shutdown();
    }
}
