package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.Dao;
import com.github.bufrurcated.astonpractice.entity.EmplDepart;
import com.github.bufrurcated.astonpractice.errors.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class EmplDepartService {
    private final Dao<EmplDepart, EmplDepart> dao;

    public EmplDepartService(Dao<EmplDepart, EmplDepart> dao) {
        this.dao = dao;
    }

    public void add(EmplDepart emplDepart) throws ResponseStatusException {
        try {
            dao.save(emplDepart);
        } catch (EmployeeNotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (DepartmentNotFoundSQLException e) {
            throw new DepartmentNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<EmplDepart> get(EmplDepart emplDepart) throws ResponseStatusException {
        try {
            return dao.find(emplDepart);
        } catch (NotFoundSQLException e) {
            throw new EmplDepartNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<EmplDepart> getAll() throws ResponseStatusException {
        try {
            return dao.findAll();
        } catch (NotFoundSQLException e) {
            throw new EmplDepartNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void update(EmplDepart emplDepart) throws ResponseStatusException {
        try {
            dao.update(emplDepart);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void remove(EmplDepart emplDepart) throws ResponseStatusException {
        try {
            dao.delete(emplDepart);
        } catch (NotFoundSQLException e) {
            throw new EmplDepartNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeAll() throws ResponseStatusException {
        try {
            dao.deleteAll();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }
}
