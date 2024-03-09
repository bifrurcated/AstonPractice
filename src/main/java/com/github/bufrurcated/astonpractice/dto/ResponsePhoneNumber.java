package com.github.bufrurcated.astonpractice.dto;

import org.json.JSONPropertyName;

public record ResponsePhoneNumber(
        @JSONPropertyName("id") Long id,
        @JSONPropertyName("employee_id") Long employeeId,
        @JSONPropertyName("phone_number") String phoneNumber) {
}
