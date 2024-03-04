package com.github.bufrurcated.astonpractice.mapper;

import com.github.bufrurcated.astonpractice.dto.RequestCreatePhoneNumber;
import com.github.bufrurcated.astonpractice.dto.RequestUpdatePhoneNumber;
import com.github.bufrurcated.astonpractice.dto.ResponsePhoneNumber;
import com.github.bufrurcated.astonpractice.entity.PhoneNumber;

public class PhoneNumberMapper {
    public ResponsePhoneNumber map(PhoneNumber phoneNumber) {
        return new ResponsePhoneNumber(phoneNumber.getEmployeeId(), phoneNumber.getPhoneNumber());
    }

    public PhoneNumber map(RequestCreatePhoneNumber requestCreatePhoneNumber) {
        var phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(requestCreatePhoneNumber.phoneNumber());
        phoneNumber.setEmployeeId(requestCreatePhoneNumber.employeeId());
        return phoneNumber;
    }

    public PhoneNumber map(RequestUpdatePhoneNumber requestUpdatePhoneNumber) {
        var phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(requestUpdatePhoneNumber.phoneNumber());
        phoneNumber.setEmployeeId(requestUpdatePhoneNumber.employeeId());
        return phoneNumber;
    }
}
