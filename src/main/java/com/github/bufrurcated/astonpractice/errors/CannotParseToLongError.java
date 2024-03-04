package com.github.bufrurcated.astonpractice.errors;

import jakarta.servlet.http.HttpServletResponse;

public class CannotParseToLongError extends ResponseStatusException {
    public CannotParseToLongError(String parseValue) {
        super(HttpServletResponse.SC_BAD_REQUEST, "Cannot parse a " + parseValue + " into a Long" );
    }
}
