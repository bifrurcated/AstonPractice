package com.github.bufrurcated.astonpractice.utils;

import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;

public interface ConsumerResponse<T> {
    void accept(T t) throws ResponseStatusException;
}
