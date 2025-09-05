package com.pransquare.nems.controllers;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.dto.TimesheetReportRequestDto;
import com.pransquare.nems.entities.AttendanceEntity;
import com.pransquare.nems.entities.TimesheetDatesApprovalEntity;
import com.pransquare.nems.models.AttendanceApprovalModel;
import com.pransquare.nems.models.AttendanceDetailsSearchModel;
import com.pransquare.nems.models.SaveOrUpdateAttendanceModel;
import com.pransquare.nems.models.TimesheetReportModel;
import com.pransquare.nems.models.WeekendApprovalModel;
import com.pransquare.nems.services.EmployeeTimeSheetService;

import org.springframework.web.bind.annotation.RequestParam;

//@CrossOrigin("*")
@RestController
@RequestMapping("/Pransquare/nems/Timesheet/")
public class EmployeeTimeSheetController {

	private final EmployeeTimeSheetService employeeTimeSheetService;

	public EmployeeTimeSheetController(EmployeeTimeSheetService employeeTimeSheetService) {
		this.employeeTimeSheetService = employeeTimeSheetService;
	}

	@PostMapping(value = "/saveOrUpdateAttendance")
	public ResponseEntity<Map<String, Object>> saveOrUpdateAttendance(
			@RequestBody SaveOrUpdateAttendanceModel saveOrUpdateAttendanceModel) {
		Map<String, Object> response = employeeTimeSheetService.saveOrUpdateAttendance(saveOrUpdateAttendanceModel);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping(value = "/sendForApprovalAndApprove")
	public ResponseEntity<Map<String, Object>> sendForApprovalAndApprove(
			@RequestBody AttendanceApprovalModel attendanceApprovalModel) {
		Map<String, Object> response = employeeTimeSheetService.sendForApprovalAndApprove(attendanceApprovalModel);
		return ResponseEntity.ok().body(response);
	}

	// @PostMapping(value = "/saveToTimesheetMaster")
	// public ResponseEntity<Map<String, Object>> saveToTimesheetMaster(
	// @RequestBody AttendanceApprovalModel attendanceApprovalModel) {
	// Map<String, Object> response =
	// employeeTimeSheetService.saveToTimesheetMaster(attendanceApprovalModel);
	// return ResponseEntity.accepted().body(response);
	// }

	@PostMapping(value = "/serchAttendanceDetails")
	public ResponseEntity<Map<String, Object>> searchAttendanceDetails(
			@RequestBody AttendanceDetailsSearchModel attendanceDetailsSearchModel) {
		Map<String, Object> returnResponse = new HashMap<>();
		Page<AttendanceEntity> response = employeeTimeSheetService
				.searchAttendanceDetails(attendanceDetailsSearchModel);
		returnResponse.put("data", response);
		return ResponseEntity.ok().body(returnResponse);
	}

	@PostMapping(value = "/serchAttendanceDetails11")
	public ResponseEntity<Map<String, Object>> searchAttendanceDetails1(
			@RequestBody AttendanceDetailsSearchModel attendanceDetailsSearchModel) {
		LocalDate fromDate = LocalDate.of(2024, 11, 14);
		LocalDate toDateDate = LocalDate.of(2024, 07, 01);
		Map<String, Object> returnResponse = new HashMap<>();
		Object response = employeeTimeSheetService
				.fetchCodeAndDescription(36l, toDateDate, fromDate);
		returnResponse.put("data", response);
		return ResponseEntity.ok().body(returnResponse);
	}

	@PostMapping(value = "/timesheetReport")
	public ResponseEntity<Resource> timesheetReport(
			@RequestBody TimesheetReportModel timesheetReportModel) {
		String filepath = employeeTimeSheetService.createTimesheetReport(timesheetReportModel);
		try {
			// Define the path to the file
			File file = Paths.get(filepath).toFile();

			// Check if the file exists
			if (!file.exists()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			// Serve the file as a resource
			Resource resource = new FileSystemResource(file);

			// Set headers for the download response
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
			headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

			// Return the file resource in the response
			return ResponseEntity.ok().headers(headers).body(resource);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PostMapping(value = "/searchAttendance")
	public ResponseEntity<Map<String, Object>> searchAttendance(
			@RequestBody TimesheetReportModel timesheetReportModel) {
		Map<String, Object> returnResponse = new HashMap<>();
		List<AttendanceEntity> response = employeeTimeSheetService
				.searchAttendanceDetailsforReport(timesheetReportModel);
		returnResponse.put("data", response);
		return ResponseEntity.ok().body(returnResponse);
	}

	@PostMapping("/getWeekendApprovals")
	public ResponseEntity<Map<String, Page<TimesheetDatesApprovalEntity>>> getWeekendApprovals(
			@RequestBody WeekendApprovalModel weekendApprovalModel) {
		return employeeTimeSheetService.getWeekendApprovals(weekendApprovalModel);
	}

	@GetMapping("/weekendValidation")
	public ResponseEntity<TimesheetDatesApprovalEntity> weekendValidation(@RequestParam Long employeeId,
			@RequestParam LocalDate taskDate) {
		return employeeTimeSheetService.weekendValidation(employeeId, taskDate);
	}

	@PostMapping("/saveAndApproveWeekend")
	public ResponseEntity<String> saveAndApproveWeekend(
			@RequestBody TimesheetDatesApprovalEntity timesheetDatesApprovalEntity) {
		return employeeTimeSheetService.saveAndApproveWeekend(timesheetDatesApprovalEntity);
	}

	@PostMapping(value = "/searchAttendanceForGrid")
	public ResponseEntity<Map<String, Object>> searchAttendanceForGrid(
			@RequestBody TimesheetReportRequestDto timesheetReportModel) {
		Map<String, Object> returnResponse = new HashMap<>();
		List<AttendanceEntity> response = employeeTimeSheetService
				.searchAttendanceForGrid(timesheetReportModel);
		returnResponse.put("data", response);
		return ResponseEntity.ok().body(returnResponse);
	}
}