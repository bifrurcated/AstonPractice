package com.github.bufrurcated.astonpractice.errors;

import jakarta.servlet.http.HttpServletResponse;

public class EmplDepartNotFoundError extends ResponseStatusException {
    public EmplDepartNotFoundError() {
        super(HttpServletResponse.SC_NOT_FOUND, "employee department not found");
    }
}
