package com.github.bufrurcated.astonpractice.dto;

public record RequestUpdateEmployee(
        Long id,
        String firstName,
        String lastName,
        Integer age) {
}
