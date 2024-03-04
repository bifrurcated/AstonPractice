package com.github.bufrurcated.astonpractice.dto;

import org.json.JSONPropertyName;

public record ResponseEmployee(
        @JSONPropertyName("first_name") String firstName,
        @JSONPropertyName("last_name") String lastName,
        @JSONPropertyName("age") Integer age) {
}
