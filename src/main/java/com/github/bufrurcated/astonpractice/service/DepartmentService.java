package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.Dao;
import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.errors.DepartmentNotFoundError;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;
import com.github.bufrurcated.astonpractice.errors.SQLError;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class DepartmentService {
    private final Dao<Department, Long> dao;

    public DepartmentService(Dao<Department, Long> dao) {
        this.dao = dao;
    }

    public void add(Department department) {
        try {
            dao.save(department);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public Department getById(Long id) {
        try {
            return dao.find(id).getFirst();
        } catch (NotFoundSQLException e) {
            throw new DepartmentNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<Department> getAll() {
        try {
            return dao.findAll();
        } catch (NotFoundSQLException e) {
            throw new DepartmentNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void update(Department department) {
        try {
            dao.update(department);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeById(Long id) {
        try {
            dao.delete(id);
        } catch (NotFoundSQLException e) {
            throw new DepartmentNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeAll() {
        try {
            dao.deleteAll();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }
}
