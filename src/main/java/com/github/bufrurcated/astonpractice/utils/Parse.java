package com.github.bufrurcated.astonpractice.utils;

import com.github.bufrurcated.astonpractice.dto.*;
import com.github.bufrurcated.astonpractice.errors.CannotParseFromJsonError;
import com.github.bufrurcated.astonpractice.errors.CannotParseToLongError;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.stream.Collectors;

public class Parse {

    public static RequestCreateEmployee jsonToCreateEmployee(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestCreateEmployee(
                    jsonObject.getString("first_name"),
                    jsonObject.getString("last_name"),
                    jsonObject.getInt("age")
            );
        } catch (JSONException exception) {
            throw new CannotParseFromJsonError(json, exception.getMessage());
        }
    }

    public static RequestUpdateEmployee jsonToUpdateEmployee(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestUpdateEmployee(
                    jsonObject.getLong("id"),
                    jsonObject.getString("first_name"),
                    jsonObject.getString("last_name"),
                    jsonObject.getInt("age")
            );
        } catch (JSONException exception) {
            throw new CannotParseFromJsonError(json, exception.getMessage());
        }
    }

    public static RequestCreateDepartment jsonToCreateDepartment(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestCreateDepartment(
                    jsonObject.getString("name")
            );
        } catch (JSONException exception) {
            throw new CannotParseFromJsonError(json, exception.getMessage());
        }
    }

    public static RequestUpdateDepartment jsonToUpdateDepartment(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestUpdateDepartment(
                    jsonObject.getLong("id"),
                    jsonObject.getString("name")
            );
        } catch (JSONException exception) {
            throw new CannotParseFromJsonError(json, exception.getMessage());
        }
    }

    public static RequestEmplDepart jsonToEmplDepart(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            Long employeeId = null;
            Long departmentId = null;
            if (!jsonObject.isNull("employee_id")) {
                employeeId = jsonObject.getLong("employee_id");
            }
            if (!jsonObject.isNull("department_id")) {
                departmentId = jsonObject.getLong("department_id");
            }
            return new RequestEmplDepart(
                    employeeId,
                    departmentId
            );
        } catch (JSONException exception) {
            throw new CannotParseFromJsonError(json, exception.getMessage());
        }
    }

    public static RequestCreatePhoneNumber jsonToCreatePhoneNumber(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestCreatePhoneNumber(
                    jsonObject.getString("phone_number"),
                    jsonObject.getLong("employee_id")
            );
        } catch (JSONException exception) {
            throw new CannotParseFromJsonError(json, exception.getMessage());
        }
    }

    public static RequestUpdatePhoneNumber jsonToUpdatePhoneNumber(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new RequestUpdatePhoneNumber(
                    jsonObject.getLong("id"),
                    jsonObject.getLong("employee_id"),
                    jsonObject.getString("phone_number")
            );
        } catch (JSONException exception) {
            throw new CannotParseFromJsonError(json, exception.getMessage());
        }
    }
}
