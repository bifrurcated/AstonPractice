package com.github.bufrurcated.astonpractice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PhoneNumberNotFoundError extends ResponseStatusException {
    public PhoneNumberNotFoundError() {
        super(HttpStatus.NOT_FOUND, "phone number not found");
    }
}
