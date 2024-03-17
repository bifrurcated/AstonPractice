package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class EmployeeSpicificFindDAO extends AbstractDao<Employee, Long> {

    public EmployeeSpicificFindDAO(SessionFactory sessionFactory) {
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

    @Override
    public void update(Employee employee) throws SQLException {
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            session.merge(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
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
