package com.pransquare.nems.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.EmployeeTdsEntity;
import com.pransquare.nems.services.EmployeeTdsService;

@RestController
@RequestMapping("/api/employee-tds")
public class EmployeeTdsController {

    private EmployeeTdsService employeeTdsService;

    public EmployeeTdsController(EmployeeTdsService employeeTdsService) {
		this.employeeTdsService = employeeTdsService;
	}

	@GetMapping
    public ResponseEntity<List<EmployeeTdsEntity>> getAllEmployeeTds() {
        return ResponseEntity.ok(employeeTdsService.getAllEmployeeTds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeTdsEntity> getEmployeeTdsById(@PathVariable int id) {
        Optional<EmployeeTdsEntity> employeeTds = employeeTdsService.getEmployeeTdsById(id);
        return employeeTds.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmployeeTdsEntity> createEmployeeTds(@RequestBody EmployeeTdsEntity employeeTds) {
        return ResponseEntity.ok(employeeTdsService.createEmployeeTds(employeeTds));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeTdsEntity> updateEmployeeTds(@PathVariable int id,
            @RequestBody EmployeeTdsEntity employeeTds) {
        return ResponseEntity.ok(employeeTdsService.updateEmployeeTds(id, employeeTds));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeTds(@PathVariable int id) {
        employeeTdsService.deleteEmployeeTds(id);
        return ResponseEntity.noContent().build();
    }
}
