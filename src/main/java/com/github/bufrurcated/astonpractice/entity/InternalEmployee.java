package com.github.bufrurcated.astonpractice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "internal_employee")
public class InternalEmployee extends Employee {
    @Column(name = "city", nullable = true, length = 16)
    private String city;
    @Column(name = "street", nullable = false, length = 32)
    private String street;
    @Column(name = "humber", nullable = false)
    private Integer streetNumber;

    @Builder(builderMethodName = "IEBuilder")
    public InternalEmployee(Long id, String firstName, String lastName, int age, String city, String street, Integer streetNumber) {
        super(id, firstName, lastName, age);
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }
}
