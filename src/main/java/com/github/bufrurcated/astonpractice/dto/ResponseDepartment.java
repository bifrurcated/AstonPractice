package com.github.bufrurcated.astonpractice.dto;

import org.json.JSONPropertyName;

public record ResponseDepartment(
        @JSONPropertyName("name") String name) {
}
