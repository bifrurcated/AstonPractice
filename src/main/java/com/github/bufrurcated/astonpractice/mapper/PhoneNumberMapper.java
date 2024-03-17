package com.github.bufrurcated.astonpractice.mapper;

import com.github.bufrurcated.astonpractice.dto.RequestCreatePhoneNumber;
import com.github.bufrurcated.astonpractice.dto.RequestUpdatePhoneNumber;
import com.github.bufrurcated.astonpractice.dto.ResponsePhoneNumber;
import com.github.bufrurcated.astonpractice.entity.Employee;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;

public class PhoneNumberMapper {
    public ResponsePhoneNumber map(PhoneNumber phoneNumber) {
        return new ResponsePhoneNumber(phoneNumber.getId(), phoneNumber.getEmployee().getId(), phoneNumber.getPhoneNumber());
    }

    public PhoneNumber map(RequestCreatePhoneNumber requestCreatePhoneNumber) {
        var phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(requestCreatePhoneNumber.phoneNumber());
        phoneNumber.setEmployee(Employee.builder().id(requestCreatePhoneNumber.employeeId()).build());
        return phoneNumber;
    }

    public PhoneNumber map(RequestUpdatePhoneNumber requestUpdatePhoneNumber) {
        var phoneNumber = new PhoneNumber();
        phoneNumber.setId(requestUpdatePhoneNumber.id());
        phoneNumber.setEmployee(Employee.builder().id(requestUpdatePhoneNumber.employeeId()).build());
        phoneNumber.setPhoneNumber(requestUpdatePhoneNumber.phoneNumber());
        return phoneNumber;
    }
}
