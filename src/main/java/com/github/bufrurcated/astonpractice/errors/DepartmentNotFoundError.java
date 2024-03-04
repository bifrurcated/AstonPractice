package com.github.bufrurcated.astonpractice.errors;

import jakarta.servlet.http.HttpServletResponse;

public class DepartmentNotFoundError extends ResponseStatusException {
    public DepartmentNotFoundError() {
        super(HttpServletResponse.SC_NOT_FOUND, "department not found");
    }
}
