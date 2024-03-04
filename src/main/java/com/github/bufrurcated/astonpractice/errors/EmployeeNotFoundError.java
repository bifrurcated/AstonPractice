package com.github.bufrurcated.astonpractice.errors;

import jakarta.servlet.http.HttpServletResponse;

public class EmployeeNotFoundError extends ResponseStatusException {
    public EmployeeNotFoundError() {
        super(HttpServletResponse.SC_NOT_FOUND, "employee not found");
    }
}
