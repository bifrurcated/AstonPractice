package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.DepartmentMapper;
import com.github.bufrurcated.astonpractice.service.DepartmentService;
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
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService service;
    private final DepartmentMapper departmentMapper;

    @GetMapping
    public ResponseEntity<String> get() {
        var responseDepartments = service.getAll().stream()
                .map(departmentMapper::map)
                .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new JSONArray(responseDepartments).toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> get(@PathVariable("id") Long id) {
        var department = service.getById(id);
        var responseDepartment = departmentMapper.map(department);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new JSONObject(responseDepartment).toString());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {
        var req = Parse.jsonToCreateDepartment(body);
        var department = departmentMapper.map(req);
        service.add(department);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Department created");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody String body) {
        var requestUpdateEmployee = Parse.jsonToUpdateDepartment(body);
        var emp = departmentMapper.map(requestUpdateEmployee);
        service.update(emp);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Department updated");
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        service.removeAll();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("All departments deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        service.removeById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Department deleted");
    }
}
