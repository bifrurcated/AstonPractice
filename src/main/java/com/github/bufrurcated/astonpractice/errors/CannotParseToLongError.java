package com.github.bufrurcated.astonpractice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CannotParseToLongError extends ResponseStatusException {
    public CannotParseToLongError(String parseValue) {
        super(HttpStatus.BAD_REQUEST, "Cannot parse a " + parseValue + " into a Long" );
    }
}
