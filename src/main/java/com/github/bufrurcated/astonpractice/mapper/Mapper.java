package com.github.bufrurcated.astonpractice.mapper;

public interface Mapper<T, R> {
    R map(T t);
}
