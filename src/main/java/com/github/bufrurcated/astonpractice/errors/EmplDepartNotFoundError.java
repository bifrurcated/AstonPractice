package com.github.bufrurcated.astonpractice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmplDepartNotFoundError extends ResponseStatusException {
    public EmplDepartNotFoundError() {
        super(HttpStatus.NOT_FOUND, "employee department not found");
    }
}
