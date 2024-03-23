package com.github.bufrurcated.astonpractice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmployeeNotFoundError extends ResponseStatusException {
    public EmployeeNotFoundError() {
        super(HttpStatus.NOT_FOUND, "employee not found");
    }
}
