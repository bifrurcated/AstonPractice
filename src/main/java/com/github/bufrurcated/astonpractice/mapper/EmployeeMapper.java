package com.github.bufrurcated.astonpractice.mapper;

import com.github.bufrurcated.astonpractice.dto.RequestCreateEmployee;
import com.github.bufrurcated.astonpractice.dto.RequestUpdateEmployee;
import com.github.bufrurcated.astonpractice.dto.ResponseEmployee;
import com.github.bufrurcated.astonpractice.entity.Employee;

public class EmployeeMapper {
    public Employee map(RequestCreateEmployee requestCreateEmployee) {
        var employee = new Employee();
        employee.setFirstName(requestCreateEmployee.firstName());
        employee.setLastName(requestCreateEmployee.lastName());
        employee.setAge(requestCreateEmployee.age());
        return employee;
    }

    public Employee map(RequestUpdateEmployee requestUpdateEmployee) {
        var employee = new Employee();
        employee.setId(requestUpdateEmployee.id());
        employee.setFirstName(requestUpdateEmployee.firstName());
        employee.setLastName(requestUpdateEmployee.lastName());
        employee.setAge(requestUpdateEmployee.age());
        return employee;
    }

    public ResponseEmployee map(Employee employee) {
        return new ResponseEmployee(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAge()
        );
    }
}
