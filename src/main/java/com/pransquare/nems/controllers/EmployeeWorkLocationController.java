package com.pransquare.nems.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.EmployeeWorkLocationEntity;
import com.pransquare.nems.services.EmployeeWorkLocationService;

@RestController
@RequestMapping("/api/employee-work-locations")
public class EmployeeWorkLocationController {

	private final EmployeeWorkLocationService employeeWorkLocationService;

	public EmployeeWorkLocationController(EmployeeWorkLocationService employeeWorkLocationService) {
		this.employeeWorkLocationService = employeeWorkLocationService;
	}

	// Get all work locations
	@GetMapping
	public ResponseEntity<List<EmployeeWorkLocationEntity>> getAllWorkLocations() {
		List<EmployeeWorkLocationEntity> workLocations = employeeWorkLocationService.findAll();
		if (workLocations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(workLocations);
	}

	// Get work location by ID
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeWorkLocationEntity> getWorkLocationById(@PathVariable Long id) {
		Optional<EmployeeWorkLocationEntity> workLocation = employeeWorkLocationService.findById(id);
		return workLocation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Get work locations by employee ID
	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<EmployeeWorkLocationEntity> getWorkLocationsByEmployeeId(@PathVariable Long employeeId) {
		EmployeeWorkLocationEntity workLocations = employeeWorkLocationService.findByEmployeeId(employeeId);
		if (workLocations == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(workLocations);
	}

	// Get work locations by work location code
	@GetMapping("/location/{workLocationCode}")
	public ResponseEntity<List<EmployeeWorkLocationEntity>> getWorkLocationsByLocationCode(
			@PathVariable String workLocationCode) {
		List<EmployeeWorkLocationEntity> workLocations = employeeWorkLocationService
				.findByWorkLocationCode(workLocationCode);
		if (workLocations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(workLocations);
	}

	// Create or update a work location
	@PostMapping
	public ResponseEntity<EmployeeWorkLocationEntity> createOrUpdateWorkLocation(
			@RequestBody EmployeeWorkLocationEntity workLocation) {
		EmployeeWorkLocationEntity savedWorkLocation = employeeWorkLocationService.saveWorkLocation(workLocation);
		return ResponseEntity.ok(savedWorkLocation);
	}

	// Delete a work location
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWorkLocation(@PathVariable Long id) {
		employeeWorkLocationService.deleteWorkLocation(id);
		return ResponseEntity.noContent().build();
	}
}
