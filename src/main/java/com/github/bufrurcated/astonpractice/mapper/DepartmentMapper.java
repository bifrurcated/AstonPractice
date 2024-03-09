package com.github.bufrurcated.astonpractice.mapper;

import com.github.bufrurcated.astonpractice.dto.RequestCreateDepartment;
import com.github.bufrurcated.astonpractice.dto.RequestUpdateDepartment;
import com.github.bufrurcated.astonpractice.dto.ResponseDepartment;
import com.github.bufrurcated.astonpractice.entity.Department;

public class DepartmentMapper {


    public ResponseDepartment map(Department department) {
        return new ResponseDepartment(department.getId(), department.getName());
    }

    public Department map(RequestCreateDepartment requestCreateDepartment) {
        var department = new Department();
        department.setName(requestCreateDepartment.name());
        return department;
    }

    public Department map(RequestUpdateDepartment requestUpdateDepartment) {
        var department = new Department();
        department.setId(requestUpdateDepartment.id());
        department.setName(requestUpdateDepartment.name());
        return department;
    }
}
