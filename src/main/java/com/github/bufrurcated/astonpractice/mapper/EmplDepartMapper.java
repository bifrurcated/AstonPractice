package com.github.bufrurcated.astonpractice.mapper;

import com.github.bufrurcated.astonpractice.dto.RequestEmplDepart;
import com.github.bufrurcated.astonpractice.dto.ResponseEmplDepart;
import com.github.bufrurcated.astonpractice.entity.EmplDepart;
import org.springframework.stereotype.Component;

@Component
public class EmplDepartMapper {
    public EmplDepart map(RequestEmplDepart requestEmplDepart) {
        var emplDepart = new EmplDepart();
        emplDepart.setEmployeeId(requestEmplDepart.employeeId());
        emplDepart.setDepartmentId(requestEmplDepart.departmentId());
        return emplDepart;
    }

    public ResponseEmplDepart map(EmplDepart emplDepart) {
        return new ResponseEmplDepart(emplDepart.getEmployeeId(), emplDepart.getDepartmentId());
    }
}
