package com.github.bufrurcated.astonpractice.errors;

import jakarta.servlet.http.HttpServletResponse;

public class PhoneNumberNotFoundError extends ResponseStatusException {
    public PhoneNumberNotFoundError() {
        super(HttpServletResponse.SC_NOT_FOUND, "phone number not found");
    }
}
