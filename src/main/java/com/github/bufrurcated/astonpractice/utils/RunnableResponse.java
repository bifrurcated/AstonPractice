package com.github.bufrurcated.astonpractice.utils;

import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;

public interface RunnableResponse {
    void execute() throws ResponseStatusException;
}
