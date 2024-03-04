package com.github.bufrurcated.astonpractice.dao;

import com.github.bufrurcated.astonpractice.dto.FindNumber;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;
import com.github.bufrurcated.astonpractice.errors.NotFoundSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneNumberDAO extends AbstractDao implements Dao<PhoneNumber, FindNumber> {
    public PhoneNumberDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void save(PhoneNumber phoneNumber) throws SQLException {
        String sql = "INSERT INTO phone_numbers (phone_number, employee_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phoneNumber.getPhoneNumber());
            preparedStatement.setLong(2, phoneNumber.getEmployeeId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<PhoneNumber> findAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT id, phone_number, employee_id FROM phone_numbers";
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.isBeforeFirst()) {
                throw new NotFoundSQLException();
            }
            List<PhoneNumber> phoneNumbers = new ArrayList<>();
            while (resultSet.next()) {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setId(resultSet.getLong(1));
                phoneNumber.setPhoneNumber(resultSet.getString(2));
                phoneNumber.setEmployeeId(resultSet.getLong(3));
                phoneNumbers.add(phoneNumber);
            }
            return phoneNumbers;
        }
    }

    @Override
    public List<PhoneNumber> find(FindNumber find) throws SQLException {
        Result result = getResult(find, "SELECT id, phone_number, employee_id FROM phone_numbers WHERE");
        try (PreparedStatement preparedStatement = connection.prepareStatement(result.sql().toString())) {
            preparedStatement.setLong(1, result.id());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                throw new NotFoundSQLException();
            }
            List<PhoneNumber> emplDeparts = new ArrayList<>();
            while (resultSet.next()) {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setId(resultSet.getLong(1));
                phoneNumber.setPhoneNumber(resultSet.getString(2));
                phoneNumber.setEmployeeId(resultSet.getLong(3));
                emplDeparts.add(phoneNumber);
            }
            return emplDeparts;
        }
    }

    private static Result getResult(FindNumber find, String query) {
        StringBuilder sql = new StringBuilder(query);
        Long id;
        if (find.id() != null) {
            sql.append(" id = ?");
            id = find.id();
        } else {
            sql.append(" employee_id = ?");
            id = find.employeeId();
        }
        return new Result(sql, id);
    }

    private record Result(StringBuilder sql, Long id) {
    }

    @Override
    public void update(PhoneNumber phoneNumber) throws SQLException {
        String sql = "UPDATE phone_numbers SET phone_number = ?, employee_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phoneNumber.getPhoneNumber());
            preparedStatement.setLong(2, phoneNumber.getEmployeeId());
            preparedStatement.setLong(3, phoneNumber.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(FindNumber find) throws SQLException {
        String query = "DELETE FROM phone_numbers WHERE";
        Result result = getResult(find, query);
        try (PreparedStatement preparedStatement = connection.prepareStatement(result.sql().toString())) {
            preparedStatement.setLong(1, result.id());
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new NotFoundSQLException();
            }
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM phone_numbers";
            statement.execute(sql);
        }
    }
}
