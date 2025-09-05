package com.pransquare.nems.controllers;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.models.EmployeeCountResponse;
import com.pransquare.nems.models.EmployeeModel;
import com.pransquare.nems.models.EmployeeSearchModel;
import com.pransquare.nems.models.EmployeeUpdateEmailAndRolesModel;
import com.pransquare.nems.repositories.EmployeeRepository.EmployeeBirthdayProjection;
import com.pransquare.nems.repositories.EmployeeRepository.EmployeeForGoal;
import com.pransquare.nems.repositories.EmployeeRepository.EmployeeNameCode;
import com.pransquare.nems.services.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/Pransquare/nems/employee")
public class EmployeeController {

    private static final Logger logger = LogManager.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/getAllEmployees")
    public Page<EmployeeEntity> getAllEmployees(@RequestBody EmployeeSearchModel employeeSearchModel) {
        return employeeService.getAllEmployees(employeeSearchModel);
    }

    @PostMapping("/getAllEmployeesByStatus")
    public Page<EmployeeEntity> getAllEmployeesByStatus(@RequestBody EmployeeSearchModel employeeSearchModel) {
        return employeeService.getAllEmployeesByStatus(employeeSearchModel);
    }

    @GetMapping("/getEmployeeByEmployeeCode/{employeeCode}")
    public EmployeeEntity getEmployeeByEmployeeCode(@PathVariable String employeeCode) {
        return employeeService.dedupeCheckWithEmployeeCode(employeeCode);
    }

    @GetMapping("/dedupeCheckWithEmployeeCode/{employeeCode}")
    public EmployeeEntity dedupeCheckWithEmployeeCode(@PathVariable String employeeCode) {
        return employeeService.dedupeCheckWithEmployeeCode(employeeCode);
    }

    @PostMapping("/createOrUpdateEmployee")
    public EmployeeEntity createEmployee(@RequestBody EmployeeModel employeeModel) {
        return employeeService.createOrUpdateEmployee(employeeModel);
    }

    @PutMapping("/updateEmployee")
    public EmployeeEntity updateEmployee(@RequestBody EmployeeModel employeeModel) {
        return employeeService.createOrUpdateEmployee(employeeModel);
    }

    @DeleteMapping("/deleteEmployee/{employeeId}")
    public EmployeeEntity deleteEmployee(@PathVariable Long employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }

    @PostMapping("/getAllEmployeesForMailCreation")
    public Page<EmployeeEntity> getAllEmployeesForMailCreation(@RequestBody EmployeeSearchModel employeeSearchModel) {
        return employeeService.getAllEmployeesForMailCreation(employeeSearchModel);
    }

    @PostMapping("/updateEmployeeRolesAndEmail")
    public ResponseEntity<String> updateEmployeeRolesAndEmail(@RequestBody EmployeeUpdateEmailAndRolesModel model) {

        return employeeService.updateEmployeeRolesAndEmail(model);
    }

    @GetMapping({ "/employeesByStatus" })
    public List<EmployeeEntity> getEmployeesByStatus(@RequestParam(required = false) Optional<String> status) {
        logger.info("EmployeeController.getEmployeesByStatus(){}", status);

        return employeeService.getEmployeesByStatus(status.orElse("Active"));
    }

    @GetMapping({ "/employeeCodeSequence" })
    public String employeeCodeSequence() {
        return employeeService.sequence();
    }

    @GetMapping("/employees/search")
    public List<EmployeeNameCode> searchEmployeesByName(@RequestParam String input) {
        return employeeService.getEmployeesByFirstNameStartingWithOrLastNameStartingWith(input);
    }

    @PostMapping("/offboard")
    public ResponseEntity<String> offboardEmployee(@RequestBody EmployeeModel employeeModel,
            HttpServletRequest request) {
        try {
            // Call the service method to offboard the employee
            String response = employeeService.offboardMember(employeeModel.getEmployeeId(),
                    employeeModel.getLastWorkingDay(), request);

            // Return success response
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any exceptions
            return ResponseEntity.status(500).body("Error offboarding employee: " + e.getMessage());
        }
    }

    @GetMapping("/getEmployeeCtc")
    public ResponseEntity<Double> getMethodName(@RequestParam Long employeeId) {
        return ResponseEntity.ok().body(employeeService.getEmployeeCtc(employeeId));
    }

    @GetMapping("/getBirthdayList")
    public ResponseEntity<List<EmployeeBirthdayProjection>> getBirthdayList() {
        return ResponseEntity.ok().body(employeeService.getBirthdayList());
    }

    @GetMapping("/getReportingIds")
    public ResponseEntity<List<Long>> getReportingIds(@RequestParam Long managerId) {
        return ResponseEntity.ok().body(employeeService.getReportingIds(managerId));
    }

@GetMapping("/employee-counts")
    public ResponseEntity<Object> getEmployeeCounts() {
        long activeCount = employeeService.getActiveEmployeeCount();
        long inactiveCount = employeeService.getInactiveEmployeeCount();
 
        // Return a response with status code 200 OK and the counts in the body
        return ResponseEntity.ok().body(
                new EmployeeCountResponse(activeCount, inactiveCount)
        );
    }
@GetMapping("/searchEmployeeForGoal")
public List<EmployeeForGoal> searchEmployeeForGoal(@RequestParam String input) {
    return employeeService.searchEmployeeForGoal(input);
}
}
