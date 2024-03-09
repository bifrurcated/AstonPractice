package com.github.bufrurcated.astonpractice.dto;

public record RequestUpdatePhoneNumber(
        Long id,
        Long employeeId,
        String phoneNumber
) {
}
