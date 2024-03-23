package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.EmployeeMapper;
import com.github.bufrurcated.astonpractice.service.EmployeeService;
import com.github.bufrurcated.astonpractice.utils.Parse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final EmployeeMapper employeeMapper;

    @GetMapping
    public ResponseEntity<String> get() {
        var responseEmployees = service.getAll().stream()
                .map(employeeMapper::map)
                .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new JSONArray(responseEmployees).toString());
    }

    @GetMapping( "/{id}")
    public ResponseEntity<String> get(@PathVariable("id") Long id) {
        var employee = service.getById(id);
        var responseEmployee = employeeMapper.map(employee);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new JSONObject(responseEmployee).toString());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {
        var req = Parse.jsonToCreateEmployee(body);
        var emp = employeeMapper.map(req);
        service.add(emp);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Employee created");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody String body) {
        var requestUpdateEmployee = Parse.jsonToUpdateEmployee(body);
        var emp = employeeMapper.map(requestUpdateEmployee);
        service.update(emp);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Employee updated");
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        service.removeAll();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("All employees deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        service.removeById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Employee deleted");
    }
}
