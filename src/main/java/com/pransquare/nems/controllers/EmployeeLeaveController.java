package com.pransquare.nems.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.EmployeeLeaveDetailsConfigEntity;
import com.pransquare.nems.entities.EmployeeLeaveEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.EmployeeLeaveDetailsConfigModel;
import com.pransquare.nems.models.EmployeeLeaveModel;
import com.pransquare.nems.models.LeaveTypeEntity;
import com.pransquare.nems.services.EmployeeLeaveService;

@RestController
@RequestMapping(value = "/Pransquare/nems/employeeLeave")
public class EmployeeLeaveController {

	private static final Logger logger = LogManager.getLogger(EmployeeLeaveController.class);
	
	private final EmployeeLeaveService employeeLeaveService;

	public EmployeeLeaveController(EmployeeLeaveService employeeLeaveService) {
		this.employeeLeaveService = employeeLeaveService;
	}

	@GetMapping("/getEmployeeLeaveDetails/{employeeId}")
	public ResponseEntity<List<EmployeeLeaveEntity>> getEmployeeLeaveDetails(@PathVariable Integer employeeId) {
		try {
			List<EmployeeLeaveEntity> employeeLeaveDetails = employeeLeaveService.findEmployeeLeaveDetails(employeeId);
			return ResponseEntity.ok(employeeLeaveDetails);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/createOrUpdateEmployeeLeave")
	public ResponseEntity<Object> createOrUpdateEmployeeLeaveEntity(
			@RequestBody EmployeeLeaveModel employeeLeaveModel) {
		EmployeeLeaveEntity employeeLeaveDetails = employeeLeaveService
				.createOrUpdateEmployeeLeaveEntity(employeeLeaveModel);
		return ResponseEntity.ok(employeeLeaveDetails);
	}

	@PostMapping("/deleteEmployeeLeave/{employeeLeaveId}")
	public ResponseEntity<EmployeeLeaveEntity> deleteEmployeeLeave(@PathVariable Long employeeLeaveId) {
		try {
			return ResponseEntity.ok(employeeLeaveService.deleteEmployeeLeaveEntity(employeeLeaveId));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/findEmployeeLeaveDetails")
	public List<EmployeeLeaveEntity> findEmployeeLeaveDetails(@RequestBody EmployeeLeaveModel employeeLeaveModel) {
		return employeeLeaveService.findEmployeeLeaveDetails(employeeLeaveModel);
	}

	@GetMapping("/getEmployeeLeavesByApproverId/{approverId}")
	public List<EmployeeLeaveEntity> getEmployeeLeavesByApproverId(@PathVariable Long approverId) {
		return employeeLeaveService.findEmployeeLeaveDetailsByApproverId(approverId);
	}

	@PostMapping("/changeEmployeeLeaveStatus/{employeeLeaveId}/status/{status}")
	public ResponseEntity<EmployeeLeaveEntity> changeEmployeeLeaveStatus(@PathVariable long employeeLeaveId,
			@PathVariable String status, @RequestBody EmployeeLeaveModel employeeLeaveModel) {
		try {
			EmployeeLeaveEntity updatedLeave = employeeLeaveService.changeEmployeeLeaveStatus(employeeLeaveId, status,
					employeeLeaveModel);
			return new ResponseEntity<>(updatedLeave, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/employeeLeaveConfig")
	public ResponseEntity<List<EmployeeLeaveDetailsConfigEntity>> getEmployeeLeaveConfigDetails(
			@RequestBody EmployeeLeaveDetailsConfigModel detailsConfigModel) {
		try {
			List<EmployeeLeaveDetailsConfigEntity> leaveConfigDetails = employeeLeaveService
					.getEmployeeLeaveConfigDetails(detailsConfigModel);
			return new ResponseEntity<>(leaveConfigDetails, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping({ "/getEmployeesByLeaveDate/{leave}" })
	public List<EmployeeLeaveEntity> getEmployeesByLeave(
			@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate leave) {
		logger.info("EmployeeLeaveController.getEmployeesByLeave()");
		return employeeLeaveService.getEmployeesByLeave(leave);
	}

	@GetMapping({ "/Test" })
	public List<LeaveTypeEntity> test() {
		logger.info("test started");
		return employeeLeaveService.performTask2();
	}
	
	@PostMapping("/generateLeaveReport")
	public ResponseEntity<byte[]> generateReport(@RequestBody EmployeeLeaveModel leaveModel) {
	    try {
	        byte[] report = employeeLeaveService.generateLeaveReport(leaveModel);
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDispositionFormData("attachment", "leave_report_employee_" + leaveModel.getEmployeeId() + ".xlsx");
	        return new ResponseEntity<>(report, headers, HttpStatus.OK);
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	}

    
    @PostMapping("/findEmployeeLeaveDetailsForGrid")
    public ResponseEntity<Page<EmployeeLeaveEntity>> findEmployeeLeaveDetailsForGrid(@RequestBody EmployeeLeaveModel employeeLeaveModel) {
        try {
            Page<EmployeeLeaveEntity> leaveDetailsPage = employeeLeaveService.findEmployeeLeaveDetailsForGrid(employeeLeaveModel);
            return new ResponseEntity<>(leaveDetailsPage, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}