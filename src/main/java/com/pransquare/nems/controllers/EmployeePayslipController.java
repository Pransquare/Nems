package com.pransquare.nems.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pransquare.nems.entities.EmployeePayslip;
import com.pransquare.nems.models.PayslipResModal;
import com.pransquare.nems.services.EmployeePayslipService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Pransquare/nems/employee-payslip")
public class EmployeePayslipController {

    private final EmployeePayslipService service;

    public EmployeePayslipController(EmployeePayslipService service) {
        this.service = service;
    }

    // Create Payslip
    @PostMapping("/create-update")
    public ResponseEntity<List<EmployeePayslip>> createPayslip(@RequestBody List<EmployeePayslip> payslip) {
        return ResponseEntity.ok(service.createPayslip(payslip));
    }

    // Fetch Payslip by Employee ID
    @GetMapping("/by-employee/{employeeId}")
    public ResponseEntity<Map<String, PayslipResModal>> getPayslipsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getPayslipsByEmployeeId(employeeId));
    }

}
