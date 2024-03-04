package com.github.bufrurcated.astonpractice.errors;

import jakarta.servlet.http.HttpServletResponse;

public class SQLError extends ResponseStatusException {
    public SQLError(String message) {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }
}
