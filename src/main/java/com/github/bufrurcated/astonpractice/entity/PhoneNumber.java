package com.github.bufrurcated.astonpractice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "phone_numbers")
public class PhoneNumber extends AbstractEntity {

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @ToString.Exclude
    public Employee employee;

    public PhoneNumber(Long id, String phoneNumber) {
        this(id, phoneNumber, null);
    }

    @Builder
    public PhoneNumber(Long id, String phoneNumber, Employee employee) {
        super(id);
        this.phoneNumber = phoneNumber;
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy h ? h.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy h ? h.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PhoneNumber ph = (PhoneNumber) o;
        return getId() != null && Objects.equals(getId(), ph.getId());
    }
}
