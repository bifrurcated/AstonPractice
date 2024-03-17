package com.github.bufrurcated.astonpractice.errors;

import jakarta.servlet.http.HttpServletResponse;

public class CannotParseFromJsonError extends ResponseStatusException {
    public CannotParseFromJsonError(String json, String errorMessage) {
        super(HttpServletResponse.SC_BAD_REQUEST, "Cannot parse " + json + ", error: " + errorMessage);
    }
}
