package com.github.bufrurcated.astonpractice.dto;

import org.json.JSONPropertyName;

public record ResponseEmplDepart(
        @JSONPropertyName("employee_id") Long employee_id,
        @JSONPropertyName("department_id") Long department_id) {
}
