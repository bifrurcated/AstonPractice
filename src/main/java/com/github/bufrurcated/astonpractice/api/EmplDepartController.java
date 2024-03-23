package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.EmplDepartMapper;
import com.github.bufrurcated.astonpractice.service.EmplDepartService;
import com.github.bufrurcated.astonpractice.utils.Parse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/employee-department")
public class EmplDepartController {

    private final EmplDepartService service;
    private final EmplDepartMapper emplDepartMapper;

    @GetMapping
    public ResponseEntity<String> get(@RequestBody(required = false) String body) {
        if (body != null) {
            var requestEmplDepart = Parse.jsonToEmplDepart(body);
            var emplDepart = emplDepartMapper.map(requestEmplDepart);
            var responseEmplDeparts = service.get(emplDepart).stream().map(emplDepartMapper::map).toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new JSONArray(responseEmplDeparts).toString());
        }
        var responseEmplDeparts = service.getAll().stream().map(emplDepartMapper::map).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new JSONArray(responseEmplDeparts).toString());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {
        var requestEmplDepart = Parse.jsonToEmplDepart(body);
        var emplDepart = emplDepartMapper.map(requestEmplDepart);
        service.add(emplDepart);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Employee Department created");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody String body) {
        if (body != null) {
            var requestEmplDepart = Parse.jsonToEmplDepart(body);
            var emplDepart = emplDepartMapper.map(requestEmplDepart);
            service.remove(emplDepart);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Employee Department deleted");
        }
        service.removeAll();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("All Employee Department deleted");
    }
}
