package com.github.bufrurcated.astonpractice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
}
