package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.Dao;
import com.github.bufrurcated.astonpractice.dao.EmployeeDAO;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.*;
import jakarta.transaction.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private final Dao<Employee, Long> dao;

    public EmployeeService(Dao<Employee, Long> dao) {
        this.dao = dao;
    }

    @Transactional
    public void add(Employee employee) throws ResponseStatusException {
        try {
            dao.save(employee);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public Employee getById(Long id) throws ResponseStatusException {
        try {
            return dao.find(id).getFirst();
        } catch (NotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<Employee> getAll() throws ResponseStatusException {
        try {
            return dao.findAll();
        } catch (NotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void update(Employee employee) throws ResponseStatusException {
        try {
            dao.update(employee);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeById(Long id) throws ResponseStatusException {
        try {
            dao.delete(id);
        } catch (NotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeAll() throws ResponseStatusException {
        try {
            dao.deleteAll();
        } catch (NotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }
}
