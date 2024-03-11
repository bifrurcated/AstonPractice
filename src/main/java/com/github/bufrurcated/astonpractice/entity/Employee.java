package com.github.bufrurcated.astonpractice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends AbstractEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "age", nullable = false)
    private int age;
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "employee_department",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "department_id", referencedColumnName = "id")
    )
    @ToString.Exclude
    private List<Department> departments = new ArrayList<>();

    @Builder
    public Employee(Long id, String firstName, String lastName, int age) {
        this(id, firstName, lastName, age, new ArrayList<>(), new ArrayList<>());
    }

    public Employee(Long id, String firstName, String lastName, int age, List<PhoneNumber> phoneNumbers, List<Department> departments) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumbers = phoneNumbers;
        this.departments = departments;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy h ? h.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy h ? h.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Employee employee = (Employee) o;
        return getId() != null && Objects.equals(getId(), employee.getId());
    }
}
