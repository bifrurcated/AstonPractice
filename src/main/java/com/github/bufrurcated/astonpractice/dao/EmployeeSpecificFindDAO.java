package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.util.List;

public class EmployeeSpecificFindDAO extends EmployeeDAO {

    public EmployeeSpecificFindDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void save(Employee employee) throws SQLException {
        super.save(employee);
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        try (var session = openSession()) {
            var hql = "SELECT e FROM Employee e WHERE e.age > 2000";
            var query = session.createQuery(hql, Employee.class);
            query.setCacheable(true);
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
            String hql = "SELECT e FROM Employee e WHERE e.id = :id AND age > 2000";
            var employees = session.createQuery(hql, Employee.class)
                    .setParameter("id", id)
                    .setHint("org.hibernate.cacheable", true)
                    .list();
            if (employees.isEmpty()) {
                throw new NotFoundSQLException();
            }
            return employees;
        }
    }
}
