package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.entity.EmplDepart;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.DepartmentNotFoundSQLException;
import com.github.bufrurcated.astonpractice.errors.EmployeeNotFoundSQLException;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class EmplDepartN1DAO extends AbstractDao<EmplDepart, EmplDepart> {

    public EmplDepartN1DAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void save(EmplDepart emplDepart) throws SQLException {
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            var dpt = session.get(Department.class, emplDepart.getDepartmentId());
            if (dpt == null) {
                tx.rollback();
                throw new DepartmentNotFoundSQLException();
            }
            var emp = session.get(Employee.class, emplDepart.getEmployeeId());
            if (emp == null) {
                tx.rollback();
                throw new EmployeeNotFoundSQLException();
            }
            //Дополнительно создаётся запрос на вытягивание коллекции
            emp.getDepartments().add(dpt);
            session.merge(emp);
            tx.commit();
        } catch (Exception e) {
            log.error(e.getMessage());
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<EmplDepart> findAll() throws SQLException {
        try (var session = openSession()) {
            var sql = "SELECT employee_id, department_id FROM employee_department";
            var query = session.createNativeQuery(sql, EmplDepart.class);
            var results = query.list();
            if (results.isEmpty()) {
                throw new NotFoundSQLException();
            }
            return results;
        }
    }

    @Override
    public List<EmplDepart> find(EmplDepart emplDepart) throws SQLException {
        try (var session = openSession()) {
            StringBuilder sql = new StringBuilder("SELECT employee_id, department_id FROM employee_department WHERE");
            Long id;
            if (emplDepart.getEmployeeId() != null) {
                sql.append(" employee_id = ?");
                id = emplDepart.getEmployeeId();
            } else {
                sql.append(" department_id = ?");
                id = emplDepart.getDepartmentId();
            }
            var query = session.createNativeQuery(sql.toString(), EmplDepart.class);
            var results = query.setParameter(1, id).list();
            if (results.isEmpty()) {
                throw new NotFoundSQLException();
            }
            return results;
        }
    }

    @Override
    public void update(EmplDepart emplDepart) throws SQLException {
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            var sql = "UPDATE employee_department SET employee_id = ? AND department_id = ?";
            var query = session.createNativeQuery(sql, Void.class);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(EmplDepart emplDepart) throws SQLException {
        Transaction tx = null;
        try (var session = openSession()) {
            tx = session.beginTransaction();
            var sql = "DELETE FROM employee_department WHERE employee_id = ? AND department_id = ?";
            var query = session.createNativeQuery(sql, Void.class);
            var rowDeleted = query
                    .setParameter(1, emplDepart.getEmployeeId())
                    .setParameter(2, emplDepart.getDepartmentId())
                    .executeUpdate();
            if (rowDeleted == 0) {
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
            var sql = "DELETE FROM employee_department";
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
