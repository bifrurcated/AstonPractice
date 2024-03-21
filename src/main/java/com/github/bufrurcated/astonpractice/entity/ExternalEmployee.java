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
@Table(name = "external_employee")
public class ExternalEmployee extends Employee {
    @Column(name = "country", nullable = false, length = 16)
    private String country;
    @Column(name = "city", nullable = true, length = 16)
    private String city;
    @Column(name = "street", nullable = false, length = 32)
    private String street;
    @Column(name = "humber", nullable = false)
    private Integer streetNumber;

    @Builder(builderMethodName = "EEBuilder")
    public ExternalEmployee(Long id, String firstName, String lastName, int age, String country, String city, String street, Integer streetNumber) {
        super(id, firstName, lastName, age);
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }
}
