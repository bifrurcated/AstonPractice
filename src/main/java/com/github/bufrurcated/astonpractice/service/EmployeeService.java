package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.Dao;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.EmployeeNotFoundError;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import com.github.bufrurcated.astonpractice.errors.SQLError;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class EmployeeService {
    private final Dao<Employee, Long> dao;

    public EmployeeService(Dao<Employee, Long> dao) {
        this.dao = dao;
    }

    public void add(Employee employee) {
        try {
            dao.save(employee);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public Employee getById(Long id) {
        try {
            return dao.find(id).getFirst();
        } catch (NotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<Employee> getAll() {
        try {
            return dao.findAll();
        } catch (NotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void update(Employee employee) {
        try {
            dao.update(employee);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeById(Long id) {
        try {
            dao.delete(id);
        } catch (NotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeAll() {
        try {
            dao.deleteAll();
        } catch (NotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }
}
