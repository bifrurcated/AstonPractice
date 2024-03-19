package com.github.bufrurcated.astonpractice.utils;

import com.github.bufrurcated.astonpractice.errors.ResponseStatusException;

public interface SupplierResponse<T> {
    T get() throws ResponseStatusException;
}
