package com.github.bufrurcated.astonpractice.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class ResponseStatusException extends Exception {
    private final int status;
    private final String reason;

    public ResponseStatusException(int status, String reason) {
        super(reason);
        this.status = status;
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return getStatus() + (this.reason != null ? " \"" + this.reason + "\"" : "");
    }
}
