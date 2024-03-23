package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.util.List;

public class EmployeeWithDepartmentsDAO extends EmployeeDAO {

    public EmployeeWithDepartmentsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        try (var session = openSession()) {
            var hql = "FROM Employee e LEFT JOIN FETCH e.departments ORDER BY e.id";
            var query = session.createQuery(hql, Employee.class);
            var results = query.list();
            if (results.isEmpty()) {
                throw new NotFoundSQLException();
            }
            return results;
        }
    }

    @Override
    public List<Employee> find(Long id) throws SQLException {
        try (var session = openSession()) {
            var hql = "SELECT e FROM Employee e LEFT JOIN FETCH e.departments WHERE e.id = :id";
            var result = session.createQuery(hql, Employee.class)
                    .setParameter("id", id)
                    .list();
            if (result.isEmpty()) {
                throw new NotFoundSQLException();
            }
            return result;
        }
    }
}
