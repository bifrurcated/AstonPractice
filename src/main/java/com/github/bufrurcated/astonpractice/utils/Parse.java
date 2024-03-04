package com.github.bufrurcated.astonpractice.utils;

import com.github.bufrurcated.astonpractice.dto.*;
import com.github.bufrurcated.astonpractice.errors.CannotParseFromJsonError;
import com.github.bufrurcated.astonpractice.errors.CannotParseToLongError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.stream.Collectors;

public class Parse {
    public static long stringToLong(HttpServletResponse resp, String value) throws IOException {
        long id;
        try {
            id = Long.parseLong(value);
        } catch (NumberFormatException e) {
            var error = new CannotParseToLongError(value);
            resp.sendError(error.getStatus(), error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
        return id;
    }

    public static String getBody(HttpServletRequest req) throws IOException {
        return req.getReader().lines().collect(Collectors.joining());
    }

    public static RequestCreateEmployee jsonToCreateEmployee(HttpServletResponse resp, String json) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestCreateEmployee(
                    jsonObject.getString("first_name"),
                    jsonObject.getString("last_name"),
                    jsonObject.getInt("age")
            );
        } catch (JSONException exception) {
            var error = new CannotParseFromJsonError(json, exception.getMessage());
            resp.sendError(error.getStatus(), error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    public static RequestUpdateEmployee jsonToUpdateEmployee(HttpServletResponse resp, String json) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestUpdateEmployee(
                    jsonObject.getLong("id"),
                    jsonObject.getString("first_name"),
                    jsonObject.getString("last_name"),
                    jsonObject.getInt("age")
            );
        } catch (JSONException exception) {
            var error = new CannotParseFromJsonError(json, exception.getMessage());
            resp.sendError(error.getStatus(), error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    public static RequestCreateDepartment jsonToCreateDepartment(HttpServletResponse resp, String json) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestCreateDepartment(
                    jsonObject.getString("name")
            );
        } catch (JSONException exception) {
            var error = new CannotParseFromJsonError(json, exception.getMessage());
            resp.sendError(error.getStatus(), error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    public static RequestUpdateDepartment jsonToUpdateDepartment(HttpServletResponse resp, String json) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestUpdateDepartment(
                    jsonObject.getLong("id"),
                    jsonObject.getString("name")
            );
        } catch (JSONException exception) {
            var error = new CannotParseFromJsonError(json, exception.getMessage());
            resp.sendError(error.getStatus(), error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    public static RequestEmplDepart jsonToEmplDepart(HttpServletResponse resp, String json) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestEmplDepart(
                    jsonObject.getLong("employee_id"),
                    jsonObject.getLong("department_id")
            );
        } catch (JSONException exception) {
            var error = new CannotParseFromJsonError(json, exception.getMessage());
            resp.sendError(error.getStatus(), error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    public static RequestCreatePhoneNumber jsonToCreatePhoneNumber(HttpServletResponse resp, String json) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestCreatePhoneNumber(
                    jsonObject.getString("phone_number"),
                    jsonObject.getLong("employee_id")
            );
        } catch (JSONException exception) {
            var error = new CannotParseFromJsonError(json, exception.getMessage());
            resp.sendError(error.getStatus(), error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    public static RequestUpdatePhoneNumber jsonToUpdatePhoneNumber(HttpServletResponse resp, String json) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestUpdatePhoneNumber(
                    jsonObject.getString("phone_number"),
                    jsonObject.getLong("employee_id")
            );
        } catch (JSONException exception) {
            var error = new CannotParseFromJsonError(json, exception.getMessage());
            resp.sendError(error.getStatus(), error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }
}
