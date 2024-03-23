package com.github.bufrurcated.astonpractice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CannotParseFromJsonError extends ResponseStatusException {
    public CannotParseFromJsonError(String json, String errorMessage) {
        super(HttpStatus.BAD_REQUEST, "Cannot parse " + json + ", error: " + errorMessage);
    }
}
