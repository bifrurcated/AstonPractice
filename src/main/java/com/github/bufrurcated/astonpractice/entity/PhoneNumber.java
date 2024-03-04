package com.github.bufrurcated.astonpractice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhoneNumber {
    private Long id;
    private String phoneNumber;
    private Long employeeId;
}
