package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.PhoneNumberMapper;
import com.github.bufrurcated.astonpractice.service.PhoneNumberService;
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
public class PhoneNumberServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = -591486146698200784L;

    private final PhoneNumberService service;
    private final PhoneNumberMapper phoneNumberMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var phoneNumberId = Optional.ofNullable(req.getParameterMap().get("id"));
        var employeeId = Optional.ofNullable(req.getParameterMap().get("employeeId"));
        if (phoneNumberId.isPresent()) {
            var strId = phoneNumberId.get()[0];
            long id = Parse.stringToLong(resp, strId);
            HttpMethodUtils.get(service::getById, id, phoneNumberMapper::map, resp);
        } else if (employeeId.isPresent()) {
            var strId = employeeId.get()[0];
            long id = Parse.stringToLong(resp, strId);
            HttpMethodUtils.getAll(service::getByEmployeeId, id, phoneNumberMapper::map, resp);
        } else {
            HttpMethodUtils.getAll(service::getAll, phoneNumberMapper::map, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestCreatePhoneNumber = Parse.jsonToCreatePhoneNumber(resp, body);
        var phoneNumber = phoneNumberMapper.map(requestCreatePhoneNumber);
        HttpMethodUtils.execute(service::add, phoneNumber, resp);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write("Phone number created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = Parse.getBody(req);
        var requestUpdatePhoneNumber = Parse.jsonToUpdatePhoneNumber(resp, body);
        var phoneNumber = phoneNumberMapper.map(requestUpdatePhoneNumber);
        HttpMethodUtils.execute(service::update, phoneNumber, resp);
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
            HttpMethodUtils.execute(service::removeById, id, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Phone number deleted");
        } else if (employeeId.isPresent()) {
            var strId = employeeId.get()[0];
            long id = Parse.stringToLong(resp, strId);
            HttpMethodUtils.execute(service::removeByEmployeeId, id, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Phone numbers by employee deleted");
        } else {
            HttpMethodUtils.execute(service::removeAll, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Phone numbers deleted");
        }
    }
}
