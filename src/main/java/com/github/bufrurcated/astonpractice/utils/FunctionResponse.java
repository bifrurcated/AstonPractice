package com.github.bufrurcated.astonpractice.utils;

import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;

public interface FunctionResponse<T, R> {
    R apply(T t) throws ResponseStatusException;
}
