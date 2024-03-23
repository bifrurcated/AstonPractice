package com.github.bufrurcated.astonpractice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DepartmentNotFoundError extends ResponseStatusException {
    public DepartmentNotFoundError() {
        super(HttpStatus.NOT_FOUND, "department not found");
    }
}
