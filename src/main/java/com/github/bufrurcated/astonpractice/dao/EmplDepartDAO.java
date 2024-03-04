package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.EmplDepart;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmplDepartDAO extends AbstractDao implements Dao<EmplDepart, EmplDepart> {

    public EmplDepartDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void save(EmplDepart emplDepart) throws SQLException {
        String sql = "INSERT INTO employee_department (employee_id, department_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, emplDepart.getEmployeeId());
            preparedStatement.setLong(2, emplDepart.getDepartmentId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<EmplDepart> findAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT employee_id, department_id FROM employee_department";
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.isBeforeFirst()) {
                throw new NotFoundSQLException();
            }
            List<EmplDepart> emplDeparts = new ArrayList<>();
            while (resultSet.next()) {
                EmplDepart emplDepart = new EmplDepart();
                emplDepart.setEmployeeId(resultSet.getLong(1));
                emplDepart.setDepartmentId(resultSet.getLong(2));
                emplDeparts.add(emplDepart);
            }
            return emplDeparts;
        }
    }

    @Override
    public List<EmplDepart> find(EmplDepart emplDepart) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT employee_id, department_id FROM employee_department WHERE");
        Long id;
        if (emplDepart.getEmployeeId() != null) {
            sql.append(" employee_id = ?");
            id = emplDepart.getEmployeeId();
        } else {
            sql.append(" department_id = ?");
            id = emplDepart.getDepartmentId();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                throw new NotFoundSQLException();
            }
            List<EmplDepart> emplDeparts = new ArrayList<>();
            while (resultSet.next()) {
                EmplDepart ed = new EmplDepart();
                ed.setEmployeeId(resultSet.getLong(1));
                ed.setDepartmentId(resultSet.getLong(2));
                emplDeparts.add(ed);
            }
            return emplDeparts;
        }
    }

    @Override
    public void update(EmplDepart emplDepart) throws SQLException {
        String sql = "UPDATE employee_department SET employee_id = ?, department_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, emplDepart.getEmployeeId());
            preparedStatement.setLong(2, emplDepart.getDepartmentId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(EmplDepart emplDepart) throws SQLException {
        String sql = "DELETE FROM employee_department WHERE employee_id = ? AND department_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, emplDepart.getEmployeeId());
            preparedStatement.setLong(2, emplDepart.getDepartmentId());
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new NotFoundSQLException();
            }
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM employee_department";
            statement.execute(sql);
        }
    }
}
