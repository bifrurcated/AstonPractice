package com.github.bufrurcated.astonpractice.utils;

import com.github.bufrurcated.astonpractice.db.DatabaseSource;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class InitData {
    @SneakyThrows
    public static void initialization(Connection connection) {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO employees (first_name, last_name, age) VALUES ('Nick', 'Jordan', 19);");
        statement.execute("INSERT INTO employees (first_name, last_name, age) VALUES ('Jonh', 'Mike', 35);");
        statement.execute("INSERT INTO employees (first_name, last_name, age) VALUES ('Artyom', 'Vedoseev', 25);");
        statement.execute("INSERT INTO departments (dpt_name) VALUES ('Back-end');");
        statement.execute("INSERT INTO departments (dpt_name) VALUES ('Front-end');");
        statement.execute("INSERT INTO employee_department (employee_id, department_id) VALUES (1, 1);");
        statement.execute("INSERT INTO employee_department (employee_id, department_id) VALUES (2, 1);");
        statement.execute("INSERT INTO employee_department (employee_id, department_id) VALUES (2, 2);");
        statement.execute("INSERT INTO employee_department (employee_id, department_id) VALUES (3, 1);");
        statement.execute("INSERT INTO phone_numbers (phone_number, employee_id) VALUES ('+78005553535', 1);");
        statement.execute("INSERT INTO phone_numbers (phone_number, employee_id) VALUES ('89009009999', 1);");
        statement.execute("INSERT INTO phone_numbers (phone_number, employee_id) VALUES ('+129149009999', 2);");
        statement.close();
    }

    @SneakyThrows
    public static void clear(Connection connection) {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS employees CASCADE;");
        statement.execute("DROP TABLE IF EXISTS departments CASCADE;");
        statement.execute("DROP TABLE IF EXISTS employee_department CASCADE;");
        statement.execute("DROP TABLE IF EXISTS phone_numbers CASCADE;");
        statement.close();
    }
}
