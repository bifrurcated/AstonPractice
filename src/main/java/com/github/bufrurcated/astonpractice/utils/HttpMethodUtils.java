package com.github.bufrurcated.astonpractice.utils;

import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class HttpMethodUtils {

    public static void execute(RunnableResponse method, HttpServletResponse resp) throws IOException {
        try {
            method.execute();
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static  <T> void execute(ConsumerResponse<T> method, T val, HttpServletResponse resp) throws IOException {
        try {
            method.accept(val);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static <T, R> void getAll(
            SupplierResponse<List<? extends T>> method,
            Function<T, R> mapper,
            HttpServletResponse resp) throws IOException {
        List<? extends R> responseList;
        try {
            responseList = method.get().stream().map(mapper).toList();
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(new JSONArray(responseList).toString());
    }

    public static <T, R, V> void getAll(
            FunctionResponse<T, List<? extends R>> method,
            T id,
            Function<R, V> mapper,
            HttpServletResponse resp) throws IOException {
        List<? extends V> responseList;
        try {
            responseList = method.apply(id).stream().map(mapper).toList();
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(new JSONArray(responseList).toString());
    }

    public static <T, R, V> void get(
            FunctionResponse<T, R> method,
            T id,
            Function<R, V> mapper,
            HttpServletResponse resp) throws IOException {
        R r;
        try {
            r = method.apply(id);
        } catch (ResponseStatusException exception) {
            resp.sendError(exception.getStatus(), exception.getReason());
            throw new RuntimeException(exception.getMessage());
        }
        var responseDTO = mapper.apply(r);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(new JSONObject(responseDTO).toString());
    }

    public static <T, R, V> void doGet(
            HttpServletRequest req,
            FunctionResponse<T, R> func,
            SupplierResponse<List<? extends R>> supp,
            Function<R, V> mapper,
            HttpServletResponse resp) throws IOException {
        var pathInfo = Optional.ofNullable(req.getPathInfo());
        if (pathInfo.isPresent()) {
            var strId = pathInfo.get().substring(1);
            T id = (T) Long.valueOf(Parse.stringToLong(resp, strId));
            HttpMethodUtils.get(func, id, mapper, resp);
        } else {
            HttpMethodUtils.getAll(supp, mapper, resp);
        }
    }

}
