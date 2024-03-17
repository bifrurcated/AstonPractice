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
public class EmplDepartN1DAO extends EmplDepartDAO {

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
}
