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

import com.pransquare.nems.entities.EmployeeTdsDetails;
import com.pransquare.nems.services.EmployeeTdsDetailsService;

@RestController
@RequestMapping("/api/employee-tds-details")
public class EmployeeTdsDetailsController {

    private EmployeeTdsDetailsService employeeTdsDetailsService;

    
    public EmployeeTdsDetailsController(EmployeeTdsDetailsService employeeTdsDetailsService) {
		this.employeeTdsDetailsService = employeeTdsDetailsService;
	}

	@GetMapping
    public ResponseEntity<List<EmployeeTdsDetails>> getAllEmployeeTdsDetails() {
        return ResponseEntity.ok(employeeTdsDetailsService.getAllEmployeeTdsDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeTdsDetails> getEmployeeTdsDetailsById(@PathVariable int id) {
        Optional<EmployeeTdsDetails> employeeTdsDetails = employeeTdsDetailsService.getEmployeeTdsDetailsById(id);
        return employeeTdsDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmployeeTdsDetails> createEmployeeTdsDetails(
            @RequestBody EmployeeTdsDetails employeeTdsDetails) {
        return ResponseEntity.ok(employeeTdsDetailsService.createEmployeeTdsDetails(employeeTdsDetails));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeTdsDetails> updateEmployeeTdsDetails(@PathVariable int id,
            @RequestBody EmployeeTdsDetails employeeTdsDetails) {
        return ResponseEntity.ok(employeeTdsDetailsService.updateEmployeeTdsDetails(id, employeeTdsDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeTdsDetails(@PathVariable int id) {
        employeeTdsDetailsService.deleteEmployeeTdsDetails(id);
        return ResponseEntity.noContent().build();
    }
}
