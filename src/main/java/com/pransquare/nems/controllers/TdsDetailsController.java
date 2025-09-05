package com.pransquare.nems.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.EmployeeTdsDetailsView;
import com.pransquare.nems.entities.TdsDetails;
import com.pransquare.nems.services.TdsDetailsService;

@RestController
@RequestMapping("/Pransquare/nems/tdsdetails")
public class TdsDetailsController {

	private TdsDetailsService tdsDetailsService;

	public TdsDetailsController(TdsDetailsService tdsDetailsService) {
		this.tdsDetailsService = tdsDetailsService;
	}

	@PostMapping("/saveTdsDetails")
	public ResponseEntity<List<TdsDetails>> createTdsDetails(@RequestBody List<TdsDetails> tdsDetailsList) {
		List<TdsDetails> createdTdsDetailsList = tdsDetailsService.saveTdsDetails(tdsDetailsList);
		return ResponseEntity.ok(createdTdsDetailsList);
	}

	@PutMapping("/updateTdsDetails")
	public ResponseEntity<List<TdsDetails>> updateTdsDetails(@RequestBody List<TdsDetails> tdsDetailsList) {
		List<TdsDetails> updatedTdsDetailsList = tdsDetailsService.updateTdsDetails(tdsDetailsList);
		return ResponseEntity.ok(updatedTdsDetailsList);
	}

	@GetMapping("/getTdsDetailsByEmployeeCode")
	public ResponseEntity<List<TdsDetails>> getTdsDetailsByEmployeeCode(@RequestParam String employeeCode) {
		List<TdsDetails> tdsDetailsList = tdsDetailsService.getTdsDetailsByEmployeeCode(employeeCode);
		if (tdsDetailsList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(tdsDetailsList);
	}

	@GetMapping("/getAllEmployeeTdsDetails")
	public ResponseEntity<Page<TdsDetails>> getAllEmployeeTdsDetails(@RequestParam int page, @RequestParam int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<TdsDetails> tdsDetailsPage = tdsDetailsService.getAllEmployeeTdsDetails(pageable);

		return ResponseEntity.ok(tdsDetailsPage);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<EmployeeTdsDetailsView>> searchEmployeeTdsDetails(
			@RequestParam(required = false) String employeeCode,
			@RequestParam(required = false) String financialYearCode,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<EmployeeTdsDetailsView> results = tdsDetailsService.search(employeeCode, financialYearCode, pageable);

		return ResponseEntity.ok(results);
	}

	@PutMapping("/enableProofDeclarationForAllActiveEmployees")
	public ResponseEntity<String> enableProofDeclarationForAllActiveEmployees() {
		return tdsDetailsService.enableProofDeclarationForAllActiveEmployees();
	}

	@PutMapping("/enableProofDeclarationForEmployee")
	public ResponseEntity<String> enableProofDeclarationForEmployee(@RequestParam String employeeCode) {
		return tdsDetailsService.enableProofDeclarationForEmployee(employeeCode);
	}

	@PutMapping("/updateTdsStatus")
	public ResponseEntity<String> updateTdsStatus(@RequestParam(required = true) String employeeCode,
			@RequestParam(required = true) String status) {
		return tdsDetailsService.updateTdsStatus(employeeCode, status);
	}
	
	@PutMapping("/enableTaxDeclarationForAllActiveEmployees")
	public ResponseEntity<String> enableTaxDeclarationForAllActiveEmployees() {
		return tdsDetailsService.enableTaxDeclarationForAllActiveEmployees();
	}

	@PutMapping("/enableTaxDeclarationForEmployee")
	public ResponseEntity<String> enableTaxDeclarationForEmployee(@RequestParam String employeeCode) {
		return tdsDetailsService.enableTaxDeclarationForEmployee(employeeCode);
	}
}