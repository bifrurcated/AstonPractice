package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.List;

public class DepartmentDAO extends AbstractDao implements Dao<Department, Long> {

    public DepartmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void save(Department department) throws SQLException {
        super.save(department);
    }

    @Override
    public List<Department> findAll() throws SQLException {
        try (var session = openSession()) {
            var sql = "SELECT id, dpt_name FROM departments";
            var query = session.createNativeQuery(sql, Department.class);
            var list = query.list();
            if (list.isEmpty()) {
                throw new NotFoundSQLException();
            }
            return list;
        }
    }

    @Override
    public List<Department> find(Long id) throws SQLException {
        try (var session = openSession()) {
            var department = session.get(Department.class, id);
            if (department == null) {
                throw new NotFoundSQLException();
            }
            return List.of(department);
        }
    }

    @Override
    public void update(Department department) throws SQLException {
        super.update(department);
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (var session = openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                var sql1 = "DELETE FROM employee_department WHERE department_id = ?";
                var sql2 = "DELETE FROM departments WHERE id = ?";
                var query1 = session.createNativeQuery(sql1, Void.class).setParameter(1, id);
                var query2 = session.createNativeQuery(sql2, Void.class).setParameter(1, id);
                query1.executeUpdate();
                var rowsDeleted = query2.executeUpdate();
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
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            var sql1 = "DELETE FROM employee_department";
            var sql2 = "DELETE FROM departments";
            var query1 = session.createNativeQuery(sql1, Void.class);
            var query2 = session.createNativeQuery(sql2, Void.class);
            query1.executeUpdate();
            var rowsDeleted = query2.executeUpdate();
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
