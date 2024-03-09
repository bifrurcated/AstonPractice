package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.dto.FindNumber;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;
import com.github.bufrurcated.astonpractice.errors.EmployeeNotFoundSQLException;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import jdk.jshell.spi.ExecutionControlProvider;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cache.internal.EnabledCaching;
import org.hibernate.query.NativeQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PhoneNumberDAO extends AbstractDao implements Dao<PhoneNumber, FindNumber> {
    public PhoneNumberDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void save(PhoneNumber phoneNumber) throws SQLException {
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            var employee = session.get(Employee.class, phoneNumber.getEmployee().getId());
            if (employee == null) {
                tx.rollback();
                throw new EmployeeNotFoundSQLException();
            }
            phoneNumber.setEmployee(employee);
            session.persist(phoneNumber);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<PhoneNumber> findAll() throws SQLException {
        try (var session = openSession()) {
            String sql = "SELECT id, phone_number, employee_id FROM phone_numbers";
            var query = session.createNativeQuery(sql, PhoneNumber.class);
            var results = query.list();
            if (results.isEmpty()) {
                throw new NotFoundSQLException();
            }
            return results;
        }
    }

    @Override
    public List<PhoneNumber> find(FindNumber find) throws SQLException {
        try (var session = openSession()) {
            Result result = getResult(find, "SELECT id, phone_number, employee_id FROM phone_numbers WHERE");
            var query = session.createNativeQuery(result.sql(), PhoneNumber.class);
            var results = query.setParameter(1, result.id()).list();
            if (results.isEmpty()) {
                throw new NotFoundSQLException();
            }
            return results;
        }
    }

    private static Result getResult(FindNumber find, String query) {
        StringBuilder sql = new StringBuilder(query);
        Long id;
        if (find.id() != null) {
            sql.append(" id = ?");
            id = find.id();
        } else {
            sql.append(" employee_id = ?");
            id = find.employeeId();
        }
        return new Result(sql.toString(), id);
    }

    private record Result(String sql, Long id) {
    }

    @Override
    public void update(PhoneNumber phoneNumber) throws SQLException {
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            session.merge(phoneNumber);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(FindNumber find) throws SQLException {
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            String sql = "DELETE FROM phone_numbers WHERE";
            Result result = getResult(find, sql);
            var query = session.createNativeQuery(result.sql(), Void.class);
            var rowsDeleted = query.setParameter(1, result.id()).executeUpdate();
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

    @Override
    public void deleteAll() throws SQLException {
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            String sql = "DELETE FROM phone_numbers";
            var query = session.createNativeQuery(sql, Void.class);
            var rowsDeleted = query.executeUpdate();
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
