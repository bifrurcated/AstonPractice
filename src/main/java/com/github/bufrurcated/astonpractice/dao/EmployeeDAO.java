package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeDAO extends AbstractDao<Employee, Long> {

    public EmployeeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void save(Employee employee) throws SQLException {
        super.save(employee);
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        try (var session = openSession()) {
            //var sql = "SELECT id, first_name, last_name, age FROM employees";
            var hql = "FROM Employee e ORDER BY e.id";
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
            var result = session.get(Employee.class, id);
            if (result == null) {
                throw new NotFoundSQLException();
            }
            return List.of(result);
        }
    }

    @Override
    public void update(Employee employee) throws SQLException {
        super.update(employee);
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (var session = openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                var sql1 = "DELETE FROM employee_department WHERE employee_id = ?";
                var sql2 = "DELETE FROM phone_numbers WHERE employee_id = ?";
                var sql3 = "DELETE FROM employees WHERE id = ?";
                var query1 = session.createNativeQuery(sql1, Void.class).setParameter(1, id);
                var query2 = session.createNativeQuery(sql2, Void.class).setParameter(1, id);
                var query3 = session.createNativeQuery(sql3, Void.class).setParameter(1, id);
                query1.executeUpdate();
                query2.executeUpdate();
                var rowsDeleted = query3.executeUpdate();
                if (rowsDeleted == 0) {
                    tx.rollback();
                    throw new NotFoundSQLException();
                }
                tx.commit();
            } catch (SQLException exception) {
                tx.rollback();
                throw new SQLException(exception);
            }
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        try (var session = openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                var sql1 = "DELETE FROM employee_department";
                var sql2 = "DELETE FROM phone_numbers";
                var sql3 = "DELETE FROM employees";
                var query1 = session.createNativeQuery(sql1, Void.class);
                var query2 = session.createNativeQuery(sql2, Void.class);
                var query3 = session.createNativeQuery(sql3, Void.class);
                query1.executeUpdate();
                query2.executeUpdate();
                var rowsDeleted = query3.executeUpdate();
                if (rowsDeleted == 0) {
                    tx.rollback();
                    throw new NotFoundSQLException();
                }
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }
}
