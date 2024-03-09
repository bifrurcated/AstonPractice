package com.github.bufrurcated.astonpractice.dto;

import org.json.JSONPropertyName;

public record ResponseDepartment(
        @JSONPropertyName("id") Long id,
        @JSONPropertyName("name") String name) {
}
