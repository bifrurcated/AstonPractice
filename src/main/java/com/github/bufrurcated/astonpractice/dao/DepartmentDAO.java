package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.Department;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO extends AbstractDao implements Dao<Department, Long> {

    public DepartmentDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void save(Department department) throws SQLException {
        String sql = "INSERT INTO department (dpt_name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, department.getName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Department> findAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT id, dpt_name FROM department";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.getFetchSize() < 1) {
                throw new NotFoundSQLException();
            }
            List<Department> departmentList = new ArrayList<>();
            while (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getLong(1));
                department.setName(resultSet.getString(2));
                departmentList.add(department);
            }
            return departmentList;
        }
    }

    @Override
    public List<Department> find(Long id) throws SQLException {
        String sql = "SELECT id, dpt_name FROM department where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new NotFoundSQLException();
            }
            Department employee = new Department();
            employee.setId(resultSet.getLong(1));
            employee.setName(resultSet.getString(2));
            return List.of(employee);
        }
    }

    @Override
    public void update(Department department) throws SQLException {
        String sql = "UPDATE department SET dpt_name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, department.getName());
            preparedStatement.setLong(2, department.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try {
            connection.setAutoCommit(false);
            String sql1 = "DELETE FROM employee_department WHERE department_id = ?";
            String sql2 = "DELETE FROM department WHERE id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement1.setLong(1, id);
            preparedStatement2.setLong(1, id);
            preparedStatement1.execute();
            int rowsDeleted = preparedStatement2.executeUpdate();
            if (rowsDeleted == 0) {
                connection.rollback();
                preparedStatement1.close();
                preparedStatement2.close();
                throw new NotFoundSQLException();
            }
            connection.commit();
            preparedStatement1.close();
            preparedStatement2.close();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException(e);
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql1 = "DELETE FROM employee_department WHERE department_id IN (SELECT id FROM department)";
            String sql2 = "DELETE FROM department";
            statement.execute(sql1);
            statement.execute(sql2);
        }
    }
}
