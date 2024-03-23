package com.github.bufrurcated.astonpractice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SQLError extends ResponseStatusException {
    public SQLError(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
