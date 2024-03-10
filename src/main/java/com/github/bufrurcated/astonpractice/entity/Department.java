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
@Table(name = "departments")
public class Department extends AbstractEntity {
    @Column(name = "dpt_name", nullable = false)
    private String name;
    @ManyToMany(mappedBy = "departments", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Employee> employees = new ArrayList<>();

    @Builder
    public Department(Long id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy h ? h.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy h ? h.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Department department = (Department) o;
        return getId() != null && Objects.equals(getId(), department.getId());
    }
}
