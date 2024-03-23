package com.github.bufrurcated.astonpractice.api;

import com.github.bufrurcated.astonpractice.mapper.PhoneNumberMapper;
import com.github.bufrurcated.astonpractice.service.PhoneNumberService;
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
@RequestMapping("/api/v1/phone-numbers")
public class PhoneNumberController {

    private final PhoneNumberService service;
    private final PhoneNumberMapper phoneNumberMapper;

    @GetMapping
    public ResponseEntity<String> get(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "employeeId", required = false) Long employeeId) {
        if (id != null) {
            var phoneNumber = service.getById(id);
            var responsePhoneNumber = phoneNumberMapper.map(phoneNumber);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new JSONObject(responsePhoneNumber).toString());
        } else if (employeeId != null) {
            var responsePhoneNumbers = service.getByEmployeeId(employeeId).stream().map(phoneNumberMapper::map).toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new JSONArray(responsePhoneNumbers).toString());
        }
        var responseEmployees = service.getAll().stream()
                .map(phoneNumberMapper::map)
                .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new JSONArray(responseEmployees).toString());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {
        var req = Parse.jsonToCreatePhoneNumber(body);
        var phoneNumber = phoneNumberMapper.map(req);
        service.add(phoneNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Phone number created");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody String body) {
        var req = Parse.jsonToUpdatePhoneNumber(body);
        var phoneNumber = phoneNumberMapper.map(req);
        service.update(phoneNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Phone number updated");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "employeeId", required = false) Long employeeId) {
        if (id != null) {
            service.removeById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Phone number deleted");
        } else if (employeeId != null) {
            service.removeByEmployeeId(employeeId);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Phone numbers by employee deleted");
        }
        service.removeAll();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Phone numbers deleted");
    }
}
