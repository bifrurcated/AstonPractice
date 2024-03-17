package com.github.bufrurcated.astonpractice.service;

import com.github.bufrurcated.astonpractice.dao.Dao;
import com.github.bufrurcated.astonpractice.dto.FindNumber;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;
import com.github.bufrurcated.astonpractice.errors.*;

import java.sql.SQLException;
import java.util.List;

public class PhoneNumberService {

    private final Dao<PhoneNumber, FindNumber> dao;

    public PhoneNumberService(Dao<PhoneNumber, FindNumber> dao) {
        this.dao = dao;
    }

    public void add(PhoneNumber phoneNumber) throws ResponseStatusException {
        try {
            dao.save(phoneNumber);
        } catch (EmployeeNotFoundSQLException e) {
            throw new EmployeeNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public PhoneNumber getById(Long id) throws ResponseStatusException {
        try {
            return dao.find(new FindNumber(id, null)).getFirst();
        } catch (NotFoundSQLException e) {
            throw new PhoneNumberNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<PhoneNumber> getByEmployeeId(Long employeeId) throws ResponseStatusException {
        try {
            return dao.find(new FindNumber(null, employeeId));
        } catch (NotFoundSQLException e) {
            throw new PhoneNumberNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<PhoneNumber> getAll() throws ResponseStatusException {
        try {
            return dao.findAll();
        } catch (NotFoundSQLException e) {
            throw new PhoneNumberNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void update(PhoneNumber phoneNumber) throws ResponseStatusException {
        try {
            dao.update(phoneNumber);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeById(Long id) throws ResponseStatusException {
        try {
            dao.delete(new FindNumber(id, null));
        } catch (NotFoundSQLException e) {
            throw new PhoneNumberNotFoundError();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public void removeByEmployeeId(Long employeeId) throws ResponseStatusException {
        try {
            dao.delete(new FindNumber(null, employeeId));
        } catch (NotFoundSQLException e) {
            throw new PhoneNumberNotFoundError();
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
