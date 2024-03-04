package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO extends AbstractDao implements Dao<Employee, Long> {

    public EmployeeDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee (first_name, last_name, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT id, first_name, last_name, age FROM employee";
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.isBeforeFirst()) {
                throw new NotFoundSQLException();
            }
            List<Employee> employeeList = new ArrayList<>();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setFirstName(resultSet.getString(2));
                employee.setLastName(resultSet.getString(3));
                employee.setAge(resultSet.getInt(4));
                employeeList.add(employee);
            }
            return employeeList;
        }
    }

    @Override
    public List<Employee> find(Long id) throws SQLException {
        String sql = "SELECT id, first_name, last_name, age FROM employee where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new NotFoundSQLException();
            }
            Employee employee = new Employee();
            employee.setId(resultSet.getLong(1));
            employee.setFirstName(resultSet.getString(2));
            employee.setLastName(resultSet.getString(3));
            employee.setAge(resultSet.getInt(4));
            return List.of(employee);
        }
    }

    @Override
    public void update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET first_name = ?, last_name = ?, age = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setLong(4, employee.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql1 = "DELETE FROM employee_department WHERE employee_id = ?";
        String sql2 = "DELETE FROM phone_numbers WHERE employee_id = ?";
        String sql3 = "DELETE FROM employee WHERE id = ?";
        try (PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
             PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
             PreparedStatement preparedStatement3 = connection.prepareStatement(sql3)) {
            connection.setAutoCommit(false);
            preparedStatement1.setLong(1, id);
            preparedStatement2.setLong(1, id);
            preparedStatement3.setLong(1, id);
            preparedStatement1.execute();
            preparedStatement2.execute();
            int rowsDeleted = preparedStatement3.executeUpdate();
            if (rowsDeleted == 0) {
                connection.rollback();
                throw new NotFoundSQLException();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException(e);
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql1 = "DELETE FROM employee_department";
            String sql2 = "DELETE FROM phone_numbers";
            String sql3 = "DELETE FROM employee";
            statement.execute(sql1);
            statement.execute(sql2);
            statement.execute(sql3);
        }
    }
}
